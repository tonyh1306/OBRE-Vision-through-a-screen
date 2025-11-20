package edu.vassar.cmpu203.obre.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.vassar.cmpu203.obre.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VideoStreamFragment extends Fragment implements VideoStreamUI {


    public VideoStreamFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_stream, container, false);
    }

    @Override
    public void setListener(Listener listener) {

    }

    @Override
    public void displayDetection(String name, int x, int y, int width, int height, VideoStreamUI ui) {

    }
}