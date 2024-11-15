package com.example.project.waste_recognition_app;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScannerFragment extends Fragment {

    private static final String TAG = "ScannerFragment";
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final String[] REQUIRED_PERMISSIONS = {"android.permission.CAMERA", "android.permission.INTERNET"};

    private PreviewView previewView;
    private ImageButton captureButton;
    private Interpreter tfliteInterpreter;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previewView = view.findViewById(R.id.view_finder);
        captureButton = view.findViewById(R.id.imgCapture);

        captureButton.setAlpha(0.5f);
        captureButton.setClickable(false);

        if (allPermissionsGranted()) {
            initializeModel();
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void initializeModel() {
        try {
            // Load the TFLite model
            tfliteInterpreter = new Interpreter(FileUtil.loadMappedFile(requireContext(), "your_model.tflite"));
            Log.d(TAG, "TFLite model loaded successfully");

            // Initialize camera preview after model load
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindCamera(cameraProvider);
                } catch (Exception e) {
                    Log.e(TAG, "Error initializing camera preview: ", e);
                }
            }, ContextCompat.getMainExecutor(requireContext()));

        } catch (IOException e) {
            Log.e(TAG, "Error loading TFLite model: ", e);
        }
    }

    private void bindCamera(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageCapture imageCapture = new ImageCapture.Builder().setTargetRotation(requireView().getDisplay().getRotation()).build();

        captureButton.setOnClickListener(view -> imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                Bitmap bitmap = convertImageProxyToBitmap(image);
                if (bitmap != null) {
                    float[] results = runModel(bitmap);
                    Log.d(TAG, "Inference results: " + java.util.Arrays.toString(results));
                }
                image.close();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e(TAG, "Error capturing image: " + exception.getMessage());
            }
        }));

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageCapture, preview);

        captureButton.setAlpha(1f);
        captureButton.setClickable(true);
    }

    private float[] runModel(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());

        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int pixel = bitmap.getPixel(x, y);
                inputBuffer.putFloat((Color.red(pixel) - 127) / 255.0f);
                inputBuffer.putFloat((Color.green(pixel) - 127) / 255.0f);
                inputBuffer.putFloat((Color.blue(pixel) - 127) / 255.0f);
            }
        }

        ByteBuffer outputBuffer = ByteBuffer.allocateDirect(3 * Float.SIZE / Byte.SIZE).order(ByteOrder.nativeOrder());
        tfliteInterpreter.run(inputBuffer, outputBuffer);

        float[] probabilities = new float[outputBuffer.asFloatBuffer().limit()];
        outputBuffer.asFloatBuffer().get(probabilities);
        return probabilities;
    }

    private Bitmap convertImageProxyToBitmap(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}