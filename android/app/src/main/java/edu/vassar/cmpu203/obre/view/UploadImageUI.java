package edu.vassar.cmpu203.obre.view;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.vassar.cmpu203.obre.R;

public class UploadImageUI {
    public Button buttonPickImage;
    public ImageView previewImage;
    public TextView resultText;

    public UploadImageUI(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_upload_image, null);

        buttonPickImage = view.findViewById(R.id.pick_image_button);
        resultText = view.findViewById(R.id.result_text);
    }

    public UploadImageUI(View view) {
        buttonPickImage = view.findViewById(R.id.pick_image_button);
        resultText = view.findViewById(R.id.result_text);
    }

    public interface Listener {
    }
}
