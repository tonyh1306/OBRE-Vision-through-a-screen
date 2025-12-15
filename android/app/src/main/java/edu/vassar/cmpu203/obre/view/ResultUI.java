package edu.vassar.cmpu203.obre.view;

/**
 * An interface for the UI that displays results to the user.
 */
public interface ResultUI extends UI{

    /**
     * An interface for listeners of UI events.
     */
    public interface Listener {
        /**
         * Called when the user wants to switch to the upload view.
         * @param detection The detection string returned by Gemini API
         */
        void onSwitchBackToUpload(String detection);
    }

}
