package edu.vassar.cmpu203.obre.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.OpenCVLoader;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.model.OpenCVAlgo;
import edu.vassar.cmpu203.obre.model.ResourceUtils;
import edu.vassar.cmpu203.obre.view.MainUI;
import edu.vassar.cmpu203.obre.view.UploadImageUI;
import edu.vassar.cmpu203.obre.view.VideoStreamFragment;
import edu.vassar.cmpu203.obre.view.VideoStreamUI;

public class MainActivity extends AppCompatActivity implements VideoStreamUI.Listener {
    private static final List<String> CAMERAX_PERMISSION = Arrays.asList(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    );
    private static final String TAG = "CameraXVideo";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private Button recordButton;
    private ExecutorService cameraExecutor;
    private VideoCapture<Recorder> videoCapture;
    private Recording currentRecording;
    private Uri lastRecordedVideoUri; // For potential playback screen logic
    // Enum state is handled implicitly by checking if currentRecording is null
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
    private CameraController cameraController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainUI mainui = new MainUI(this);
        setContentView(mainui.getRootView());
        VideoStreamFragment fragment = new VideoStreamFragment();
        fragment.setListener(this);
        mainui.displayFragment(fragment);

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
            }
            else {
                ActivityCompat.requestPermissions(
                        this, CAMERAX_PERMISSION.toArray(new String[0]), REQUEST_CODE_PERMISSIONS
                );
            }
        }
        else {
            Log.e(TAG, "OpenCV initialization failed!");
            fragment.displayError("OpenCV initialization failed!");
            return;
        }

        cameraExecutor = Executors.newSingleThreadExecutor();
    }


    private boolean hasRequiredPermission() {
        return CAMERAX_PERMISSION.stream().allMatch(permission ->
                ContextCompat.checkSelfPermission(
                        getApplicationContext(),
                        permission
                ) == PackageManager.PERMISSION_GRANTED
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

    @Override
    public void onUploadImage() {
        // TODO: implement next phase
    }

    @Override
    public void onDoneUploadingImage() {
        // TODO: implement next phase
    }

    @Override
    public void onExit() {
        this.finish();
    }

    @Override
    public void onStartStream(VideoStreamUI ui) {
        VideoStreamFragment fragment = (VideoStreamFragment) ui;
        try {
            cameraController = new CameraController(this, fragment);
        } catch (ExecutionException | InterruptedException e) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }

}