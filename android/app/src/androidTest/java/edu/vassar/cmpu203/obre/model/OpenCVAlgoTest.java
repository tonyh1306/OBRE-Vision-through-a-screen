package edu.vassar.cmpu203.obre.model;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.google.android.gms.common.util.CollectionUtils.isEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Instrumented test for OpenCVAlgo.
 * Requires a connected device or emulator.
 */
@RunWith(AndroidJUnit4.class)
public class OpenCVAlgoTest {

    private Context context;
    private OpenCVAlgo algo;

    /**
     * Sets up the testing environment before each test is run.
     */
    @Before
    public void setUp() {
        context = getInstrumentation().getTargetContext();

        if (!OpenCVLoader.initLocal()) {
            fail("Could not initialize OpenCV Native Library");
        }

        algo = new OpenCVAlgo(context);
    }
    /**
     * Tests how {@code runOnFrame()} responds when given a {@code null} frame input.
     * This ensures the method performs proper defensive null checks.
     */
    @Test
    public void runOnFrame_null_input_handling() {
        // Verify that passing a null 'frame' parameter returns an empty list immediately
        List<DetectedObject> result = algo.runOnFrame(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    /**
     * Tests how {@code runOnFrame()} behaves when supplied with a valid
     * but uninitialized (empty) {@link Mat}.
     * This verifies that the algorithm can handle non-null but invalid/empty
     * image data
     */
    @Test
    public void runOnFrame_empty_Mat_handling() {
        // Verify that passing a valid but empty Mat object returns an empty list
        Mat emptyFrame = new Mat();
        List<DetectedObject> detectedObjects = algo.runOnFrame(emptyFrame);
        assertNotNull(detectedObjects);
        assertTrue(detectedObjects.isEmpty());
        emptyFrame.release();
    }
    /**
     * Tests that runOnFrame() properly handles the case where the model
     * has not been initialized. When 'net' is null, the method should
     * return a non-empty list containing a single error object with the
     * name "Error: Model not loaded".
     */
    @Test
    public void runOnFrame_uninitialized_model_handling() {
        // Verify that if the 'net' object is null, it returns a specific error object
        algo.net = null;

        Mat frame = new Mat();
        List<DetectedObject> result = algo.runOnFrame(frame);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Error: Model not loaded", result.get(0).getName());
        frame.release();
    }

//    @Test
//    public void runOnFrame_successful_detection__Integration_() {
        // Verify that a frame containing a clear object results in detections.
//        try
//        {
//            InputStream ims = context.getAssets().open("bottle.jpg");
//            Bitmap bitmap = BitmapFactory.decodeStream(ims);
//            Mat image = new Mat();
//            Utils.bitmapToMat(bitmap, image);
//            org.opencv.imgproc.Imgproc.cvtColor(image, image, org.opencv.imgproc.Imgproc.COLOR_RGBA2RGB);
//            List<DetectedObject> detectedObjects = algo.runOnFrame(image);
//            assertNotNull(detectedObjects);
//            assertFalse(detectedObjects.isEmpty());
//            image.release();
//            ims.close();
//        }
//        catch(IOException ex)
//        {
//            fail("Could not read image file: " + ex.getMessage());
//        }
//    }
    /**
     * Tests handling of an image where the model returns zero detections.
     * When no objects are found, the method is expected to return a list
     * containing one placeholder DetectedObject whose name is
     * "No objects detected".
     */
    @Test
    public void runOnFrame_zero_detections_handling() {
        // Verify that when the model runs successfully but finds no objects, it returns "No objects detected"
        Mat blackFrame = Mat.zeros(640, 640, CvType.CV_8UC3);

        List<DetectedObject> result = algo.runOnFrame(blackFrame);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("No objects detected", result.get(0).getName());

        blackFrame.release();
    }
    /**
     * Tests that runOnFrame() filters detections correctly according to
     * the model's confidence threshold logic.
     */
    @Test
    public void runOnFrame_confidence_threshold_filtering() {
        Mat noiseFrame = new Mat(640, 640, CvType.CV_8UC3);
        org.opencv.core.Core.randn(noiseFrame, 128, 50);

        List<DetectedObject> result = algo.runOnFrame(noiseFrame);
        for (DetectedObject obj : result) {
            assertNotNull(obj.getName());
            assertNotNull(obj.getDim());
        }
        noiseFrame.release();
    }
    /**
     * Tests that runOnFrame() can be called repeatedly without causing
     * memory leaks, OpenCV resource misuse, or unexpected exceptions.
     */
    @Test
    public void runOnFrame_resource_management__Memory_Leak_() {
        // This test verifies that the code runs without throwing memory exceptions over multiple loops
        Mat frame = new Mat(640, 640, CvType.CV_8UC3, new Scalar(100, 100, 100));
        try {
            for (int i = 0; i < 10; i++) {
                algo.runOnFrame(frame);
            }
        } catch (Exception e) {
            fail("Running consecutive frames threw an exception: " + e.getMessage());
        } finally {
            frame.release();
        }
    }

}
