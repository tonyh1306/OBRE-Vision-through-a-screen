package edu.vassar.cmpu203.obre.view;

import android.view.Display;

public interface VideoStreamUI {
    interface Listener {
        void onStartStream(VideoStreamUI ui);
        void onStopStream(VideoStreamUI ui);
    }

    void setListener(final Listener listener);
    void displayDetection(String name, int x, int y, int width, int height, VideoStreamUI ui);

}
