package com.example.project.waste_recognition_app;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.tensorflow.lite.support.common.FileUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ScannerFragment extends Fragment {
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.INTERNET"};

    private PreviewView previewView;
    private ImageButton imgButton;

    private final String[] labels = {"Recycling", "Garbage", "Compost"};

    private Interpreter tfliteInterpreter;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private View scannerFragmentView;

    public ScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previewView = view.findViewById(R.id.view_finder);
        imgButton = view.findViewById(R.id.imgCapture);

        scannerFragmentView = view;

        imgButton.setAlpha(.5f);
        imgButton.setClickable(false);

        if(allPermissionsGranted()){
            initializeModel();
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void initializeModel() {
        try {
            // Load the model from assets (or update to your model path)
            tfliteInterpreter = new Interpreter(FileUtil.loadMappedFile(requireContext(), "your_model.tflite"));
            Log.d("ScannerFragment", "Interpreter initialized successfully.");

            // Enable camera preview after model load
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    Log.e("ScannerFragment", "Error initializing camera preview: ", e);
                }
            }, ContextCompat.getMainExecutor(requireContext()));

        } catch (IOException e) {
            Log.e("ScannerFragment", "Error loading model: ", e);
        }
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        // Setup preview and image capture
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageCapture imageCapture = new ImageCapture.Builder().setTargetRotation(scannerFragmentView.getDisplay().getRotation()).build();

        imgButton.setOnClickListener(view -> imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                new Handler(Looper.getMainLooper()).post(() -> cameraProvider.unbind(preview));

                Bitmap bitmap = imageProxyToBitmap(image);
                if (bitmap != null) {
                    // Preprocess and run model inference
                    float[] probabilities = runModel(bitmap);
                    displayResults(probabilities);
                }
                image.close();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e("ScannerFragment", "Image capture failed: " + exception.getMessage());
            }
        }));

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageCapture, preview);
        imgButton.setAlpha(1f);
        imgButton.setClickable(true);
    }

    private float[] runModel(Bitmap bitmap) {
        // Resize and prepare byte buffer
        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        ByteBuffer input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());
        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int px = bitmap.getPixel(x, y);
                float rf = (Color.red(px) - 127) / 255.0f;
                float gf = (Color.green(px) - 127) / 255.0f;
                float bf = (Color.blue(px) - 127) / 255.0f;
                input.putFloat(rf);
                input.putFloat(gf);
                input.putFloat(bf);
            }
        }

        ByteBuffer output = ByteBuffer.allocateDirect(3 * Float.SIZE / Byte.SIZE).order(ByteOrder.nativeOrder());
        tfliteInterpreter.run(input, output);

        float[] probabilities = new float[output.asFloatBuffer().limit()];
        output.asFloatBuffer().get(probabilities);
        return probabilities;
    }

    private void displayResults(float[] probabilities) {
        ResultsDisplayFragment resultsFragment = ResultsDisplayFragment.newInstance(labels, probabilities);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, resultsFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}
