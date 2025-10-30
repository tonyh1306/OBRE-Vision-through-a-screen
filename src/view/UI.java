package view;

import java.util.List;

public interface UI {
    void startVideoStreaming();

    void setListener();

    void startDetecting();

    void displayMessage(String message);

    interface Listener {
        void analyze_video();

    }

    public void displayDetections(List<String> detections);
}
