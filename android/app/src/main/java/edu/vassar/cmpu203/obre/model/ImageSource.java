package edu.vassar.cmpu203.obre.model;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * A media source that provides frames from a single image file.
 */
public class ImageSource implements MediaSource {
    private Mat image;
    private ArrayList<Mat> frames;

    /**
     * Constructs an ImageSource by loading an image from the given file path.
     * @param imageAddress The path to the image file.
     */
    public ImageSource(String imageAddress) {
        frames = new ArrayList<>();
        try {
            File file = new File(ResourceUtils.getResourcePath(imageAddress));
            if (!file.exists()) {
                throw new FileNotFoundException("Image file not found at: " + ResourceUtils.getResourcePath(imageAddress));
            }
            image = Imgcodecs.imread(ResourceUtils.getResourcePath(imageAddress));
            frames.add(image);
        } catch (RuntimeException | FileNotFoundException e) {
            System.out.println(e.getMessage()
                    + "\nImage address: " + imageAddress);
        }
    }

    /**
     * Returns the image as a single frame.
     * @return The Mat object representing the image.
     */
    public Mat getFrame() {
        return image;
    }

    /**
     * Returns a list containing the single image frame.
     * @return An ArrayList containing the Mat object of the image.
     */
    public ArrayList<Mat> getFrameArray() {
        return frames;
    }

}
