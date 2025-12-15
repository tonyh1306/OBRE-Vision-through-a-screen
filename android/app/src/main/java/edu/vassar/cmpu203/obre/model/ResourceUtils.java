package edu.vassar.cmpu203.obre.model;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for handling resource files.
 */
public class ResourceUtils {
    /**
     * Constructs a path to a resource file within the project's resources directory.
     *
     * @param fileName The name of the resource file.
     */
    public static String getResourcePath(String fileName) {
        return System.getProperty("user.dir") + File.separator + "resources" + File.separator + fileName;
    }

    /**
     * Copies a raw resource from the application's resources to a file in the app's internal storage.
     *
     * @param context The application context.
     * @param resId The resource ID of the raw resource.
     * @param fileName The desired name for the output file.
     */
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
