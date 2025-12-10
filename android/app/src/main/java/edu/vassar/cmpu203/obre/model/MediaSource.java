package edu.vassar.cmpu203.obre.model;

import org.opencv.core.Mat;

import java.util.ArrayList;

/**
 * An interface for classes that provide media frames, such as from a video file or a live camera.
 */
public interface MediaSource {
    /**
     * Retrieves a single frame from the media source.
     */
    Mat getFrame();

    /**
     * Retrieves all frames from the media source
     */
    ArrayList<Mat> getFrameArray();
}
