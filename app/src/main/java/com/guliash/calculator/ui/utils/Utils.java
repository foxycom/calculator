package com.guliash.calculator.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Utils {

    public static void disableKeyboardOnEditTextFocus(EditText editText) {
        if (editText != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                editText.setShowSoftInputOnFocus(false);
            } else {
                editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                editText.setTextIsSelectable(true);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

}
