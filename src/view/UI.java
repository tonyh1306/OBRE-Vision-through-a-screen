package src.view;

import src.controller.Controller;

public interface UI {
    void startVideoStreaming();

    void setListener(Controller ctrl);


    interface Listener {
        void analyze_video();

    }
}
