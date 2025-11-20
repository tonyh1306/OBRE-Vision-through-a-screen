package edu.vassar.cmpu203.obre.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

import edu.vassar.cmpu203.obre.R;

<<<<<<< HEAD
/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UploadImageFragment extends Fragment implements UploadImageUI {
=======
public class UploadImageFragment extends Fragment {

    private ImageView imageView;
>>>>>>> refs/remotes/origin/main

    public UploadImageFragment() {
        // Required empty public constructor
    }

<<<<<<< HEAD
=======
    public static UploadImageFragment newInstance() {
        return new UploadImageFragment();
    }

>>>>>>> refs/remotes/origin/main
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri imageUri = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                                if (imageView != null) {
                                    imageView.setImageBitmap(bitmap);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_image, container, false);
        imageView = view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void setListener(Listener listener) {

    }

    @Override
    public void displayDescription() {

    }
}