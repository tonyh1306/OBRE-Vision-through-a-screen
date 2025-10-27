package model;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Iterator;

public interface MediaSource{
    public ArrayList<Mat> frames = null;
    public Mat getFrame();
    public ArrayList<Mat> getFrameArray();
}
