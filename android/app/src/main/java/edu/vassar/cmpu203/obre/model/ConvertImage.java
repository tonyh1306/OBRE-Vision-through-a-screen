package edu.vassar.cmpu203.obre.model;

import android.graphics.Bitmap;
import com.google.ai.client.generativeai.type.Blob;
import com.google.ai.client.generativeai.type.Part;
import java.io.ByteArrayOutputStream;

/**
 * A utility class to convert images into a format suitable for the Gemini API.
 */
public class ConvertImage {

    /**
     * Converts a Bitmap image into a Gemini-compatible Part object.
     *
     * @param bitmap The input image.
     * @return A Part object containing the image data for the API.
     */
    public static Part fromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

        byte[] bytes = stream.toByteArray();

        Blob blob = new Blob("image/jpeg", bytes);

        return new Part(blob);
    }
}
