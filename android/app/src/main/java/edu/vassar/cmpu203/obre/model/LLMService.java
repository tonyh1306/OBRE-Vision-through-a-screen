package edu.vassar.cmpu203.obre.model;

import android.graphics.Bitmap;

public interface LLMService {
    /**
     * Sends an image to the LLM for analysis.
     * @param bitmap The image to analyze
     * @param listener Callback to handle success or error
     */
    void sendImage(Bitmap bitmap, Listener listener);

    interface Listener {
        void onSuccess(String responseText);
        void onError(Exception e);
    }
}
