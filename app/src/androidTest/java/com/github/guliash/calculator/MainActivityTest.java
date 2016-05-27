package com.github.guliash.calculator;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.guliash.calculator.R;
import com.guliash.calculator.ui.activities.HelpActivity;
import com.guliash.calculator.ui.activities.MainActivity;
import com.guliash.calculator.ui.activities.OpenActivity;
import com.guliash.calculator.ui.activities.SaveActivity;
import com.guliash.calculator.ui.activities.SettingsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.guliash.calculator.Matchers.atPosition;
import static com.github.guliash.calculator.Matchers.atPositionDoesNotExist;
import static com.github.guliash.calculator.Matchers.withDoubleText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Test
    public void checkBackspaceButtonRemoves() {
        String stringToTypeIn = "123";
        onView(withId(R.id.input_field)).perform(typeText(stringToTypeIn));
        onView(withId(R.id.backspace)).perform(click());
        onView(withId(R.id.input_field)).check(matches(withText(stringToTypeIn.substring(0,
                stringToTypeIn.length() - 1))));
    }

    @Test
    public void checkEqualsCalculates() {
        String stringToTypeIn = "2 + 3";
        onView(withId(R.id.input_field)).perform(typeText(stringToTypeIn));
        onView(withId(R.id.equals_image_button)).perform(click());
        onView(withId(R.id.result_field)).check(matches(withDoubleText(5.0)));
    }

    @Test
    public void checkOpenMenuItemStartsOpenActivity() {
        Intents.init();

        try {
            onView(withId(R.id.open)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.open)).perform(click());
        }

        Intents.intended(IntentMatchers.hasComponent(OpenActivity.class.getName()));

        Intents.release();
    }

    @Test
    public void checkHelpMenuItemStartsHelpActivity() {
        Intents.init();

        try {
            onView(withId(R.id.help)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.help)).perform(click());
        }

        Intents.intended(IntentMatchers.hasComponent(HelpActivity.class.getName()));

        Intents.release();
    }

    @Test
    public void checkSaveMenuItemStartsSaveActivity() {
        Intents.init();

        try {
            onView(withId(R.id.save)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.save)).perform(click());
        }

        Intents.intended(IntentMatchers.hasComponent(SaveActivity.class.getName()));

        Intents.release();
    }

    @Test
    public void checkSettingsMenuItemStartsSettingsActivity() {
        Intents.init();

        try {
            onView(withId(R.id.settings)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.settings)).perform(click());
        }

        Intents.intended(IntentMatchers.hasComponent(SettingsActivity.class.getName()));

        Intents.release();
    }

    @Test
    public void checkThatAddVariableButtonAddsVariable() {
        onView(withId(R.id.add_variable_button)).perform(click());
        onView(withId(R.id.variables_rv)).perform(scrollToPosition(2));
        onView(withId(R.id.variables_rv)).check(matches(atPosition(2, isDisplayed())));
    }

    @Test
    public void checkThatRemoveVariableWorks() {
        onView(withId(R.id.variables_rv)).perform(actionOnItemAtPosition(1,
                Actions.clickChildViewWithId(R.id.remove_button)));
        onView(withId(R.id.variables_rv)).check(matches(atPositionDoesNotExist(1)));
    }



}
