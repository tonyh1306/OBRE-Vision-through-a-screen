package edu.vassar.cmpu203.obre.model;
import org.opencv.core.Mat;

import java.util.List;

public interface MediaAlgo {
    public List<DetectedObject> runOnFrame(Mat frame);

}
