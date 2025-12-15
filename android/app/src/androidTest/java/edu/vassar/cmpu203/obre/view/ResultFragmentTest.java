package edu.vassar.cmpu203.obre.view;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

import edu.vassar.cmpu203.obre.controller.MainActivity;

public class ResultFragmentTest {

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
    );

    /**
     * Tests the navigation from UploadFragment to ResultFragment.
     * We simulate the callback that happens after an image is analyzed.
     */
    @Test
    public void testTransitionToResultFragment() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {

            scenario.onActivity(MainActivity::onUploadImageRequested);

            InstrumentationRegistry.getInstrumentation().waitForIdleSync();

            scenario.onActivity(activity -> {
                ResultFragment resultFragment =
                        ResultFragment.newInstance("This is a test description.");

                MainUI mainUI = new MainUI(activity, activity);

                mainUI.displayFragment(resultFragment);
            });

            Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withText("This is a test description."))
                    .check(androidx.test.espresso.assertion.ViewAssertions.
                            matches(androidx.test.espresso.matcher.ViewMatchers.isDisplayed()));
        }
    }

}
