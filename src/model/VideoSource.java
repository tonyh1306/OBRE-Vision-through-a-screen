package model;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;

public class VideoSource implements MediaSource {
    private ArrayList<Mat> frames;
    VideoCapture vd;
    private boolean cameraMode = false;

    public VideoSource(String videoAddress, int camera) {
        frames = new ArrayList<>();
        Mat frame = new Mat();
        try {
            if (camera == 0) {
                cameraMode = true;
                vd = new VideoCapture(0);

                if (!vd.isOpened()) {
                    System.err.println("Camera is not opened!");
                    return;
                }
                return;
            }

            vd = new VideoCapture(ResourceUtils.getResourcePath(videoAddress));
            if (!vd.isOpened()) {
                System.err.println("Video is not readable!");
                return;
            }
            while (vd.read(frame)) {
                if (frame.empty()) {
                    System.out.println("Out of frames. Exiting.");
                    break;
                }
                frames.add(frame.clone());
            }
            vd.release();
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage()
                    + "\nVideo address: " + videoAddress);
        }

    }

    @Override
    public Mat getFrame() {
        if (cameraMode) {
            Mat frame = new Mat();
            if (vd != null && vd.isOpened()) {
                vd.read(frame);
            }
            return frame;
        }
        return frames.get(0);
    }

    public ArrayList<Mat> getFrameArray() {
        if (cameraMode) {
            throw new UnsupportedOperationException("Camera mode cannot preload!");
        }
        return frames;
    }
}
