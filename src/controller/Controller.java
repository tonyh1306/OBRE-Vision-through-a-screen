package controller;
import view.CmdLineUI;

import model.ImageSource;
import model.MediaSource;
import model.OpenCVAlgo;
import org.opencv.core.Core;
import model.TextRecognizer;

public class Controller {

    private CmdLineUI ui;

    /**
     * Constructs a Controller with the given CmdLineUI.
     * @param ui The command-line user interface.
     */
    public Controller(CmdLineUI ui){
        this.ui = ui;
        ui.setListener(this);
    }

    /**
     * Processes an image file for object recognition.
     * @param filename The path to the image file.
     */
    public void processImageFile(String filename) {
        try {
            MediaSource imageSource = new ImageSource(filename);
            System.out.println("Processing image from:"+ filename);
            //Give the open cv algo the image source object
            OpenCVAlgo algo = new OpenCVAlgo(imageSource);
            algo.runAlgorithm().forEach(System.out::println);
            //detects the detections
            //the method name may change when the OpenCVAlgo is changed
            //List<String> detections = algo.detectObjects(imageSource);
            //ui.displayDetections(detections);
        }
        catch (Exception e) {System.out.println("Error occured:"+ e);}
    }

    /**
     * Processes an image file for text recognition (currently is a mock prototype).
     * @param filename The path to the image should be jpg or png.
     */
    public void processTextRecognition(String filename) {
        System.out.println("Scanning for text in this file: " + filename);

        TextRecognizer recognizer = new TextRecognizer();
        String recognizedText = recognizer.recognizeText(filename);

        ui.displayMessage(recognizedText);
    }

    /**
     * Initializes the UI and controller and starts the detection.
     * @param args
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("OpenCV version: " + Core.VERSION);

        CmdLineUI ui = new CmdLineUI();
        Controller ctrl = new Controller(ui);
        ui.startDetecting();
    }
}

