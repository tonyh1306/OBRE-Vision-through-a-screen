package model;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class OpenCVAlgo implements MediaAlgo {

    static String MODEL_NAME = ResourceUtils.getResourcePath("./yolo12n.onnx");
    public MediaSource mediaSource;
    public static int IMG_SIZE = 640;
    Net net = Dnn.readNetFromONNX(MODEL_NAME);

    public OpenCVAlgo(MediaSource mediaSource) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.mediaSource = mediaSource;
        net.setPreferableBackend(Dnn.DNN_BACKEND_OPENCV);
        net.setPreferableTarget(Dnn.DNN_TARGET_CPU);
    }

    @Override
    public void runAlgorithm() {
        final Size NET_SIZE = new Size(IMG_SIZE, IMG_SIZE);
        Mat resizedImage = new Mat();

        // Detect source type
        boolean imageMode = (mediaSource instanceof ImageSource);
        ArrayList<Mat> media = null;
        boolean cameraMode = false;

        if (!imageMode) {
            try {
                media = mediaSource.getFrameArray(); // video file mode (finite frames)
            } catch (UnsupportedOperationException ex) {
                cameraMode = true;                   // live camera mode (infinite stream)
            }
        }

        if (imageMode) {
            Mat image = mediaSource.getFrame();
            if (image == null || image.empty()) {
                System.err.println("ImageSource returned empty frame.");
                return;
            }
            processFrame(image, resizedImage, NET_SIZE);
            HighGui.imshow("Image", resizedImage);
            HighGui.waitKey(0);
        } else if (!cameraMode) {
            for (Mat frame : media) {
                if (!processFrame(frame, resizedImage, NET_SIZE)) break;
            }
        } else {
            while (true) {
                Mat frame = mediaSource.getFrame();
                if (frame == null || frame.empty()) {
                    if (HighGui.waitKey(10) == 'q') break;
                    continue;
                }
                if (!processFrame(frame, resizedImage, NET_SIZE)) break;
            }
        }

        HighGui.destroyAllWindows();
        System.exit(0);
    }


    /**
     * Shared per-frame detection pipeline.
     * Returns false to request exit (user pressed 'q' or ESC); true to continue.
     */
    private boolean processFrame(Mat image, Mat resizedImage, Size netSize) {
        if (image == null || image.empty()) {
            // Show a blank-ish tick so window stays responsive
            if (HighGui.waitKey(1) == 'q') return false;
            return true;
        }

        // 1) Resize once per frame
        resize(image, resizedImage, netSize);

        // 2) Blob from resized
        Mat inputBlob = Dnn.blobFromImage(resizedImage, 1.0 / 255.0, netSize,
                new Scalar(0, 0, 0), true, false);
        net.setInput(inputBlob);

        // 3) Forward
        Mat outputs = net.forward();

        // 4) YOLO12n: [1,84,8400] → [8400,84]
        Mat out = outputs.reshape(1, 84); // 84 x 8400
        Core.transpose(out, out);         // 8400 x 84

        // 5) Collect boxes/scores/classes
        final float CONF_THRESH = 0.50f;
        final float NMS_THRESH = 0.45f;

        List<Rect2d> boxes = new ArrayList<>();
        List<Float> scores = new ArrayList<>();
        List<Integer> classIds = new ArrayList<>();

        for (int i = 0; i < out.rows(); i++) {
            // columns: [cx, cy, w, h, cls0..cls79]
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

        // 6) NMS (safe for empty)
        if (boxes.isEmpty()) {
            drawNoDetectionsOverlay(resizedImage);     // centered red message
            HighGui.imshow("Image", resizedImage);
            int key = HighGui.waitKey(1);
            return !(key == 'q' || key == 27);
        }

        MatOfRect2d nmsBoxes = new MatOfRect2d();
        nmsBoxes.fromList(boxes);
        MatOfFloat nmsScores = new MatOfFloat();
        nmsScores.fromList(scores);
        MatOfInt keep = new MatOfInt();
        Dnn.NMSBoxes(nmsBoxes, nmsScores, CONF_THRESH, NMS_THRESH, keep);

        if (keep.empty()) {
            drawNoDetectionsOverlay(resizedImage);     // centered red message
            HighGui.imshow("Image", resizedImage);
            int key = HighGui.waitKey(1);
            return !(key == 'q' || key == 27);
        }

        // 7) Draw kept boxes
        int[] keptIdx = keep.toArray();
        for (int idx : keptIdx) {
            Rect2d b = boxes.get(idx);
            String name = COCO_CLASSES[classIds.get(idx)];
            Point p1 = new Point(b.x, b.y);
            Point p2 = new Point(b.x + b.width, b.y + b.height);
            rectangle(resizedImage, p1, p2, new Scalar(0, 165, 255), 2);
            putText(resizedImage, name, new Point(b.x, Math.max(0, b.y - 5)),
                    0, 0.7, new Scalar(0, 165, 255), 2); //replaced Imgproc.FONT_HERSHEY_SIMPLEX for fontface
        }

        HighGui.imshow("Image", resizedImage);
        int key = HighGui.waitKey(1);
        return !(key == 'q' || key == 27);
    }

    /**
     * Draws a centered “No objects detected” overlay in bright red, larger font.
     */
    private void drawNoDetectionsOverlay(Mat frame) {
        final String msg = "No objects detected";
        final double fontScale = 1.2; // bigger
        final int thickness = 2;
        final int font = 0; //replaced Imgproc.FONT_HERSHEY_SIMPLEX bc it was not working
        final Scalar color = new Scalar(0, 0, 255); // bright red (BGR)

        int[] baseLine = new int[1];
        Size textSize = getTextSize(msg, font, fontScale, thickness, baseLine);

        int x = (int) Math.round((frame.cols() - textSize.width) / 2.0);
        int y = (int) Math.round((frame.rows() + textSize.height) / 2.0);

        // Optional: a faint rectangle behind text for readability
        Point tl = new Point(x - 10, y - textSize.height - 10);
        Point br = new Point(x + textSize.width + 10, y + baseLine[0] + 10);
        rectangle(frame, tl, br, new Scalar(0, 0, 0), -1); // filled black box

        putText(frame, msg, new Point(x, y), font, fontScale, color, thickness);
    }

    private static final String[] COCO_CLASSES = {
            "person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck", "boat",
            "traffic light", "fire hydrant", "stop sign", "parking meter", "bench", "bird", "cat",
            "dog", "horse", "sheep", "cow", "elephant", "bear", "zebra", "giraffe", "backpack",
            "umbrella", "handbag", "tie", "suitcase", "frisbee", "skis", "snowboard", "sports ball",
            "kite", "baseball bat", "baseball glove", "skateboard", "surfboard", "tennis racket",
            "bottle", "wine glass", "cup", "fork", "knife", "spoon", "bowl", "banana", "apple",
            "sandwich", "orange", "broccoli", "carrot", "hot dog", "pizza", "donut", "cake", "chair",
            "couch", "potted plant", "bed", "dining table", "toilet", "tv", "laptop", "mouse",
            "remote", "keyboard", "cell phone", "microwave", "oven", "toaster", "sink",
            "refrigerator", "book", "clock", "vase", "scissors", "teddy bear", "hair drier", "toothbrush"
    };
}
