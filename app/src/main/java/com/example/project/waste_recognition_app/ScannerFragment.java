package com.example.project.waste_recognition_app;

import static android.content.ContentValues.TAG;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ScannerFragment extends Fragment {
    private LottieAnimationView camera;
    private ScrollView scrollView;
    private ImageView imageView, grey_bin, blue_bin, green_bin, backgroundAnimation;
    private TextView greyBinResult, blueBinResult, greenBinResult;
    private int imageSize = 300;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views so that we can use them later
        camera = view.findViewById(R.id.button);
        imageView = view.findViewById(R.id.imageView);
        grey_bin = view.findViewById(R.id.grey_bin);
        blue_bin = view.findViewById(R.id.blue_bin);
        green_bin = view.findViewById(R.id.green_bin);
        greyBinResult = view.findViewById(R.id.grey_bin_result);
        blueBinResult = view.findViewById(R.id.blue_bin_result);
        greenBinResult = view.findViewById(R.id.green_bin_result);
        scrollView = view.findViewById(R.id.scrollView);

        // Initialize the background animation
        backgroundAnimation = view.findViewById(R.id.scanner_background_animation);

        // Load the GIF into the ImageView using Glide
        Glide.with(requireContext())
                .asGif()
                .load(R.raw.scanner_background_animation)
                .into(backgroundAnimation);

        // Set up Lottie animation (start rotating when visible)
        camera.setAnimation(R.raw.scanner_icon);
        camera.playAnimation();
        camera.loop(true);

        // Set up Lottie button click listener
        camera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Hide background animation when camera button is clicked
                backgroundAnimation.setVisibility(View.GONE);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
                scrollView.scrollTo(0, 0);
                resetViewState();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == requireActivity().RESULT_OK && data != null) {
            if (requestCode == 3) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                // Classify the image immediately and update the UI
                String binType = classifyImage(image);
                showBinResult(binType);

                // Get current date and time
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                // Upload image to Firebase and save details including date and time
                uploadImageToFirebase(image, binType, currentDate, currentTime);
            }
        }
    }

    private void resetViewState() {
        // Hide GIF views and result TextViews
        grey_bin.setVisibility(View.GONE);
        blue_bin.setVisibility(View.GONE);
        green_bin.setVisibility(View.GONE);
        greyBinResult.setVisibility(View.GONE);
        blueBinResult.setVisibility(View.GONE);
        greenBinResult.setVisibility(View.GONE);
    }

    private void showBinResult(String binType) {
        // Show the correct bin text and GIF animation based on classification
        switch (binType) {
            case "Recyclable":
                greyBinResult.setText("This item should be placed in the Recyclable bin.");
                greyBinResult.setVisibility(View.VISIBLE);
                loadGifIntoImageView(grey_bin, R.raw.grey_dustbin_animation);
                break;

            case "Non-Recyclable":
                blueBinResult.setText("This item should be placed in the Non-Recyclable bin.");
                blueBinResult.setVisibility(View.VISIBLE);
                loadGifIntoImageView(blue_bin, R.raw.blue_dustbin_animation);
                break;

            case "Organic":
                greenBinResult.setText("This item should be placed in the Organic bin.");
                greenBinResult.setVisibility(View.VISIBLE);
                loadGifIntoImageView(green_bin, R.raw.green_dustbin_animation);
                break;

            default:
                Log.e(TAG, "Unknown bin type: " + binType);
                break;
        }
    }

    private void loadGifIntoImageView(ImageView imageView, int gifResource) {
        // Load the appropriate GIF and show the ImageView
        Glide.with(requireContext())
                .asGif()
                .load(gifResource)
                .into(imageView);
        imageView.setVisibility(View.VISIBLE);
    }

    private String classifyImage(Bitmap image) {
        try {
            Interpreter tfliteInterpreter = new Interpreter(loadModelFile());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 300, 300, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            TensorBuffer outputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 8}, DataType.FLOAT32);
            tfliteInterpreter.run(inputFeature0.getBuffer(), outputFeature0.getBuffer());

            float[] confidences = outputFeature0.getFloatArray();

            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"Battery", "Cardboard", "Food Waste", "Glass", "Metal", "Paper", "Plastic", "Trash"};
            tfliteInterpreter.close();

            return displayGarbageDisposalBin(classes[maxPos]);
        } catch (IOException e) {
            Log.e(TAG, "Error during model inference", e);
            return "Unknown";
        }
    }

    private String displayGarbageDisposalBin(String item) {
        String bin = "";
        switch (item) {
            case "Cardboard":
            case "Plastic":
            case "Paper":
            case "Metal":
                bin = "Recyclable";
                break;

            case "Glass":
            case "Trash":
            case "Battery":
                bin = "Non-Recyclable";
                break;

            case "Food Waste":
                bin = "Organic";
                break;
        }

        return bin;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = requireContext().getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void uploadImageToFirebase(Bitmap image, String binType, String date, String time) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String uniqueId = String.valueOf(System.currentTimeMillis());
        StorageReference storageRef = storage.getReference().child("images/" + uniqueId + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            saveToFirestore(uniqueId, binType, imageUrl, date, time);
        })).addOnFailureListener(e -> Log.e(TAG, "Failed to upload image", e));
    }

    private void saveToFirestore(String uniqueId, String binType, String imageUrl, String date, String time) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userId == null) {
            Log.e(TAG, "User not logged in. Cannot save item.");
            return;
        }

        Map<String, Object> scannedItem = new HashMap<>();
        scannedItem.put("binType", binType);
        scannedItem.put("img", imageUrl);
        scannedItem.put("date", date);
        scannedItem.put("time", time);
        scannedItem.put("timestamp", uniqueId);  // Add timestamp for sorting

        db.collection("users").document(userId).collection("scanned_items")
                .document(uniqueId)
                .set(scannedItem)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Item added to Firestore with ID: " + uniqueId))
                .addOnFailureListener(e -> Log.e(TAG, "Error adding item to Firestore", e));
    }
}