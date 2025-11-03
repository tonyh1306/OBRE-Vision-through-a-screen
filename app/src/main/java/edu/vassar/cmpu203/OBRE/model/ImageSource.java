package edu.vassar.cmpu203.OBRE.model;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ImageSource implements MediaSource {
    private Mat image;
    private ArrayList<Mat> frames;

    public ImageSource(String imageAddress) throws FileNotFoundException {
        frames = new ArrayList<>();
        try {
            File file = new File(ResourceUtils.getResourcePath(imageAddress));
            if (!file.exists()) {
                throw new FileNotFoundException("Image file not found at: " + ResourceUtils.getResourcePath(imageAddress));
            }
            image = Imgcodecs.imread(ResourceUtils.getResourcePath(imageAddress));
            frames.add(image);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage()
                    + "\nImage address: " + imageAddress);
        }
    }

    public Mat getFrame() {
        return image;
    }

    public ArrayList<Mat> getFrameArray() {
        return frames;
    }

}
