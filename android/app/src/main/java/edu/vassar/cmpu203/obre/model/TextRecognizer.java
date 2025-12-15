package edu.vassar.cmpu203.obre.model;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.mlkit.vision.common.InputImage;

/**
 * A class that uses ML Kit to recognize text in an image.
 * It implements the ImageAnalysis.Analyzer interface to be used with CameraX and TextRecignition service so that it can be switched.
 */
public class TextRecognizer implements ImageAnalysis.Analyzer, TextRecognitionService {
    /**
     * Analyzes an image to extract text.
     * This method is called by CameraX for each frame.
     * @param imageProxy The image to analyze.
     */
    @OptIn(markerClass = ExperimentalGetImage.class)
    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
        }
    }
}
