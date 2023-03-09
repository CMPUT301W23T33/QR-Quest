package com.example.qrquest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

//import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This Test class is used to test Starting Screen fragment(s) navigation.
 * It tests fragments CreateAccountFragment, StartFragment.
 * Issue/Bug #1: Currently not opening StartActivity, but instead MainActivity
 * Issue/Bug #2: Tests not launching at all
 * @author Abinand Nanthananthan
 */
//@RunWith(AndroidJUnit4.class) // not sure if needed
public class StartActivityTest {
    // Test Method #1
    //Testing it with ActivityScenarioRule: Issue: Displays MainActivity rather than StartActivity.
    //// goes to MainActivity For some reason ("hello world" page), even though specified StartActivity
    @Rule
    public ActivityScenarioRule<StartActivity> activityRule =
            new ActivityScenarioRule<>(StartActivity.class);
    @Test
    /**
     * Tests whether the app switches from StartFragment to StartActivity upon button press
     */
    public void CheckContinueButton() {
        ActivityScenario<StartActivity> activityScenario = activityRule.getScenario();
        onView(withId(R.id.button_next)).perform(click()); // comes up as error as theres no button in MainActivity
        onView(withId(R.id.createAccountFragment)).check(matches(isDisplayed()));
        /*
        ActivityScenario<StartActivity> scenario = ActivityScenario.launchInActivity(StartActivity.class);
        activityScenario.onActivity(activity -> {
            onView(withId(R.id.button_next)).perform(click());
            onView(withId(R.id.createAccountFragment)).check(matches(isDisplayed()));
        });
         */
    }

    // Test Method #2
    // Testing it with FragmentScenario: Issue: Unsure as Testing doesn't launch
    /*
    @Rule
    FragmentScenario<StartFragment> scenario = FragmentScenario.launchInContainer(StartFragment.class);
    @Test
    public void testSwitchToNextFragment() {
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.createAccountFragment)).check(matches(isDisplayed()));
     */

    // Test Method #3
    // Testing it using FragmentManager/SupportFragmentManager?
    //...

    // Methods below will not work until app launches as intended
    @Test
    /**
     * Tests whether the back button on StartActivity
     */
    public void CheckBackButton() {
        //ActivityScenario<StartActivity> scenario = activityRule.getScenario();
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.button_back)).perform(click());
    }
    @Test
    /**
     * Tests if a unique username is generated
     */
    public void CheckUsernameGeneration() {
        //By default, should generate unique username without having to click "Another"
        //ActivityScenario<StartActivity> scenario = activityRule.getScenario();
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.name_text)).check(matches(not(withText(""))));
        //.. unfinished, need to test if names are unique
    }


}
