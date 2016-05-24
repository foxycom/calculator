package com.github.guliash.calculator;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.guliash.calculator.R;
import com.guliash.calculator.ui.activities.HelpActivity;
import com.guliash.calculator.ui.activities.MainActivity;
import com.guliash.calculator.ui.activities.OpenActivity;
import com.guliash.calculator.ui.activities.SaveActivity;
import com.guliash.calculator.ui.activities.SettingsActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AnyOf.anyOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final double EPS = 1e-15;

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
        onView(withId(R.id.result_field)).check(matches(withDouble(5.0)));
    }

    @Test
    public void checkOpenMenuItemStartsOpenActivity() {
        Intents.init();

        try {
            onView(anyOf(withId(R.id.open), withText(R.string.open))).perform(click());
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
            onView(anyOf(withId(R.id.help), withText(R.string.help))).perform(click());
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
            onView(anyOf(withId(R.id.save), withText(R.string.save))).perform(click());
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
            onView(anyOf(withId(R.id.settings), withText(R.string.settings))).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.settings)).perform(click());
        }

        Intents.intended(IntentMatchers.hasComponent(SettingsActivity.class.getName()));

        Intents.release();
    }


    public static Matcher<View> withDouble(final double expected) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if(!(item instanceof TextView)) {
                    return false;
                }

                double actual = Double.valueOf(((TextView) item).getText().toString());

                return Math.abs(actual - expected) < EPS;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Actual value differs from expected");
            }
        };
    }



}
