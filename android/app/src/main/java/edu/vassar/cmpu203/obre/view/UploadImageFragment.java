package edu.vassar.cmpu203.obre.view;

import android.app.Activity;
import android.content.Intent;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.model.LLMAlgo;

public class UploadImageFragment extends Fragment {

    private TextView resultText;
    private ImageView previewImage;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final LLMAlgo llm = new LLMAlgo();
    Bitmap selectedBitmap;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK &&
                                result.getData() != null) {

                            Uri uri = result.getData().getData();

                            try {
                                ContentResolver resolver = requireActivity().getContentResolver();
                                InputStream stream = resolver.openInputStream(uri);

                                selectedBitmap = BitmapFactory.decodeStream(stream);

                                previewImage.setImageBitmap(selectedBitmap);
                                resultText.setText("");

                            } catch (IOException e) {
                                resultText.setText("Failed to load image.");
                            }
                        }
                    }
            );

    public UploadImageFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload_image, container, false);

        resultText = view.findViewById(R.id.result_text);
        previewImage = view.findViewById(R.id.preview_image);

        Button pickImageButton = view.findViewById(R.id.pick_image_button);

        pickImageButton.setOnClickListener(v -> openImagePicker());

        //My logic for creating a Analyze image button

        Button runAiButton = view.findViewById(R.id.run_ai_button);
        runAiButton.setOnClickListener(v -> {
            if (selectedBitmap == null) {
                resultText.setText("Please select an image first.");
                return;
            }

            resultText.setText("Analyzing image...");

            llm.sendImageToGemini(
                    "Describe this image for a visually impaired user.",
                    selectedBitmap,
                    executor,
                    new LLMAlgo.LLMAlgoListener() {
                        @Override
                        public void onResponse(String text) {
                            requireActivity().runOnUiThread(() -> {
                                resultText.setText(text);
                            });
                        }

                        @Override
                        public void onError(Throwable error) {
                            requireActivity().runOnUiThread(() -> {
                                resultText.setText("Error: " + error.getMessage());
                            });
                        }
                    }
            );
        });



            return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}
