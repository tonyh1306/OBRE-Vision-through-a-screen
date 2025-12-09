package edu.vassar.cmpu203.obre.view;
/**
 * An interface for the view that displays the camera's video stream.
 * It is implemented by {@link VideoStreamFragment}.
 */
public interface VideoStreamUI {

    /**
     * An interface for a listener to the {@link VideoStreamUI} to be implemented
     * by the controller.
     */
    interface Listener {

        /**
         * Called when the view is ready to start displaying the stream.
         * @param ui the view that is ready
         */
        void onStartStream(VideoStreamUI ui);

        /**
         * Called when the view is being shut down.
         * @param ui the view that is being shut down
         */
        void onStopStream(VideoStreamUI ui);

        /**
         * Called when the user requests to switch the camera.
         */
        void onSwitchCamera();

        /**
         * Called when the user requests to switch to the image upload screen.
         */
        void onUploadImageRequested();
    }

    /**
     * Displays an error message to the user.
     * @param msg the message to be displayed
     */
    void displayError(String msg);

    /**
     * Displays a toast to the user indicating camera permissions were denied.
     */
    void showPermissionError();
}
