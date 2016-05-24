package com.github.guliash.calculator;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.guliash.calculator.R;
import com.guliash.calculator.ui.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Test
    public void checkBackspaceButtonRemoves() {
        String stringToTypeIn = "123";
        Espresso.onView(withId(R.id.input_field)).perform(typeText(stringToTypeIn));
        Espresso.onView(withId(R.id.backspace)).perform(click());
        Espresso.onView(withId(R.id.input_field)).check(matches(withText(stringToTypeIn.substring(0,
                stringToTypeIn.length() - 1))));

    }

}
