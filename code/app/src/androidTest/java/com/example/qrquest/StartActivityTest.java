package com.example.qrquest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

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
 * @author Abinand Nanthananthan
 */
@RunWith(AndroidJUnit4.class) // not sure if needed
public class StartActivityTest {
    @Rule
    // goes to MainActivity For some reason ("hello world" page), even though
    // specified StartActivity
    public ActivityScenarioRule<StartActivity> activityRule =
            new ActivityScenarioRule<>(StartActivity.class);

    ActivityScenario<StartActivity> scenario = ActivityScenario.launch(StartActivity.class);

    @Test
    /**
     * Tests whether the app switches from StartFragment to StartActivity upon button press
     */
    public void CheckContinueButton() {
        ActivityScenario<StartActivity> scenario = activityRule.getScenario();
        onView(withId(R.id.button_next)).perform(click()); // comes up as error as theres no button in MainActivity
        onView(withId(R.id.createAccountFragment)).check(matches(isDisplayed()));
    }

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
