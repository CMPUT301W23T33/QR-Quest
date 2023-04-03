package com.example.qrquest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotEquals;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
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

    /**
     * This test is for the navigation of the main camera activity and those that follow (FOR THIS TO WORK SCANNING OF A QR CODE IS REQUIRED BY THE OPERATOR) therefore it is commented out when running the main tests
     */
//    @Test
//    public void testOnNavigationQRDisplayActivity() throws InterruptedException {
//
//        // First time using this application
//        if (Objects.equals(username, "")){
//            testOnNavigationStartActivity();
//        }
//
//        // Main Screen -> Camera Screen
//        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_camera_1)).perform(click());
//        onView(withId(R.id.camera_activity)).check(matches(isDisplayed()));
//
//        // Camera Screen -> QR Detected Screen (need to scan qr code manually in the 2 seconds given)
//        Thread.sleep(2000);
//
//        // QR Detected Screen -> Image and Location Prompt
//        onView(withId(R.id.qr_detected)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_next)).perform(click());
//
//        // Image and Location Prompt -> QR Edit Screen
//        onView(withId(R.id.fragment_prompt)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_sorry)).perform(click());
//        Thread.sleep(5000);
//        onView(withId(R.id.button_sorry)).perform(click());
//
//        // QR Edit Screen -> QR Comments Screen
//        onView(withId(R.id.edit_qr)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_check)).perform(click());
//
//        // QR Comments Screen -> QR Comments
//        onView(withId(R.id.qr_display_fragment)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_open)).perform(click());
//
//        // QR Comments -> QR Comments Add
//        onView(withId(R.id.bottom_sheet)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_add)).perform(click());
//
//        // QR Comments Add -> QR Comments
//        onView(withId(R.id.dialog_layout)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_check)).perform(click());
//        onView(withId(R.id.bottom_sheet)).check(matches(isDisplayed()));
//
//    }
    
    // tests the leaderboard activity navigation
    @Test
    public void testOnNavigationLeaderboardActivity() throws InterruptedException {

        // First time using this application
        if (Objects.equals(username, "")){
            testOnNavigationStartActivity();
        }

        // Main Screen -> Leaderboard
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.layout)).check(matches(isDisplayed()));
        onView(withId(R.id.button_leaderboard)).perform(click());

        // Leaderboard navigation
        ViewInteraction screen = onView(withId(R.id.leaderboard_fragment));
        screen.check(matches(isDisplayed()));
        Thread.sleep(500);
        screen.perform(swipeLeft());
        Thread.sleep(500);
        screen.perform(swipeLeft());
        Thread.sleep(500);
        screen.perform(swipeLeft());
        Thread.sleep(500);
        screen.perform(swipeRight());
        Thread.sleep(500);
        screen.perform(swipeRight());
        Thread.sleep(500);
        screen.perform(swipeRight());

        // Leaderboard -> Main Screen
        onView(withId(R.id.button_back)).perform(click());
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));

    }

    // tests the navigation of the search activity
    @Test
    public void testOnNavigationSearchActivity(){

        // First time using this application
        if (Objects.equals(username, "")){
            testOnNavigationStartActivity();
        }

        // Main Screen -> Search Screen
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.layout)).check(matches(isDisplayed()));
        onView(withId(R.id.button_search)).perform(click());

        // clicks the search bar
        onView(withId(R.id.search_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.search_screen_text)).perform(click());

        // Search Screen -> Main Screen
        onView(withId(R.id.search_screen_button_back)).perform(click());
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));


    }

    // Test navigation on Start Activity
    private void testOnNavigationStartActivity(){

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
