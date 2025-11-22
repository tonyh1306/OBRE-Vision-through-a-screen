package edu.vassar.cmpu203.obre.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.OpenCVLoader;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.view.MainUI;
import edu.vassar.cmpu203.obre.view.UploadImageFragment;
import edu.vassar.cmpu203.obre.view.UploadImageUI;
import edu.vassar.cmpu203.obre.view.VideoStreamFragment;
import edu.vassar.cmpu203.obre.view.VideoStreamUI;

public class MainActivity extends AppCompatActivity implements VideoStreamUI.Listener, UploadImageUI.Listener {
    private static final List<String> CAMERAX_PERMISSION = Arrays.asList(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    );
    private static final String TAG = "CameraXVideo";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private PreviewView previewView;
    private Button recordButton;
    private ExecutorService cameraExecutor;
    private VideoCapture<Recorder> videoCapture;
    private Recording currentRecording;
    private Uri lastRecordedVideoUri; // For potential playback screen logic
    // Enum state is handled implicitly by checking if currentRecording is null
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

    enum Options {
        UPLOAD_IMAGE,
        VIDEO_STREAM,
        EXIT()
    }

    private Options state = Options.VIDEO_STREAM;
    private MainUI mainui; // Reference to the fragment manager class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainui = new MainUI(this);

        setContentView(mainui.getRootView());

        previewView = findViewById(R.id.previewView);

        cameraExecutor = Executors.newSingleThreadExecutor();


        if (hasRequiredPermission()) {
            try {
                startCamera();
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera start failed", e);
                Toast.makeText(this, "Failed to start camera.", Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(
                    this, CAMERAX_PERMISSION.toArray(new String[0]), REQUEST_CODE_PERMISSIONS
            );
        }

        if (OpenCVLoader.initLocal()) {
            Log.i(TAG, "OpenCV loaded successfully");
        } else {
            Log.e(TAG, "OpenCV initialization failed!");
            (Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG)).show();
            return;
        }
    }

    private boolean hasRequiredPermission() {
        return CAMERAX_PERMISSION.stream().allMatch(permission ->
                ContextCompat.checkSelfPermission(
                        getApplicationContext(),
                        permission
                ) == PackageManager.PERMISSION_GRANTED
        );
    }

    private void showVideoStream() {
        mainui.displayFragment(new VideoStreamFragment());
    }

    private void showImageUpload() {
        mainui.displayFragment(new UploadImageFragment());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (hasRequiredPermission()) {
                try {
                    startCamera();
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "Camera failed with permission", e);
                    Toast.makeText(this, "Failed to start camera.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission not granted by user.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public boolean cameraPermissionState() {
        if (hasRequiredPermission()) {
            return true;
        } else {
            return false;
        }
    }

    private void startCamera() throws ExecutionException, InterruptedException {
        com.google.common.util.concurrent.ListenableFuture<ProcessCameraProvider> cameraProviderListenerFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenerFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenerFuture.get();

                if (previewView == null) {
                    Log.e(TAG, "PreviewView has not been initialized.");
                    return;
                }

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HD))
                        .build();

                videoCapture = VideoCapture.withOutput(recorder);


                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera initialization failed.", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }


    @SuppressLint("MissingPermission")
    private void onToggleRecording() {
        if (videoCapture == null) {
            return;
        }

        if (currentRecording == null) {
            String name = "CameraX-recording-" + new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                    .format(System.currentTimeMillis()) + ".mp4";
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, name);


            MediaStoreOutputOptions outputOptions = new MediaStoreOutputOptions.Builder(
                    getContentResolver(),
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                    .setContentValues(contentValues)
                    .build();

            currentRecording = videoCapture.getOutput()
                    .prepareRecording(this, outputOptions)
                    .withAudioEnabled()
                    .start(ContextCompat.getMainExecutor(this), this::handleVideoRecorderEvent);
        } else {
            // Stop recording
            currentRecording.stop();
            currentRecording = null;
            // Update UI (e.g., set button text to "Start")
            // recordButton.setText("Start Recording");
        }
    }

    private void handleVideoRecorderEvent(VideoRecordEvent event) {
        if (event instanceof VideoRecordEvent.Start) {
            Log.d(TAG, "Recording started");
        } else if (event instanceof VideoRecordEvent.Finalize) {
            VideoRecordEvent.Finalize finalizeEvent = (VideoRecordEvent.Finalize) event;
            if (finalizeEvent.hasError()) {
                Log.e(TAG, "Recording error: " + finalizeEvent.getError() + ", " + finalizeEvent.getCause());
                Toast.makeText(this, "Recording error: " + finalizeEvent.getError(), Toast.LENGTH_SHORT).show();
            } else {
                lastRecordedVideoUri = finalizeEvent.getOutputResults().getOutputUri();
                Log.d(TAG, "Recording finalized: " + lastRecordedVideoUri);
                Toast.makeText(this, "Video saved: " + lastRecordedVideoUri, Toast.LENGTH_SHORT).show();
                // Transition to playback screen logic here
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }

    @Override
    public void onCameraViewStarted() {

    }

    @Override
    public void onStartStream(VideoStreamUI ui) {

    }

    @Override
    public void onStopStream(VideoStreamUI ui) {

    }

}