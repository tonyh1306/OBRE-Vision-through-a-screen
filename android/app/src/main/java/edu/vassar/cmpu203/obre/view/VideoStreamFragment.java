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

import edu.vassar.cmpu203.obre.databinding.FragmentVideoStreamBinding;
import edu.vassar.cmpu203.obre.model.DetectedObject;

public class VideoStreamFragment extends Fragment implements VideoStreamUI {

    private FragmentVideoStreamBinding binding;
    private Listener listener;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentVideoStreamBinding.inflate(inflater);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.binding.cameraSwitchButton.setOnClickListener((v) -> {
            if (this.listener != null) this.listener.onSwitchCamera();
        });
        this.binding.uploadImageButton.setOnClickListener(v -> {
            if (this.listener != null) this.listener.onUploadImageRequested();
        });

    }


    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public PreviewView getPreviewView() {
        return binding.previewView;
    }

    public void updateDetections(List<DetectedObject> objects) {
        if (binding != null) {
            binding.overlayView.setDetectedObjects(objects);
        }
    }

    public void presentAllowed(String msg) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displayError(String s) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }
}
