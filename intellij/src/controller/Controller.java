package controller;
import view.CmdLineUI;

import model.ImageSource;
import model.MediaSource;
import model.OpenCVAlgo;
import org.opencv.core.Core;
import model.TextRecognizer;
import view.UI;

public class Controller implements UI.Listener {

    private UI ui;


    enum Options {
        UPLOAD_IMAGE("upload_image"),
        KEEP_VIDEO_STREAMING("keep_video_streaming"),
        SCAN_FOR_TEXT("scan_for_text"),
        EXIT("exit");

        String option;
        Options(String option) {
            this.option = option;
        }

        String getOption() {
            return option;
        }
    }

    Options option = Options.KEEP_VIDEO_STREAMING;

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
    public void onUploadImage(String filename) {
        this.option = Options.UPLOAD_IMAGE;
        try {
            MediaSource imageSource = new ImageSource(filename);
            OpenCVAlgo algo = new OpenCVAlgo(imageSource);
            ui.displayDetections(algo.runAlgorithm());
        }
        catch (Exception e) {System.out.println("Error occured: "+ e +"\n");}
        this.option = Options.KEEP_VIDEO_STREAMING;
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
        ctrl.ui.setListener(ctrl);
        ctrl.ui.startDetecting();
    }
}

