package edu.vassar.cmpu203.obre.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.model.LLMAlgo;
import java.io.InputStream;

public class UploadImageFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 10;

    private ImageView previewImage;
    private TextView resultText;
    private Bitmap selectedBitmap;

    public UploadImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Moved FragmentManager initialization here
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        LLMAlgo llm = new LLMAlgo();
//        //hide the welcome to obre after clicking upload image fragment
//        if (fragment != null) {
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.hide(fragment);
//            transaction.commit();
//        }

        previewImage = view.findViewById(R.id.preview_image);
        resultText = view.findViewById(R.id.result_text);

        Button pickImageButton = view.findViewById(R.id.pick_image_button);
        Button runAIButton = view.findViewById(R.id.run_ai_button);

        pickImageButton.setOnClickListener(v -> openImagePicker());

        runAIButton.setOnClickListener(v -> {
            if (selectedBitmap == null) {
                Toast.makeText(getContext(), "Please pick an image first", Toast.LENGTH_SHORT).show();
                return;
            }
            llm.sendImageToGemini(selectedBitmap, new LLMAlgo.Listener() {
                @Override
                public void onSuccess(String responseText) {
                    // Switch to ResultFragment
                    requireActivity().runOnUiThread(() -> {
                        // Replace the entire UploadImageFragment with ResultFragment
                        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, new ResultFragment(responseText));
                        ft.addToBackStack(null);
                        ft.commit();
                    });
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });
    }

    /**
     * Launches the file picker
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Handles image selected from device
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
                    previewImage.setImageBitmap(selectedBitmap);

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
