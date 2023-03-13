package com.example.qrquest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import android.app.Activity;

import androidx.navigation.Navigation;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QR_navigation {

    @Rule
    public ActivityScenarioRule<CameraActivity> activityRule =
            new ActivityScenarioRule<>(CameraActivity.class);

    /**
     * Tests navigation between the layouts of the qr scanner, says no to each prompt
     * @IMPORTANT: You have to either postition the virtual camera to face a qr code or have one in front of your real camera, the code will wait 2 seconds before checking that a qr has been scanned
     */
    @Test
    public void LayoutNav() throws Exception{
        ActivityScenario<CameraActivity> activityScenario = activityRule.getScenario();
        Thread.sleep(2000);
        onView(withId(R.id.qr_detected)).check(matches(isDisplayed()));
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.prompt)).check(matches(isDisplayed()));
        onView(withId(R.id.button_sorry)).perform(click());
        onView(withId(R.id.prompt)).check(matches(isDisplayed()));
        onView(withId(R.id.button_sorry)).perform(click());
        onView(withId(R.id.edit_qr)).check(matches(isDisplayed()));
    }

    /**
     * same as above but adds picture and location
     */
    @Test
    public void LayoutNavPicture() throws Exception{
        ActivityScenario<CameraActivity> activityScenario = activityRule.getScenario();
        Thread.sleep(2000);
        onView(withId(R.id.qr_detected)).check(matches(isDisplayed()));
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.prompt)).check(matches(isDisplayed()));
        onView(withId(R.id.button_sorry)).perform(click());
        onView(withId(R.id.camera)).check(matches(isDisplayed()));
        onView(withId(R.id.camera_button_capture_image)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.prompt)).check(matches(isDisplayed()));
        onView(withId(R.id.button_sure)).perform(click());
        onView(withId(R.id.edit_qr)).check(matches(isDisplayed()));
    }
}
