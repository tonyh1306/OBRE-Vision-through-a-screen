package edu.vassar.cmpu203.obre.model;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
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
public class VideoStreamFragmentTest {
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO);

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testPresent() {
        onView(withId(R.id.previewView)).check(matches(isDisplayed()));
        onView(withId(R.id.overlayView)).check(matches(isDisplayed()));
        onView(withId(R.id.cameraSwitchButton)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadImageButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testSwitchCameraButton() {
        Espresso.onView(ViewMatchers.withId(R.id.cameraSwitchButton)).perform(ViewActions.click());
        onView(withId(R.id.previewView)).check(matches(isDisplayed()));
        onView(withId(R.id.overlayView)).check(matches(isDisplayed()));
        onView(withId(R.id.cameraSwitchButton)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadImageButton)).check(matches(isDisplayed()));
    }
}
