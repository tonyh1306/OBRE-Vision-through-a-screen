package edu.vassar.cmpu203.obre.model;

import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Callback;
import okhttp3.Response;

import edu.vassar.cmpu203.obre.BuildConfig;

public class LLMAlgo {

    public interface Listener {
        void onSuccess(String responseText);
        void onError(Exception e);
    }

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Sends a Base64-encoded image to Gemini via REST.
     */
    public void sendImageToGemini(String base64Image, Listener listener) throws JSONException {

        JSONObject requestJson = buildRequestJson(base64Image);

        RequestBody body = RequestBody.create(
                requestJson.toString(),
                MediaType.get("application/json")
        );

        // Correct REST endpoint
        String url = "https://generativelanguage.googleapis.com/v1/models/"
                + "gemini-1.5-flash:generateContent?key=" + BuildConfig.API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call,
                                   @NonNull Response response) throws IOException {

                if (!response.isSuccessful()) {
                    listener.onError(new Exception("HTTP " + response.code() + " → " + response.message()));
                    return;
                }

                String responseBody = response.body().string();
                listener.onSuccess(responseBody);
            }
        });
    }

    private JSONObject buildRequestJson(String base64Image) throws JSONException {

        // inlineData object
        JSONObject inlineData = new JSONObject();
        inlineData.put("mimeType", "image/png");
        inlineData.put("data", base64Image);

        JSONObject inlinePart = new JSONObject();
        inlinePart.put("inlineData", inlineData);

        // text part
        JSONObject textPart = new JSONObject();
        textPart.put("text", "Describe what is in this image for a visually impaired person.");

        // parts array
        JSONArray parts = new JSONArray();
        parts.put(inlinePart);
        parts.put(textPart);

        // contents array
        JSONArray contents = new JSONArray();
        JSONObject contentObj = new JSONObject();
        contentObj.put("parts", parts);
        contents.put(contentObj);

        // final request
        JSONObject json = new JSONObject();
        json.put("contents", contents);

        return json;
    }
}
