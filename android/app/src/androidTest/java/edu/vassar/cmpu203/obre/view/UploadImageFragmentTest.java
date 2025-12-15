package edu.vassar.cmpu203.obre.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.vassar.cmpu203.obre.R;
import edu.vassar.cmpu203.obre.controller.MainActivity;

@RunWith(AndroidJUnit4.class)
public class UploadImageFragmentTest {
    // required - launches the appropriate activity prior to each test
    @org.junit.Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO);
    /**
     * Checks that tapping the Upload Image button shows the upload screen
     * and its main UI elements.
     */
    @Test
    public void testSwitchScreen() {
        Espresso.onView(ViewMatchers.withId(R.id.uploadImageButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.backButton)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.pick_image_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.preview_image)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    /**
     * Ensures that pressing the Back button returns to the camera screen.
     */
    @Test
    public void testBackButtonReturnsToStream() {
        onView(withId(R.id.uploadImageButton)).perform(click());
        onView(withId(R.id.backButton)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        onView(withId(R.id.cameraSwitchButton)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView((withId(R.id.backButton))).check(ViewAssertions.doesNotExist());
    }

    /**
     * Verifies that tapping Analyze with no image selected
     * keeps the button visible and doesn't crash.
     */
    @Test
    public void testAnalyzeButtonValidation() {
        onView(withId(R.id.uploadImageButton)).perform(click());
        onView(withId(R.id.run_ai_button)).perform(click());
        onView(withId(R.id.run_ai_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
