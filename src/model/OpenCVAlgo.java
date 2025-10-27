package model;

import com.sun.tools.javac.Main;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OpenCVAlgo implements ImageAlgo {
    static String MODEL_NAME = ResourceUtils.getResourcePath("yolo12n.onnx");
    public MediaSource mediaSource;
    public static int IMG_SIZE = 640;

    public OpenCVAlgo(MediaSource mediaSource) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.mediaSource = mediaSource;
    }

    @Override
    public void runAlgorithm() {
        ArrayList<Mat> media = mediaSource.getFrameArray();
        for (Mat image : media) {
            Mat inputBlob = Dnn.blobFromImage(image, 1.0 / 255.0,
                    new Size(IMG_SIZE, IMG_SIZE),
                    // Supply the spatial size that the Convolutional Neural Network expects.
                    new Scalar(new double[]{0.0, 0.0, 0.0}),
                    true, false);
            Net net = Dnn.readNetFromONNX(MODEL_NAME);
            net.setInput(inputBlob);
            Mat outputs = net.forward();

            Logger logger = Logger.getLogger(Main.class.getName());
            logger.info("Boxes Output: " + outputs.toString());
            System.out.println(System.getProperty("user.dir"));


            Mat mat2D = outputs.reshape(1, (int) outputs.size().width); // The second parameter is the number of rows
            Core.transpose(mat2D, mat2D);

            var box = new Rect2d();
            var className = "";
            for (int i = 0; i < mat2D.rows(); i++) {
                List<Double> scores = new ArrayList<>();
                for (int j = 4; j < COCO_CLASSES.length + 4; j++) {
                    scores.add(mat2D.get(i, j)[0]);
                }

                MaxScore maxScore = ScoreUtils.findMaxScore(scores);
                if (maxScore.maxValue() < 0.6) {
                    continue;
                }

                double width = mat2D.get(i, 2)[0];
                double height = mat2D.get(i, 3)[0];
                double x = mat2D.get(i, 0)[0] - (0.5 * width);
                double y = mat2D.get(i, 1)[0] - (0.5 * height);
                box = new Rect2d(x, y, width, height);
                className = COCO_CLASSES[maxScore.indexOfMax()];
                break;
            }

            Point xy = new Point(box.x, box.y);
            Point wh = new Point(box.x + box.width, box.y + box.height);

            // In order to draw the boxes correctly, we need to resize the image
            Mat resizedImage = new Mat();
            Imgproc.resize(image, resizedImage, new Size(IMG_SIZE, IMG_SIZE), Imgproc.INTER_AREA);

            Imgproc.rectangle(resizedImage, wh, xy, new Scalar(0, 165, 255), 2);

            Point text_point = new Point(box.x, box.y - 5);
            Imgproc.putText(resizedImage, className, text_point, Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 165, 255), 2);
            System.out.printf(className + " and Text Point: " + text_point);

            HighGui.imshow("Image", resizedImage);
            HighGui.waitKey(0);
        }

        HighGui.destroyAllWindows();
        System.exit(0);


    }

    private static final String[] COCO_CLASSES = {
            "person",
            "bicycle",
            "car",
            "motorcycle",
            "airplane",
            "bus",
            "train",
            "truck",
            "boat",
            "traffic light",
            "fire hydrant",
            "stop sign",
            "parking meter",
            "bench",
            "bird",
            "cat",
            "dog",
            "horse",
            "sheep",
            "cow",
            "elephant",
            "bear",
            "zebra",
            "giraffe",
            "backpack",
            "umbrella",
            "handbag",
            "tie",
            "suitcase",
            "frisbee",
            "skis",
            "snowboard",
            "sports ball",
            "kite",
            "baseball bat",
            "baseball glove",
            "skateboard",
            "surfboard",
            "tennis racket",
            "bottle",
            "wine glass",
            "cup",
            "fork",
            "knife",
            "spoon",
            "bowl",
            "banana",
            "apple",
            "sandwich",
            "orange",
            "broccoli",
            "carrot",
            "hot dog",
            "pizza",
            "donut",
            "cake",
            "chair",
            "couch",
            "potted plant",
            "bed",
            "dining table",
            "toilet",
            "tv",
            "laptop",
            "mouse",
            "remote",
            "keyboard",
            "cell phone",
            "microwave",
            "oven",
            "toaster",
            "sink",
            "refrigerator",
            "book",
            "clock",
            "vase",
            "scissors",
            "teddy bear",
            "hair drier",
            "toothbrush"
    };
}
