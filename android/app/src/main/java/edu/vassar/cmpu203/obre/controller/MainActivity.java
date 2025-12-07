package edu.vassar.cmpu203.obre.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.vassar.cmpu203.obre.model.LLMAlgo;
import edu.vassar.cmpu203.obre.model.Ledger;
import edu.vassar.cmpu203.obre.view.MainUI;
import edu.vassar.cmpu203.obre.view.ResultFragment;
import edu.vassar.cmpu203.obre.view.ResultUI;
import edu.vassar.cmpu203.obre.view.UploadImageFragment;
import edu.vassar.cmpu203.obre.view.VideoStreamFragment;
import edu.vassar.cmpu203.obre.view.VideoStreamUI;

/**
 * The main entry point of the application.
 * <p>
 * This Activity acts as the primary Controller in the MVC architecture. It handles:
 * <ul>
 *     <li>Navigation between fragments (Video Stream, Upload Image, Results).</li>
 *     <li>Runtime permission requests for the Camera.</li>
 *     <li>Coordination between the UI views and the Model logic (LLMAlgo, CameraController).</li>
 * </ul>
 */
public class MainActivity extends AppCompatActivity implements VideoStreamUI.Listener, UploadImageFragment.Listener, ResultUI

        .Listener {

    private static final List<String> CAMERAX_PERMISSION = Arrays.asList(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    );
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_PERMISSIONS = 10;

    private ExecutorService cameraExecutor;
    private CameraController cameraController;
    private MainUI mainUI;
    private UploadImageFragment uploadFragment;
    private ExecutorService textExecutor;
    Ledger ledger;

    // Image Picker Launcher
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        if (uploadFragment != null) {
                            uploadFragment.updateImagePreview(bitmap);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to load image", e);
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainUI = new MainUI(this);
        setContentView(mainUI.getRootView());

        VideoStreamFragment fragment = new VideoStreamFragment();
        fragment.setListener(this);
        mainUI.displayFragment(fragment);

        if (OpenCVLoader.initLocal()) {
            org.opencv.core.Core.setNumThreads(0);
            Log.i(TAG, "OpenCV loaded successfully");
            if (hasRequiredPermission()) {
                getSupportFragmentManager().registerFragmentLifecycleCallbacks(new androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewCreated(@NonNull androidx.fragment.app.FragmentManager fm, @NonNull androidx.fragment.app.Fragment f, @NonNull android.view.View v, @Nullable Bundle savedInstanceState) {
                        if (f == fragment) {
                            onStartStream(fragment);
                            fm.unregisterFragmentLifecycleCallbacks(this);
                        }
                    }
                }, false);
            } else {
                ActivityCompat.requestPermissions(
                        this, CAMERAX_PERMISSION.toArray(new String[0]), REQUEST_CODE_PERMISSIONS
                );
            }
        } else {
            Log.e(TAG, "OpenCV initialization failed!");
            fragment.displayError("OpenCV initialization failed!");
            return;
        }
        cameraExecutor = Executors.newSingleThreadExecutor();
        textExecutor = Executors.newSingleThreadExecutor();
        TextRecognizer textScanner = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    private boolean hasRequiredPermission() {
        return CAMERAX_PERMISSION.stream().allMatch(permission ->
                ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!hasRequiredPermission()) {
                Toast.makeText(this, "Permission not granted by user.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * Callback triggered when the VideoStreamFragment is ready to start the camera.
     * Initializes the CameraController.
     *
     * @param ui The UI interface representing the video stream view.
     */
    @Override
    public void onStartStream(VideoStreamUI ui) {
        VideoStreamFragment fragment = (VideoStreamFragment) ui;
        try {
            cameraController = new CameraController(this, fragment);
        } catch (Exception e) {
            Log.e(TAG, "Failed to start camera", e);
            fragment.displayError("Failed to start camera");
        }
    }

    @Override
    public void onStopStream(VideoStreamUI ui) {
        if (cameraController != null) {
            cameraController.stop();
            cameraController = null;
        }
    }

    /**
     * Callback triggered to switch the active camera lens (front/back).
     */
    @Override
    public void onSwitchCamera() {
        if (cameraController != null) {
            cameraController.switchCamera();
        } else {
            Log.e(TAG, "CameraController is null, cannot switch");
        }
    }


    /**
     * Callback triggered when the user requests to analyze a static image.
     * Stops the live camera and navigates to the UploadImageFragment.
     */
    public void onUploadImageRequested() {
        if (cameraController != null) {
            cameraController.stop();
            cameraController = null;
        }

        uploadFragment = new UploadImageFragment();
        uploadFragment.setListener(this);
        mainUI.displayFragment(uploadFragment);
    }

    /**
     * Launches the system image picker to allow the user to select a photo.
     */
    @Override
    public void onPickImageRequested() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    /**
     * Sends the selected image to the Gemini LLM for analysis.
     * On success, navigates to the ResultFragment.
     *
     * @param image The Bitmap image to analyze.
     */
    @Override
    public void onAnalyzeImageRequested(Bitmap image) {
        LLMAlgo llm = new LLMAlgo();

        llm.sendImageToGemini(image, new LLMAlgo.Listener() {
            @Override
            public void onSuccess(String responseText) {
                runOnUiThread(() -> {
                    ResultFragment resultFragment = new ResultFragment(responseText);
                    resultFragment.setListener(MainActivity.this);
                    mainUI.displayFragment(resultFragment, true);
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Gemini Error", e);
                runOnUiThread(() -> {
                    if (uploadFragment != null) uploadFragment.onAnalysisFailed();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


    /**
     * Handles navigation back to the live video stream from the upload screen.
     * Ensures the view is fully created before re-initializing the camera.
     */
    @Override
    public void onSwitchBackToStream() {
        VideoStreamFragment fragment = new VideoStreamFragment();
        fragment.setListener(this);
        mainUI.displayFragment(fragment);
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(@NonNull androidx.fragment.app.FragmentManager fm, @NonNull androidx.fragment.app.Fragment f, @NonNull android.view.View v, @Nullable Bundle savedInstanceState) {
                if (f == fragment) {
                    onStartStream(fragment);
                    fm.unregisterFragmentLifecycleCallbacks(this);
                }
            }
        }, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }

    @Override
    public void onSwitchToUpload(String detection) {
        uploadFragment.updateDetections(detection);

        File outfile = new File(getFilesDir(), "detections.txt");
        try {
            FileOutputStream fos = new FileOutputStream(outfile, true);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.ledger);
            fos.close();
            oos.close();
        } catch (Exception e) {
            Log.e(TAG, "Error writing to file", e);
        }
    }

}
