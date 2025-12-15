package edu.vassar.cmpu203.obre.view;

import static org.opencv.android.NativeCameraView.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.databinding.FragmentUploadImageBinding;
import edu.vassar.cmpu203.obre.model.Ledger;

/**
 * Fragment that allows users to pick an image from the gallery and send it for AI analysis.
 */
public class UploadImageFragment extends Fragment implements UI {

    public void updateLedgerDisplay(Ledger ledger) {
        if (linearLayout != null && getContext() != null) {
            linearLayout.removeAllViews();
            for (String description : ledger.getDescriptions()) {
                addSingleTextView(description);
            }
        }
    }

    /**
     * Interface for handling interactions within the UploadImageFragment.
     * The Controller (Activity) implements this to handle navigation and logic.
     */
    public interface Listener {
        /**
         * Called when the user requests to pick an image from the gallery.
         */
        void onPickImageRequested();

        /**
         * Called when the user requests to analyze the selected image.
         *
         * @param image The bitmap to be analyzed.
         */
        void onAnalyzeImageRequested(Bitmap image);

        /**
         * Called when the user requests to switch back to the camera stream view.
         */
        void onSwitchBackToStream();

    }

    private Bitmap selectedBitmap;
    private Listener listener;
    private FragmentUploadImageBinding binding;
    LinearLayout linearLayout;
    List<String> history = new ArrayList<>();

    /**
     * Default constructor. Initializes the fragment and prepares the argument bundle for saving history.
     */
    public UploadImageFragment() {
    }

    /**
     * Inflates the layout for this fragment and initializes view binding.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentUploadImageBinding.inflate(inflater, container, false);
        linearLayout = binding.linearLayout;
        return this.binding.getRoot();
    }

    /**
     * Sets up the view listeners and populates the view with any existing detection history after the view has been created.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.pickImageButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPickImageRequested();
            }
        });

        binding.runAiButton.setOnClickListener(v -> {
            if (selectedBitmap == null) {
                Toast.makeText(getContext(), "Please pick an image first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (listener != null) {
                // Show loading state
                binding.loadingSpinner.setVisibility(View.VISIBLE);
                listener.onAnalyzeImageRequested(selectedBitmap);
                selectedBitmap = null;
            }
        });

        binding.backButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSwitchBackToStream();
            }
        });
    }

    /**
     * Sets the listener to handle UI events.
     *
     * @param listener The listener implementation.
     */
    public <L extends UI.Listener> void setListener(@NonNull final L listener) {
        if (listener instanceof UploadImageFragment.Listener)
            this.listener = (UploadImageFragment.Listener) listener;
    }

    /**
     * Updates the ImageView with the selected Bitmap.
     *
     * @param bitmap The image selected by the user.
     */
    public void updateImagePreview(Bitmap bitmap) {
        this.selectedBitmap = bitmap;
        if (binding != null) {
            binding.previewImage.setImageBitmap(bitmap);
        }
    }


    // Image Picker Launcher
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().getContentResolver(), uri
                        );
                        updateImagePreview(bitmap);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to load image", e);
                    }
                }
            }
    );


    /**
     * Launches the system image picker to allow the user to select a photo.
     */

    public void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    /**
     * Hides the loading spinner if the analysis fails.
     */
    public void onAnalysisFailed() {
        if (binding != null) binding.loadingSpinner.setVisibility(View.GONE);
    }

    /**
     * Adds a new detection result to the history and updates the UI to display it.
     *
     * @param detection The new detection string to add.
     */
    public void updateDetections(String detection) {
        if (linearLayout != null && getContext() != null) {
            addSingleTextView(detection);
        }
    }

    /**
     * Creates and adds a new TextView to the UI for a single detection result.
     *
     * @param text The text to display in the TextView.
     */
    private void addSingleTextView(String text) {
        TextView textView = new TextView(getContext());
        String detection = text + "\n\n";
        textView.setText(detection);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
        linearLayout.addView(textView);
    }

    /**
     * Cleans up resources associated with the view, preventing memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        linearLayout = null;
    }
}
