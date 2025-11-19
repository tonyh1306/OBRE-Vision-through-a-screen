package edu.vassar.cmpu203.obre.model;

import android.graphics.Bitmap;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test for the LLMAlgo class.
 */
@RunWith(AndroidJUnit4.class)
public class LLMAlgoTest {

    /**
     * Tests the getResponse method to ensure it retrieves a non-empty response from the API.
     * This is an integration test that makes a real network call.
     */
    @Test
    public void testGetResponse() throws InterruptedException {
        // 1. Latch to wait for the async response
        CountDownLatch latch = new CountDownLatch(1);
        final String[] responseHolder = new String[1];
        final Throwable[] errorHolder = new Throwable[1];

        // 2. Create a dummy image and the listener
        Bitmap testImage = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        LLMAlgo.LLMAlgoListener listener = new LLMAlgo.LLMAlgoListener() {
            @Override
            public void onResponse(String response) {
                responseHolder[0] = response;
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {
                errorHolder[0] = throwable;
                latch.countDown();
            }
        };

        // 3. Call the method to be tested
        LLMAlgo llmAlgo = new LLMAlgo();
        llmAlgo.getResponse("What is in this picture?", testImage, listener, Executors.newSingleThreadExecutor());

        // 4. Wait for the response and assert the results
        latch.await(20, TimeUnit.SECONDS);

        assertNull("The API returned an error: " + (errorHolder[0] != null ? errorHolder[0].getMessage() : ""), errorHolder[0]);
        assertNotNull("The API response was null.", responseHolder[0]);
        assertFalse("The API response was empty.", responseHolder[0].isEmpty());
    }
}
