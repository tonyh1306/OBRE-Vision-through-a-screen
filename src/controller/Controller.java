package controller;
import view.CmdLineUI;
import view.UI;

import model.ImageSource;
import model.MediaSource;
import model.OpenCVAlgo;
import model.VideoSource;
import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;

public class Controller {
    private view.CmdLineUI ui;

    public void setListener() {
        ui.setListener(this);
    }

    public void processImageFile(String filename) {
        try {
            MediaSource imageSource = new ImageSource(filename);
            System.out.println("Prcoessing image from:"+ filename);
            //Give the open cv algo the image source object
            OpenCVAlgo algo = new OpenCVAlgo(imageSource);
            algo.runAlgorithm();
            //detects the detections
            //the method name may change when the OpenCVAlgo is changed
            List<String> detections = algo.detectObjects(imageSource);
            ui.displayDetections(detections);
        }
        catch (Exception e) {System.out.println("Error occured:"+ e);}
    }


    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        MediaSource vd = new VideoSource("vase-vid.mp4",0);
        MediaSource img = new ImageSource("vase.jpg");
        OpenCVAlgo cv = new OpenCVAlgo(img);
        cv.runAlgorithm();
        final UI ui = (UI) new CmdLineUI();
        final Controller ctrl = new Controller();
        ctrl.ui.setListener(ctrl);
        ctrl.ui.startVideoStreaming();
    }
}

