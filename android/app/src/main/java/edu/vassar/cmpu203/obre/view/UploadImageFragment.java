package edu.vassar.cmpu203.obre.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.model.LLMAlgo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Fragment allowing users to select an image and send it to Gemini.
 */
public class UploadImageFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 10;

    private ImageView previewImage;
    private TextView resultText;
    private Bitmap selectedBitmap;

    public UploadImageFragment() {
        // Required empty public constructor
    }

    public static UploadImageFragment newInstance() {
        return new UploadImageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        previewImage = view.findViewById(R.id.preview_image);
        resultText  = view.findViewById(R.id.result_text);

        Button pickImageButton = view.findViewById(R.id.pick_image_button);
        Button runAIButton     = view.findViewById(R.id.run_ai_button);

        pickImageButton.setOnClickListener(v -> openImagePicker());

        runAIButton.setOnClickListener(v -> {
            if (selectedBitmap == null) {
                Toast.makeText(getContext(), "Please pick an image first", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                callGeminiAPI(selectedBitmap);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /** Launches the file picker */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /** Handles image selected from device */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    InputStream is = requireActivity().getContentResolver().openInputStream(uri);
                    selectedBitmap = BitmapFactory.decodeStream(is);
                    previewImage.setImageBitmap(selectedBitmap);

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /** Convert bitmap → Base64 for Gemini REST */
    private String bitmapToBase64(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /** Calls the Gemini REST client */
    private void callGeminiAPI(Bitmap bitmap) throws JSONException {
        resultText.setText("Processing…");

        String base64 = bitmapToBase64(bitmap);
        LLMAlgo client = new LLMAlgo();

        client.sendImageToGemini(base64, new LLMAlgo.Listener() {
            @Override
            public void onSuccess(String responseText) {
                requireActivity().runOnUiThread(() ->
                        resultText.setText("Gemini Response:\n\n" + responseText)
                );
            }

            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() ->
                        resultText.setText("Error: " + e.getMessage())
                );
            }
        });
    }
}
