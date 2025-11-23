package edu.vassar.cmpu203.obre.controller;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import org.opencv.core.Core; // <--- ADD THIS IMPORT
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.vassar.cmpu203.obre.model.DetectedObject;
import edu.vassar.cmpu203.obre.model.OpenCVAlgo;
import edu.vassar.cmpu203.obre.view.VideoStreamFragment;

/**
 * Manages the CameraX lifecycle, frame analysis, and object detection pipeline.
 * <p>
 * This class is responsible for:
 * <ul>
 *     <li>Binding the camera to the lifecycle of the activity.</li>
 *     <li>Converting camera frames (ImageProxy) to OpenCV Mat objects.</li>
 *     <li>Running object detection on a background thread.</li>
 *     <li>Updating the UI with detection results.</li>
 * </ul>
 */
public class CameraController {
    private static final String TAG = "CameraController";
    private final MainActivity mainActivity;
    private final ExecutorService cameraExecutor;
    private final ExecutorService detectionExecutor;
    private OpenCVAlgo detector;
    private final VideoStreamFragment fragment;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
    private ProcessCameraProvider cameraProvider;
    private volatile boolean isRunning = false;
    private Mat latestFrame = new Mat();
    private byte[] cachedI420Buffer;

    /**
     * Initializes the camera controller and starts the camera immediately.
     *
     * @param activity The main activity context, used for lifecycle binding.
     * @param frag The fragment where the camera preview and overlays are displayed.
     */
    public CameraController(MainActivity activity, VideoStreamFragment frag) {
        this.mainActivity = activity;
        cameraExecutor = Executors.newSingleThreadExecutor();
        detectionExecutor = Executors.newSingleThreadExecutor();
        this.fragment = frag;
        detector = new OpenCVAlgo(activity);

        startCamera();
    }

    /**
     * Asynchronously initializes the CameraX provider.
     * <p>
     * This method requests the {@link ProcessCameraProvider} instance. Once the provider
     * is available, it calls {@link #bindCameraUseCases()} to set up the preview and
     * image analysis. If initialization fails, an error is logged and displayed on the UI.
     */

