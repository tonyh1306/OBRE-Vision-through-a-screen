package edu.vassar.cmpu203.obre.model;

import static org.opencv.imgproc.Imgproc.resize;

import android.content.Context;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect2d;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements object detection using OpenCV and the YOLO neural network model.
 */
public class OpenCVAlgo implements MediaAlgo {
    public MediaSource mediaSource;
    public static int IMG_SIZE = 640;
    Net net;
    List<DetectedObject> detectedObjects;

    /**
     * Initializes the YOLO model by loading the ONNX file from resources.
     *
     * @param context Application context used to access raw resources.
     */
    public OpenCVAlgo(Context context) {
        try {
            String modelPath = ResourceUtils.copyResourceToFile(context,
                    context.getResources().getIdentifier("yolo12n", "raw", context.getPackageName()),
                    "yolo12n.onnx");
            net = Dnn.readNetFromONNX(modelPath);
            org.opencv.core.Core.setNumThreads(0);
            net.setPreferableBackend(Dnn.DNN_BACKEND_DEFAULT);
            net.setPreferableTarget(Dnn.DNN_TARGET_CPU);
        } catch (Exception e) {
            Log.e("OpenCVAlgo", "Error loading YOLO model", e);
            net = null;
        }
        detectedObjects = new ArrayList<>();
    }

    /**
     * Processes a single frame to detect objects. This involves resizing, blob creation,
     * running the neural network, and filtering results with non-maximum suppression (NMS).
     *
     * @param image The original image frame from the camera.
     * @param resizedImage A Mat object to store the resized image.
     * @param netSize The target size for the neural network input.
     */
    private void processFrame(Mat image, Mat resizedImage, Size netSize) {
        if (net == null || image == null || image.empty()) return;

        // 1) Resize
        resize(image, resizedImage, netSize);

        // 2) Blob
        Mat inputBlob = Dnn.blobFromImage(resizedImage, 1.0 / 255.0, netSize, new Scalar(0, 0, 0), true, false);
        net.setInput(inputBlob);

        // 3) Forward
        Mat outputs = net.forward();

        // 4) Reshape
        Mat out = outputs.reshape(1, 84);
        Core.transpose(out, out);

        // 5) Filter boxes
        final float CONF_THRESH = 0.50f;
        final float NMS_THRESH = 0.45f;

        List<Rect2d> boxes = new ArrayList<>();
        List<Float> scores = new ArrayList<>();
        List<Integer> classIds = new ArrayList<>();

        for (int i = 0; i < out.rows(); i++) {
            double best = -1;
            int bestId = -1;
            for (int c = 0; c < COCO_CLASSES.length; c++) {
                double s = out.get(i, 4 + c)[0];
                if (s > best) {
                    best = s;
                    bestId = c;
                }
            }
            if (best < CONF_THRESH) continue;

            double cx = out.get(i, 0)[0];
            double cy = out.get(i, 1)[0];
            double w = out.get(i, 2)[0];
            double h = out.get(i, 3)[0];

            double x = cx - w / 2.0;
            double y = cy - h / 2.0;

            boxes.add(new Rect2d(x, y, w, h));
            scores.add((float) best);
            classIds.add(bestId);
        }

        // NMS
        MatOfRect2d nmsBoxes = new MatOfRect2d();
        nmsBoxes.fromList(boxes);
        MatOfFloat nmsScores = new MatOfFloat();
        nmsScores.fromList(scores);
        MatOfInt keep = new MatOfInt();
        Dnn.NMSBoxes(nmsBoxes, nmsScores, CONF_THRESH, NMS_THRESH, keep);

        if (keep.rows() > 0) {
            int[] keptIdx = keep.toArray();
            for (int idx : keptIdx) {
                Rect2d b = boxes.get(idx);
                String name = COCO_CLASSES[classIds.get(idx)];
                detectedObjects.add(new DetectedObject(name, b.x, b.y, b.width, b.height));
            }
        }
    }


    /**
     * Runs object detection on a single frame.
     *
     * @param frame The input image matrix (Mat) to analyze.
     * @return A list of DetectedObject instances containing class names and bounding boxes.
     */
    public synchronized List<DetectedObject> runOnFrame(Mat frame) {
        detectedObjects.clear();
        if (net == null) {
            detectedObjects.add(new DetectedObject("Error: Model not loaded", 0, 0, 0, 0));
            return new ArrayList<>(detectedObjects);
        }

        if (frame == null || frame.empty()) {
            return new ArrayList<>(detectedObjects);
        }

        final Size NET_SIZE = new Size(IMG_SIZE, IMG_SIZE);
        Mat resized = new Mat();
        try {
            processFrame(frame, resized, NET_SIZE);
            if (detectedObjects.isEmpty()) {
                detectedObjects.add(new DetectedObject("No objects detected", 0, 0, 0, 0));
            }
            return new ArrayList<>(detectedObjects);
        } finally {
            if (!resized.empty()) resized.release();
        }
    }

    private static final String[] COCO_CLASSES = {"person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck", "boat", "traffic light", "fire hydrant", "stop sign", "parking meter", "bench", "bird", "cat", "dog", "horse", "sheep", "cow", "elephant", "bear", "zebra", "giraffe", "backpack", "umbrella", "handbag", "tie", "suitcase", "frisbee", "skis", "snowboard", "sports ball", "kite", "baseball bat", "baseball glove", "skateboard", "surfboard", "tennis racket", "bottle", "wine glass", "cup", "fork", "knife", "spoon", "bowl", "banana", "apple", "sandwich", "orange", "broccoli", "carrot", "hot dog", "pizza", "donut", "cake", "chair", "couch", "potted plant", "bed", "dining table", "toilet", "tv", "laptop", "mouse", "remote", "keyboard", "cell phone", "microwave", "oven", "toaster", "sink", "refrigerator", "book", "clock", "vase", "scissors", "teddy bear", "hair drier", "toothbrush"};
}
