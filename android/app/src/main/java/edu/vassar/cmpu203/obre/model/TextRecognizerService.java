package edu.vassar.cmpu203.obre.model;

import androidx.camera.core.ImageProxy;

/**
 * An interface for a service that performs text recognition on images.
 * Added for flexibility in changing out the text recognition service.
 */
public interface TextRecognitionService {

        /**
         * Analyze an image frame for text.
         * @param imageProxy The frame to analyze
         * @return detected text as a String
         */
        String analyze(ImageProxy imageProxy);
}
