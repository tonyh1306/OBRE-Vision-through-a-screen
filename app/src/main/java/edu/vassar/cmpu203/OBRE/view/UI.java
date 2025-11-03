package edu.vassar.cmpu203.OBRE.view;

import java.util.List;
/**
 * Command-line interface for the Object Recognition App.It handles
 * user input and displays output for both object and text recognition in the command line.
 * It is implemented by the CmdLineUI
 */
public interface UI {

    /**
     * Starts streaming video
     */
    void startVideoStreaming();
    /**
     * sets the listener
     */
    void setListener(final Listener listener);

    /**
     * starts the main loop of the program. Begins the prompt to the user
     */
    void startDetecting();

    void displayMessage(String message);

    /**
     * function for when the user requests to analyze video.
     */
    interface Listener {
        void onUploadImage(String fileName);

    }

    /**
     * Displays detected objects in the UI.
     * @param detections Which is a list of detected object names.
     */
    public void displayDetections(List<String> detections);
}
