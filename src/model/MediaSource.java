package model;

import org.opencv.core.Mat;
import java.util.ArrayList;
import java.util.Iterator;

public interface MediaSource{
    Mat getFrame();
    ArrayList<Mat> getFrameArray();
}
