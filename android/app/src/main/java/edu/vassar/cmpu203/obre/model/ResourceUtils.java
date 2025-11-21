package edu.vassar.cmpu203.obre.model;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceUtils {
    public static String getResourcePath(String fileName) {
        return System.getProperty("user.dir") + File.separator + "resources" + File.separator + fileName;
    }

    public static String copyResourceToFile(Context context, int resId, String fileName) {
        File outFile = new File(context.getFilesDir(), fileName);
        if (!outFile.exists()) {
            try (InputStream is = context.getResources().openRawResource(resId);
                 OutputStream os = new FileOutputStream(outFile)) {

                byte[] buffer = new byte[4096];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outFile.getAbsolutePath();
    }
}
