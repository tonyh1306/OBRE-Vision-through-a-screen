package model;

import java.io.File;

public class ResourceUtils {
    public static String getResourcePath(String fileName) {
        return System.getProperty("user.dir") + File.separator + "resources" + File.separator + fileName;
    }

}
