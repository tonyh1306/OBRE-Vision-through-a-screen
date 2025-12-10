package edu.vassar.cmpu203.obre.view;

/**
 * An interface for the UI that displays results to the user.
 */
public interface ResultUI {

    /**
     * An interface for listeners of UI events.
     */
    public interface Listener {
        /**
         * Called when the user wants to switch to the upload view.
         * @param detection.
         */
        void onSwitchToUpload(String detection);
    }

    /**
     * Sets the listener for UI events.
     * @param listener The listener to set.
     */
    void setListener(Listener listener);
}
