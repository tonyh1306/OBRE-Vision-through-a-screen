package edu.vassar.cmpu203.obre.model;
import org.json.JSONException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LLMAlgoTest {

    @Test
    public void testGeminiRealCall() throws InterruptedException, JSONException {
        LLMAlgo client = new LLMAlgo();

        // minimal valid 1×1 PNG pixel
        String tinyBase64Image =
                "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAuMB9oNwH5QAAAAASUVORK5CYII=";

        CountDownLatch latch = new CountDownLatch(1);
        final String[] resultHolder = new String[1];
        final Exception[] errorHolder = new Exception[1];

        client.sendImageToGemini(tinyBase64Image, new LLMAlgo.Listener() {
            @Override
            public void onSuccess(String response) {
                resultHolder[0] = response;
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                errorHolder[0] = e;
                latch.countDown();
            }
        });

        boolean completed = latch.await(15, TimeUnit.SECONDS);
        assertTrue("Callback did not fire", completed);

        if (errorHolder[0] != null) {
            errorHolder[0].printStackTrace();
            fail("API call failed: " + errorHolder[0].getMessage());
        }

        assertNotNull("Response should not be null", resultHolder[0]);
        assertFalse("Response should not be empty", resultHolder[0].isEmpty());

        System.out.println("Gemini Response: " + resultHolder[0]);
    }
}
