package edu.vassar.cmpu203.obre.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;

import edu.vassar.cmpu203.obre.R;

public class HomeFragment extends Fragment {

    private TextView resultText;
    private ImageView previewImage;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Initialize UI elements if they exist in fragment_home.xml
        // resultText = view.findViewById(R.id.resultText); // Uncomment if IDs exist
        // previewImage = view.findViewById(R.id.previewImage); // Uncomment if IDs exist

        Button uploadButton = view.findViewById(R.id.button_go_to_upload);
        uploadButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new UploadImageFragment()) // Ensure ID matches MainUI
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            try {
                                if (uri != null && previewImage != null) {
                                    ContentResolver resolver = requireActivity().getContentResolver();
                                    InputStream stream = resolver.openInputStream(uri);
                                    Bitmap selectedBitmap = BitmapFactory.decodeStream(stream);
                                    previewImage.setImageBitmap(selectedBitmap);

                                    if (resultText != null) resultText.setText("");
                                }
                            } catch (IOException e) {
                                if (resultText != null) resultText.setText("Failed to load image.");
                            }
                        }
                    }
            );
}
