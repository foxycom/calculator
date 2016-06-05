package com.github.guliash.calculator;

import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
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

    /**
     * This matcher makes sense only if the adapter has a view holder for {@code position}
     * @param position position to apply matcher at
     * @param itemMatcher matcher to apply
     * @return result matcher
     */
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static Matcher<View> atPositionDoesNotExist(final int position) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("item at position does not exist");
            }

            @Override
            protected boolean matchesSafely(RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                return viewHolder == null;
            }
        };
    }

}
