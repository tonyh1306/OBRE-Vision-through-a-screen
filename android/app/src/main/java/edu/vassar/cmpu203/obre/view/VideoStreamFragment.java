package edu.vassar.cmpu203.obre.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;

import java.util.List;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.databinding.FragmentVideoStreamBinding;
import edu.vassar.cmpu203.obre.model.DetectedObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoStreamFragment extends Fragment implements VideoStreamUI {

    private FragmentVideoStreamBinding binding;
    private Listener listener;

    /**
     * Default constructor for the fragment.
     */
    public VideoStreamFragment() {

    }

    /**
     * Displays a short toast message on the main thread.
     *
     * @param msg The message to display.
     */
    public void presentAllowed(String msg) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentVideoStreamBinding.inflate(inflater);
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

        this.binding.cameraSwitchButton.setOnClickListener(v -> listener.onSwitchCamera());
        this.binding.uploadImageButton.setOnClickListener(v -> listener.onUploadImageRequested());
    }

    @Override
    public void onStop() {
        super.onStop();
        listener.onStopStream(this);
    }

    /**
     * Sets the listener up
     * @param listener The listener to set.
     */
    public <L extends UI.Listener> void setListener(@NonNull final L listener) {
        if (listener instanceof VideoStreamFragment.Listener)
            this.listener = (VideoStreamFragment.Listener) listener;
    }

    /**
     * Displays an error message to the user
     * @param msg The error message to display.
     */
    @Override
    public void displayError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
    /**
     * Updates the overlay view with a list of detected objects.
     *
     * @param objects The list of objects detected by the model.
     */
    public void updateDetections(List<DetectedObject> objects) {
        if (binding != null) {
            binding.overlayView.setDetectedObjects(objects);
        }
    }

    /**
     * Updates the text view with recognized text.
     *
     * @param text The recognized text to display.
     */
    public void updateTextDetections(String text) {
        if (binding != null) {
            binding.text.setText(text);
        }
    }

    /**
     * returns the PreviewView used by CameraX.
     *
     * @return The PreviewView, or null if the binding is not initialized.
     */
    public PreviewView getPreviewView() {
        if (binding == null) {
            return null;
        }
        return binding.previewView;
    }

    /**
     * Shows a toast message indicating that camera permissions are required.
     */
    @Override
    public void showPermissionError() {
        Toast.makeText(getContext(), "Camera permissions are required for this feature.", Toast.LENGTH_LONG).show();
    }
}
