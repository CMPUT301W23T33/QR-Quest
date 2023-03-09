package com.example.qrquest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

//import androidx.fragment.app.testing.FragmentScenario;
//import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This Test class is used to test Starting Screen fragment(s) navigation.
 * It tests fragments CreateAccountFragment, StartFragment.
 * Issue/Bug #1: Currently not opening StartActivity, but instead MainActivity
 * @author Abinand Nanthananthan
 */
@RunWith(AndroidJUnit4.class) // not sure if needed, needed to run the FragmentScenario
public class StartActivityTest {
    // Test Method #1
    //Testing it with ActivityScenarioRule: Issue: Displays MainActivity rather than StartActivity.
    // goes to MainActivity For some reason ("hello world" page), even though specified StartActivity
    @Rule
    public ActivityScenarioRule<StartActivity> activityRule =
            new ActivityScenarioRule<>(StartActivity.class);
    //ActivityScenario<StartActivity> scenario = ActivityScenario.launch(StartActivity.class);
    //ActivityScenario<StartActivity> activityScenario = activityRule.getScenario();

    @Test
    /**
     * Tests whether the app switches from StartFragment to StartActivity upon button press
     */
    public void CheckContinueButton() {
        ActivityScenario<StartActivity> activityScenario = activityRule.getScenario();
        onView(withId(R.id.button_next)).perform(click()); // comes up as error as theres no button in MainActivity
        onView(withId(R.id.createAccountFragment)).check(matches(isDisplayed()));

        /*
        ActivityScenario<StartActivity> scenario = ActivityScenario.launch(StartActivity.class);
        scenario.onActivity(activity -> {
            onView(withId(R.id.button_next)).perform(click());
            onView(withId(R.id.createAccountFragment)).check(matches(isDisplayed()));
        });
         */
    }
    // Test Method #2
    // Testing it with FragmentScenario: Issue: Dependency causes Test to not launch
    // Issue #2: java.lang.RuntimeException: Failed to instantiate test runner class androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
//    @Rule
//    public FragmentScenario<StartFragment> scenario = FragmentScenario.launchInContainer(StartFragment.class);
//    @Test
//    /**
//     * Tests whether the fragments switch
//     */
//    public void testSwitchToNextFragment() {
//        //FragmentScenario<StartFragment> scenario1 = scenario
//        onView(withId(R.id.button_next)).perform(click());
//        onView(withId(R.id.createAccountFragment)).check(matches(isDisplayed()));
//    }

    // Test Method #3
    // Testing it using FragmentManager/SupportFragmentManager?
    //...

    @Test
    /**
     * Tests whether the back button on StartActivity
     */
    public void CheckBackButton() {
        ActivityScenario<StartActivity> scenario = activityRule.getScenario();
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.button_back)).perform(click());
    }
    @Test
    /**
     * Tests if a unique username is generated
     */
    public void CheckUsernameGeneration() {
        //By default, should generate unique username without having to click "Another"
        ActivityScenario<StartActivity> scenario = activityRule.getScenario();
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.name_text)).check(matches(not(withText(""))));
        //.. unfinished, need to test if names are unique
    }


}
