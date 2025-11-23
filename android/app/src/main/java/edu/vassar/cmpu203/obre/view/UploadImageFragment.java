package edu.vassar.cmpu203.obre.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.vassar.cmpu203.obre.R;

public class UploadImageFragment extends Fragment {

    public interface Listener {
        void onPickImageRequested();
        void onAnalyzeImageRequested(Bitmap image);
        void onSwitchBackToStream();
    }

    private ImageView previewImage;
    private ProgressBar loadingSpinner;
    private Bitmap selectedBitmap;
    private Listener listener;

    public UploadImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previewImage = view.findViewById(R.id.preview_image);
        loadingSpinner = view.findViewById(R.id.loading_spinner);
        Button pickImageButton = view.findViewById(R.id.pick_image_button);
        Button runAIButton = view.findViewById(R.id.run_ai_button);
        Button backButton = view.findViewById(R.id.backButton);

        pickImageButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPickImageRequested();
            }
        });

        runAIButton.setOnClickListener(v -> {
            if (selectedBitmap == null) {
                Toast.makeText(getContext(), "Please pick an image first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (listener != null) {
                // Show loading state
                if (loadingSpinner != null) loadingSpinner.setVisibility(View.VISIBLE);
                listener.onAnalyzeImageRequested(selectedBitmap);
            }
        });

        backButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSwitchBackToStream();
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Called by the Controller (MainActivity) to update the UI with the selected image.
     */
    public void updateImagePreview(Bitmap bitmap) {
        this.selectedBitmap = bitmap;
        if (previewImage != null) {
            previewImage.setImageBitmap(bitmap);
        }
    }

    /**
     * Called by Controller if analysis fails to hide spinner
     */
    public void onAnalysisFailed() {
        if (loadingSpinner != null) loadingSpinner.setVisibility(View.GONE);
    }
}
