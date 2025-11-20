package edu.vassar.cmpu203.obre.view;

public interface UploadImageUI {
    interface Listener {

        /**
         * called when uploading an image to the app
         */
        void onUploadImage();

        void onDoneUploadingImage();

        void onExit();

    }

    void setListener(final Listener listener);

    void displayDescription();
}
