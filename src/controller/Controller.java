package controller;
import src.view.CmdLineUI;
import src.view.UI;

import model.ImageSource;
import model.MediaSource;
import model.OpenCVAlgo;
import model.VideoSource;
import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;

public class Controller {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        MediaSource img = new VideoSource("vase-vid.mp4",0);
//        MediaSource img = new ImageSource("vase.jpg");
        OpenCVAlgo cv = new OpenCVAlgo(img);
        cv.runAlgorithm();
        final UI ui = new CmdLineUI();
        final Controller ctrl = new Controller(ui);
        ctrl.ui.setListener(ctrl);
        ctrl.ui.startVideoStreaming();
    }
}

public class Controller implements UI.Listener{
    private final UI ui;

    @Override
    public void analyze_video() {

    }

    public void onProcessImage(){

    }

}
