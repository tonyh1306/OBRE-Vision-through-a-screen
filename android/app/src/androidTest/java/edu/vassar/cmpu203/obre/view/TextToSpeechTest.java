package edu.vassar.cmpu203.obre.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * An instrumented test to verify the functionality of the Android TextToSpeech engine.
 */
@RunWith(AndroidJUnit4.class)
public class TextToSpeechTest {

    private TextToSpeech tts;
    private CountDownLatch latch;
    private int initStatus = -1; // A default value indicating not initialized

    /**
     * Sets up the test environment. This method initializes the TextToSpeech engine and
     * uses a CountDownLatch to wait for the asynchronous initialization to complete before
     * any tests are run.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Before
    public void setUp() throws InterruptedException {
        latch = new CountDownLatch(1);
        Context context = ApplicationProvider.getApplicationContext();

        // Initialize the TextToSpeech engine and set up a listener to capture the init status.
        tts = new TextToSpeech(context, status -> {
            initStatus = status;
            latch.countDown(); // Signal that initialization is complete.
        });

        // Wait for a maximum of 10 seconds for the TTS engine to initialize.
        boolean initialized = latch.await(10, TimeUnit.SECONDS);
        if (!initialized) {
            fail("TextToSpeech engine did not initialize within the timeout period.");
        }
    }

    /**
     * Tests that the TextToSpeech engine is successfully connected and initialized.
     * <p>
     * The test fails if the initialization status is not {@link TextToSpeech#SUCCESS}.
     */
    @Test
    public void testTextToSpeechEngineIsConnected() {
        // Assert that the initialization was successful.
        assertEquals("TextToSpeech engine initialization failed.", TextToSpeech.SUCCESS, initStatus);
    }

    /**
     * Cleans up resources after each test. This method shuts down the TextToSpeech
     * engine to release system resources.
     */
    @After
    public void tearDown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
