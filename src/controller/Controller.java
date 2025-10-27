package controller;

import model.ImageSource;
import model.MediaSource;
import model.OpenCVAlgo;
import org.opencv.core.Core;

public class Controller {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        MediaSource img = new ImageSource("cow.jpg");
        OpenCVAlgo cv = new OpenCVAlgo(img);
        cv.runAlgorithm();
    }
}
