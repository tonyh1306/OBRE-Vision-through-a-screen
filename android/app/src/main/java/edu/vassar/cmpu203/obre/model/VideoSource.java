package edu.vassar.cmpu203.obre.model;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;

/**
 * Represents a source of media, which can be a pre-loaded video or a live camera feed.
 * This class handles the initialization of either a VideoCapture object for camera input
 * or pre-loads all frames from a video file.
 */
public class VideoSource implements MediaSource {
    private ArrayList<Mat> frames;
    VideoCapture vd;
    private boolean cameraMode = false;

    /**
     * Constructs a VideoSource. If the camera parameter is set to 0, it initializes a live camera feed.
     * Otherwise, it attempts to load a video from the specified file path.
     *
     * @param videoAddress The file path of the video, or an empty string if using camera mode.
     * @param camera The camera index to use. Set to 0 to enable camera mode.
     */
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

    /**
     * Retrieves a single frame. In camera mode, it captures a new frame from the camera.
     * In video file mode, it returns the first frame of the pre-loaded sequence.
     *
     * @return A Mat object representing the current frame.
     */
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

    /**
     * Retrieves all pre-loaded frames from a video file.
     *
     * @return An ArrayList of Mat objects, each representing a frame from the video.
     * @throws UnsupportedOperationException if the source is in camera mode, as pre-loading is not applicable.
     */
    public ArrayList<Mat> getFrameArray() {
        if (cameraMode) {
            throw new UnsupportedOperationException("Camera mode cannot preload!");
        }
        return frames;
    }
}
