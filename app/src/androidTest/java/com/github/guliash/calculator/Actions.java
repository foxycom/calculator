package com.github.guliash.calculator;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Matcher;

public class Actions {

    public static ViewAction inputTextForChildViewWitId(final int id, final String text) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Input text in a child view with the given id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                EditText editText = (EditText)view.findViewById(id);
                editText.setText(text);
            }
        };
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with a given id";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.findViewById(id).performClick();
            }
        };
    }

}
