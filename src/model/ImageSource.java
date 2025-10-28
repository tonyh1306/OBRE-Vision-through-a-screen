package model;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

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
            throw new RuntimeException(e);
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

//    public Iterator<Mat> iterator() {
//        return new Iterator<Mat>() {
//            @Override
//            public boolean hasNext() {
//                return image != null;
//            }
//
//            @Override
//            public Mat next() {
//                return image;
//            }
//        };
//    }

}
