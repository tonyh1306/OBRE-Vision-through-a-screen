package edu.vassar.cmpu203.obre.model;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.Part;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.vassar.cmpu203.obre.BuildConfig;

public class LLMAlgo {

    public interface LLMAlgoListener {
        void onResponse(String text);
        void onError(Throwable error);
    }

    public void sendImageToGemini(
            String prompt,
            Bitmap bitmap,
            Executor executor,
            LLMAlgoListener listener
    ) {

        GenerativeModel model = new GenerativeModel(
                "gemini-1.5-flash",
                BuildConfig.API_KEY
        );

        // Convert bitmap → image Part
        Part imagePart = ConvertImage.fromBitmap(bitmap);

        Content content = new Content.Builder()
                .addText(prompt)
                .addPart(imagePart)
                .build();

        ListenableFuture<GenerateContentResponse> future =
                model.generateContent(content);

        Futures.addCallback(future, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse response) {
                listener.onResponse(response.getText());
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                listener.onError(t);
            }
        }, executor);
    }
}
