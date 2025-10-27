package model;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class VideoSource implements MediaSource {
    private Mat frame;
    private ArrayList<Mat> frames;
    VideoCapture vd;

    public VideoSource(String videoAddress) {
        frames = new ArrayList<>();
        try {
           vd = new VideoCapture(videoAddress);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage()
                    + "\nVideo address: " + videoAddress);
        }
        while (true) {
            vd.read(frame);
            frames.add(frame);
            if (frame.empty()) {
                System.out.println("Out of frames. Exiting.");
                break;
            }
        }
    }

    @Override
    public Mat getFrame() {
        return frames.getFirst();
    }

    public ArrayList<Mat> getFrameArray() {
        return frames;
    }

//    public Iterator<Mat> iterator() {
//        return new Iterator<Mat>() {
//            @Override
//            public boolean hasNext() {
//                return frames != null;
//            }
//
//            @Override
//            public Mat next() {
//                return frames.getFirst();
//            }
//        };
//    }
}