    private void startCamera() {
        com.google.common.util.concurrent.ListenableFuture<ProcessCameraProvider> cameraProviderListenerFuture = ProcessCameraProvider.getInstance(mainActivity);
        cameraProviderListenerFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderListenerFuture.get();
                bindCameraUseCases();

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera start failed", e);
                fragment.displayError("Camera start failed");
            }
        }, ContextCompat.getMainExecutor(mainActivity));
    }

    /**
     * Binds the camera preview and image analysis use cases to the lifecycle owner.
     * Also sets up the analyzer to process frames.
     */
    public void bindCameraUseCases() {
        if (fragment.getPreviewView() == null) {
            Log.e(TAG, "PreviewView is null. Cannot bind camera.");
            return;
        }

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(fragment.getPreviewView().getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor, this::analyzeImage);
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(mainActivity, cameraSelector, preview, imageAnalysis);
        isRunning = true;
        startDetection();

        Log.d(TAG, "Camera started successfully");
        fragment.presentAllowed("Camera started successfully");
    }

    /**
     * Analyzes a single frame from the camera.
     * Converts the YUV image to an RGB Mat, handles rotation, and updates the latest frame buffer.
     *
     * @param imageProxy The image frame provided by CameraX.
     */
    public void analyzeImage(ImageProxy imageProxy) {
        if (!isRunning) {
            imageProxy.close();
            return;
        }
        try {
            // 1. Get the rotation required (usually 90 for back camera portrait)
            int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
            Mat frame = imageProxyToMat(imageProxy);

            if (!frame.empty()) {
                // 2. Rotate the Mat so the AI sees an upright image
                if (rotationDegrees != 0) {
                    Mat rotated = new Mat();
                    if (rotationDegrees == 90) {
                        Core.rotate(frame, rotated, Core.ROTATE_90_CLOCKWISE);
                    } else if (rotationDegrees == 180) {
                        Core.rotate(frame, rotated, Core.ROTATE_180);
                    } else if (rotationDegrees == 270) {
                        Core.rotate(frame, rotated, Core.ROTATE_90_COUNTERCLOCKWISE);
                    } else {
                        frame.copyTo(rotated);
                    }

                    // Swap frames
                    frame.release();
                    frame = rotated;
                }

                synchronized (latestFrame) {
                    if (!latestFrame.empty()) {
                        latestFrame.release();
                    }
                    latestFrame = frame; // Ownership transferred
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing image", e);
        } finally {
            imageProxy.close();
        }
    }

    /**
     * Starts the continuous object detection loop on a background thread.
     * <p>
     * This method runs on the {@code detectionExecutor}. It continuously retrieves the
     * {@code latestFrame}, runs the {@link OpenCVAlgo} detector on it, and posts the
     * resulting list of {@link DetectedObject}s back to the main thread to update the UI overlay.
     * <p>
     * The loop is throttled with a 30ms sleep to prevent CPU exhaustion, targeting approx. 30 FPS.
     */
    private void startDetection() {
        detectionExecutor.execute(() -> {
            while (isRunning) {
                Mat frame = null;
                synchronized (latestFrame) {
                    if (!latestFrame.empty()) {
                        frame = latestFrame.clone();
                    }
                }

                if (frame != null) {
                    List<DetectedObject> detections = detector.runOnFrame(frame);
                    frame.release();
                    // Pass results back to UI thread to update overlay
                    mainActivity.runOnUiThread(() -> fragment.updateDetections(detections));
                }

                try {
                    Thread.sleep(30); // Throttle detection to ~30fps
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    /**
     * Converts a CameraX {@link ImageProxy} (YUV_420_888) into an OpenCV {@link Mat} (RGB).
     * <p>
     * Since CameraX produces images in YUV format with potentially different row/pixel strides,
     * this method manually extracts the Y, U, and V planes, consolidates them into a contiguous
     * I420 byte array, and then uses {@link Imgproc#cvtColor} to convert to RGB.
     *
     * @param imageProxy The image frame provided by the CameraX ImageAnalysis use case.
     * @return An OpenCV Mat containing the RGB representation of the image.
     */
    @NonNull
    private Mat imageProxyToMat(ImageProxy imageProxy) {
        // Force disable threading to prevent SIGILL on emulator
        org.opencv.core.Core.setNumThreads(0);

        ImageProxy.PlaneProxy[] planes = imageProxy.getPlanes();
        int width = imageProxy.getWidth();
        int height = imageProxy.getHeight();

        int frameSize = width * height;
        int qSize = frameSize / 4;
        int totalSize = frameSize + qSize * 2;

        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int uvRowStride = planes[1].getRowStride();
        int uvPixelStride = planes[1].getPixelStride();
        int yRowStride = planes[0].getRowStride();

        if (cachedI420Buffer == null || cachedI420Buffer.length != totalSize) {
            cachedI420Buffer = new byte[totalSize];
        }

        byte[] i420 = cachedI420Buffer;

        // 1. Copy Y Plane
        int pos = 0;
        for (int row = 0; row < height; row++) {
            yBuffer.position(row * yRowStride);
            yBuffer.get(i420, pos, width);
            pos += width;
        }

        // 2. Copy U and V Planes (de-interleave)
        int chromaHeight = height / 2;
        int chromaWidth = width / 2;
        byte[] uRow = new byte[uvRowStride];
        byte[] vRow = new byte[uvRowStride];
        int uOffset = frameSize;
        int vOffset = frameSize + qSize;
        int chromaPos = 0;

        for (int row = 0; row < chromaHeight; row++) {
            uBuffer.position(row * uvRowStride);
            vBuffer.position(row * uvRowStride);
            uBuffer.get(uRow, 0, uvRowStride);
            vBuffer.get(vRow, 0, uvRowStride);

            for (int col = 0; col < chromaWidth; col++) {
                i420[uOffset + chromaPos] = uRow[col * uvPixelStride];
                i420[vOffset + chromaPos] = vRow[col * uvPixelStride];
                chromaPos++;
            }
        }

        // 3. Convert I420 -> RGB
        Mat yuvMat = new Mat(height + height / 2, width, CvType.CV_8UC1);
        yuvMat.put(0, 0, i420);

        Mat rgbMat = new Mat();
        Imgproc.cvtColor(yuvMat, rgbMat, Imgproc.COLOR_YUV2RGB_I420);
        yuvMat.release();

        return rgbMat;
    }

    /**
     * Stops the camera, shuts down background executors, and releases OpenCV resources.
     * Should be called when the camera is no longer needed.
     */
    public void stop() {
        isRunning = false;
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        if (cameraExecutor != null) cameraExecutor.shutdown();
        if (detectionExecutor != null) detectionExecutor.shutdown();

        synchronized (latestFrame) {
            if (!latestFrame.empty()) latestFrame.release();
        }
    }

    /**
     * Toggles between the front and back cameras.
     */
    public void switchCamera() {
        cameraSelector = (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                ? CameraSelector.DEFAULT_FRONT_CAMERA
                : CameraSelector.DEFAULT_BACK_CAMERA;
        if (cameraProvider != null) {
            bindCameraUseCases();
        }
    }
}
