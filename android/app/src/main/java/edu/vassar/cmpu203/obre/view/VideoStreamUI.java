package edu.vassar.cmpu203.obre.view;

import android.view.Display;

import edu.vassar.cmpu203.obre.controller.CameraController;

public interface VideoStreamUI {
    interface Listener {
        void onStartStream(VideoStreamUI ui);
        void onStopStream(VideoStreamUI ui);

        void onSwitchCamera();
        void onUploadImageRequested();
    }

    void setListener(final Listener listener);

}
