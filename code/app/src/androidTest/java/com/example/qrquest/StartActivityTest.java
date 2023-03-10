package com.example.qrquest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import androidx.test.core.app.ActivityScenario;
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
@RunWith(AndroidJUnit4.class)
public class StartActivityTest {
    // Because of SharedPreferences, have to uninstall App to do these testings //////

    @Rule
    public ActivityScenarioRule<StartActivity> activityRule =
            new ActivityScenarioRule<>(StartActivity.class);

    @Test
    /**
     * Tests whether the app switches from StartFragment to StartActivity upon button press
     */
    public void CheckContinueButton() {
        // Because of SharedPreferences, have to uninstall App to do these testings
        ActivityScenario<StartActivity> activityScenario = activityRule.getScenario();
        onView(withId(R.id.button_next)).perform(click());
        onView(withId(R.id.createAccountFragmentView)).check(matches(isDisplayed()));
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
        onView(withId(R.id.button_elevated_secondary)).perform((click()));
        onView(withId(R.id.name_text)).check(matches(not(withText(" "))));
        onView((withId(R.id.button_elevated_primary))).perform(click());
        // test username/Firestore testing in Database testing class

    }


}
