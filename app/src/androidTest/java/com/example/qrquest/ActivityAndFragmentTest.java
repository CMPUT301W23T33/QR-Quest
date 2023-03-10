package com.example.qrquest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotEquals;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

/**
 * This Test class is used to test Starting Screen fragment(s) navigation.
 * It tests fragments CreateAccountFragment, StartFragment.
 * @author Abinand Nanthananthan
 * @author Dang Viet Anh Dinh
 */
@RunWith(AndroidJUnit4.class)
public class ActivityAndFragmentTest {

    public Context context;
    public SharedPreferences sp;
    public String username;

    // Enforce initial activity
    @Rule
    public ActivityScenarioRule<StartActivity> activityScenarioRule = new ActivityScenarioRule<>(StartActivity.class);

    // Provide application access to location and camera
    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA);

    // Get username and context for test use cases if needed
    @Before
    public void useAppContext() {

        // Get context
        context = getInstrumentation().getTargetContext();

        // Get shared preferences
        sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);

        // Get username
        username = sp.getString("username", "");
    }

    // Test start activity
    @Test
    public void testStartActivity() {

        // First time using this application
        if (username.equals("")) {
            testOnNavigationStartActivity();
        }

        // Having gone through the main screen at least once
        else {
            onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        }
    }

    // Test navigation to Main Activity
    @Test
    public void testOnNavigationMainActivity(){

        // First time using this application
        if (Objects.equals(username, "")){
            testOnNavigationStartActivity();
        }

        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
    }

    // Test fragments on Main Activity
    @Test
    public void testMainActivity(){

        // First time using this application
        if (Objects.equals(username, "")){
            testOnNavigationStartActivity();
        }

        // Main Screen
        onView(withId(R.id.main_fragment)).check(matches(isDisplayed()));

        // Main Screen -> Profile Screen
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.profile_screen)).check(matches(isDisplayed()));

        // Profile Screen -> Edit Profile
        onView(withId(R.id.profile_screen_name)).check(matches(withText(username)));
        onView(withId(R.id.profile_screen_button_edit)).perform(click());
        onView(withId(R.id.edit_profile)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_profile_name)).check(matches(withText(username)));

        // Edit Profile -> Profile Screen
        onView(withId(R.id.edit_profile_button_back)).perform(click());
        onView(withId(R.id.profile_screen)).check(matches(isDisplayed()));

        // Profile Screen -> Main Screen
        onView(withId(R.id.profile_screen_button_back)).perform(click());
    }

    // Test navigation on Camera Activity
    @Test
    public void testOnNavigationCameraActivity(){

        // First time using this application
        if (Objects.equals(username, "")){
            testOnNavigationStartActivity();
        }

        // Main Screen -> Camera Screen
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.button_camera_1)).perform(click());
        onView(withId(R.id.camera_activity)).check(matches(isDisplayed()));

        // Camera Screen -> Main Screen
        onView(withId(R.id.camera_button_back)).perform(click());
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
    }

    // Test navigation on Start Activity
    public void testOnNavigationStartActivity(){

        // Start Screen
        onView(withId(R.id.start_fragment)).check(matches(isDisplayed()));

        // Start Screen -> Create Account Screen
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.create_account_fragment)).check(matches(isDisplayed()));

        // Create Account Screen -> Start Screen
        onView(withId(R.id.button_back)).perform(click());
        onView(withId(R.id.start_fragment)).check(matches(isDisplayed()));

        // Start Screen -> Create Account Screen
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.button_elevated_secondary)).perform(click());

        // Create Account Screen -> Main Screen
        onView(withId(R.id.button_elevated_primary)).perform(click());
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        username = sp.getString("username", "");
        assertNotEquals(username, "");
    }

}
