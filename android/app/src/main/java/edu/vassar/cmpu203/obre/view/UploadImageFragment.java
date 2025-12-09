package edu.vassar.cmpu203.obre.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.databinding.FragmentUploadImageBinding;

/**
 * Fragment that allows users to pick an image from the gallery and send it for AI analysis.
 */
public class UploadImageFragment extends Fragment {

    /**
     * Interface for handling interactions within the UploadImageFragment.
     * The Controller (Activity) implements this to handle navigation and logic.
     */
    public interface Listener {
        void onPickImageRequested();
        void onAnalyzeImageRequested(Bitmap image);
        void onSwitchBackToStream();
    }

    private Bitmap selectedBitmap;
    private Listener listener;
    private FragmentUploadImageBinding binding;
    LinearLayout linearLayout;
    String pendingDetectionText = null;
    List<String> history = new ArrayList<>();
    static final String ARG_RESULT_TEXT = "result_text";

    public UploadImageFragment() {
        // Required empty public constructor
        Bundle bundle = new Bundle();
        bundle.putSerializable("ARG_RESULT_TEXT", (Serializable) history);
        setArguments(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentUploadImageBinding.inflate(inflater, container, false);
        linearLayout = binding.scrollView.findViewById(R.id.linearLayout);
        return this.binding.getRoot();
    }

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

        for (String detection : history) {
            addSingleTextView(detection);
        }
    }

    /**
     * Sets the listener to handle UI events.
     *
     * @param listener The listener implementation.
     */
    public void setListener(Listener listener) {
        this.listener = listener;
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

    /**
     * Hides the loading spinner if the analysis fails.
     */
    public void onAnalysisFailed() {
        if (binding != null) binding.loadingSpinner.setVisibility(View.GONE);
    }

    public void updateDetections(String detection) {
        this.history.add(detection);
        if (linearLayout != null && getContext() != null) {
            addSingleTextView(detection);
        }
    }

    private void addSingleTextView(String text) {
        TextView textView = new TextView(getContext());
        String detection = text + "\n\n";
        textView.setText(detection);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
        linearLayout.addView(textView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        linearLayout = null;
    }
}
