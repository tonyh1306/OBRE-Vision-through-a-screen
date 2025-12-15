package edu.vassar.cmpu203.obre.model;
import org.opencv.core.Mat;

import java.util.List;

public interface MediaAlgo {

    /**
     * run the media algo on a frame.
     */
    public List<DetectedObject> runOnFrame(Mat frame);

}
