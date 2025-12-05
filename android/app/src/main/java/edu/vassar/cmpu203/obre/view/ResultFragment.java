package edu.vassar.cmpu203.obre.view;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import java.util.Locale;

import edu.vassar.cmpu203.obre.R;
/**
 * A Fragment responsible for displaying AI-generated result text to the user and
 * optionally reading it aloud using Android's {@link TextToSpeech} (TTS) engine.
 */
public class ResultFragment extends Fragment {

    private static final String ARG_RESULT_TEXT = "result_text";
    private String resultText;

    private TextToSpeech tts;

    public ResultFragment() {
    }

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
     * @param view The fragment's root view.
     * @param savedInstanceState Not used.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AudioManager am = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        super.onViewCreated(view, savedInstanceState);

        TextView resultTextView = view.findViewById(R.id.result_text_view);
        resultTextView.setText(resultText != null ? resultText : "No result");

        view.findViewById(R.id.back_button).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

            tts = new TextToSpeech(requireContext(), status -> {
                Handler h = new Handler(Looper.getMainLooper());
                h.postDelayed(() -> {
                    if (tts == null) return;
                    Log.e("TTS", "TTS initialization is stuck");
                }, 5000);

                if (status != TextToSpeech.SUCCESS) {
                    Log.e("TTS", "Initialization failed");
                    return;
                }

                int lang = tts.setLanguage(Locale.US);
                if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "This language is not currently supported");
                    return;
                }

                String text = (resultText == null || resultText.isEmpty())
                        ? "Content not available"
                        : resultText;

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId");
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
}
