package model;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ImageSource implements MediaSource {
    private Mat image;
    private ArrayList<Mat> frames;

    public ImageSource(String imageAddress) {
        frames = new ArrayList<>();
        try {
            image = Imgcodecs.imread(ResourceUtils.getResourcePath(imageAddress));
            if (image.empty()) {
                throw new FileNotFoundException(ResourceUtils.getResourcePath(imageAddress));
            }
            frames.add(image);
        } catch (FileNotFoundException e) {
            System.out.println(
                    "Image file not found at: " + ResourceUtils.getResourcePath(imageAddress)
            );;
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
