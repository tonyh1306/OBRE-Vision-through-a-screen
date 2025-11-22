package edu.vassar.cmpu203.obre.model;
import android.content.Context;

import org.junit.Test;
import android.net.Uri;
import androidx.test.platform.app.InstrumentationRegistry;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

public class LLMAlgoTest {

    @Test
    public void testGeminiRealCall() throws InterruptedException {
        // Get actual Android context
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        LLMAlgo llm = new LLMAlgo();

        // Convert file path to Uri
        Uri imageUri = Uri.fromFile(new File("/Users/mayalaidler/CMPU203/team-c/android/app/src/androidTest/assets/bottle.jpg"));

        CountDownLatch latch = new CountDownLatch(1);
        final String[] resultHolder = new String[1];
        final Exception[] errorHolder = new Exception[1];

        llm.sendImageToGemini(context, imageUri, new LLMAlgo.Listener() {
            @Override
            public void onSuccess(String responseText) {
                resultHolder[0] = responseText;
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                errorHolder[0] = e;
                e.printStackTrace();
                latch.countDown();
            }
        });

        // Wait for callback (timeout after 30 seconds)
        boolean completed = latch.await(30, TimeUnit.SECONDS);
        assertTrue("Callback did not fire within 30 seconds", completed);

        // Assert no error occurred
        assertNull("API call returned an error", errorHolder[0]);

        // Assert we got a response
        assertNotNull("Response should not be null", resultHolder[0]);
        assertFalse("Response should not be empty", resultHolder[0].isEmpty());

        // Optional: print response for debugging
        System.out.println("Gemini Response: " + resultHolder[0]);
    }
}

