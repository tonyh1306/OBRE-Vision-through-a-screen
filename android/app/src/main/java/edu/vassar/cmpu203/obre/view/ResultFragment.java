package edu.vassar.cmpu203.obre.view;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.databinding.FragmentResultBinding;

/**
 * A Fragment responsible for displaying AI-generated result text to the user and
 * optionally reading it aloud using Android's {@link TextToSpeech} (TTS) engine.
 */
public class ResultFragment extends Fragment implements ResultUI {

    private static final String ARG_RESULT_TEXT = "result_text";
    private String resultText;
    private Listener listener;
    private FragmentResultBinding binding;

    private TextToSpeech tts;

    public ResultFragment() {
    }

    /**
     * This is a constructer that creates a new bundle and sets arguments
     */

    public static ResultFragment newInstance(String resultText) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESULT_TEXT, resultText);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Reads the result text argument passed to the fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resultText = getArguments().getString(ARG_RESULT_TEXT);
        }
    }

    /**
     * Inflates the UI layout for the fragment.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    /**
     * Sets up the UI, displays the result text, initializes TextToSpeech,
     * and wires the back button listener.
     *
     * @param view               The fragment's root view.
     * @param savedInstanceState Not used.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AudioManager am = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        super.onViewCreated(view, savedInstanceState);

        LinearLayout ll = view.findViewById(R.id.scroll_layout);

        TextView tv = new TextView(getContext());
        tv.setText(resultText != null ? resultText : "No result");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
        tv.setFreezesText(true);
        ll.addView(tv);

        view.findViewById(R.id.back_button).setOnClickListener(v -> {
                    this.listener.onSwitchToUpload(resultText);
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
        );

        tts = new TextToSpeech(requireContext(), status -> {
            if (status != TextToSpeech.SUCCESS) {
                Log.e("TTS", "Initialization failed");
                return;
            }

            if (tts == null) {
                Log.e("TTS", "TTS object is not ready yet.");
                return;
            }

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This language is not supported");
            } else {
                String text = (resultText == null || resultText.isEmpty()) ? "Content not available" : resultText;
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId");
            }
        });
    }

    /**
     * Stops and releases the TextToSpeech engine when the fragment is destroyed.
     */
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.onDestroy();
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
