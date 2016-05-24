package com.github.guliash.calculator;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {

    private static final double EPS = 1e-15;

    public static Matcher<View> withDoubleText(final double expected) {
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
