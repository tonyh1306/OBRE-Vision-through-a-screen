package model;

import org.opencv.core.Mat;
import java.util.ArrayList;

public interface MediaSource{
    Mat getFrame();
    ArrayList<Mat> getFrameArray();
}
