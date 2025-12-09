package edu.vassar.cmpu203.obre.view;

public interface VideoStreamUI {
    interface Listener {
        void onStartStream(VideoStreamUI ui);
        void onStopStream(VideoStreamUI ui);

        void onSwitchCamera();
        void onUploadImageRequested();
    }

    void setListener(final Listener listener);

}
