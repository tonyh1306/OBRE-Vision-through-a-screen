package controller;
import view.CmdLineUI;
import view.UI;

import model.ImageSource;
import model.MediaSource;
import model.OpenCVAlgo;
import model.VideoSource;
import org.opencv.core.Core;
import model.TextRecognizer;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;

public class Controller {

    private CmdLineUI ui;

    public Controller(CmdLineUI ui){
        ui.setListener(this);
        this.ui = ui;
    }

    public void processImageFile(String filename) {
        try {
            MediaSource imageSource = new ImageSource(filename);
            System.out.println("Processing image from:"+ filename);
            //Give the open cv algo the image source object
            OpenCVAlgo algo = new OpenCVAlgo(imageSource);
            //algo.runAlgorithm();
            //detects the detections
            //the method name may change when the OpenCVAlgo is changed
            //List<String> detections = algo.detectObjects(imageSource);
            //ui.displayDetections(detections);
        }
        catch (Exception e) {System.out.println("Error occured:"+ e);}
    }

    public void processTextRecognition(String filename) {
        System.out.println("Scanning for text in this file: " + filename);

        // simulates OCR
        TextRecognizer recognizer = new TextRecognizer();
        String recognizedText = recognizer.recognizeText(filename);

        ui.displayMessage(recognizedText);
    }


    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("OpenCV version: " + Core.VERSION);

        //MediaSource vd = new VideoSource("vase-vid.mp4",0);
        MediaSource img = new ImageSource("vase.jpg");
        //OpenCVAlgo cv = new OpenCVAlgo(img);
        //cv.runAlgorithm();

        CmdLineUI ui = new CmdLineUI();
        Controller ctrl = new Controller(ui);
        ui.startDetecting();
    }
}

