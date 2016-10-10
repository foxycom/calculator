package com.guliash.calculator.ui.activities;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.guliash.calculator.ui.fragments.AlertDialogFragment;

public class BaseActivity extends AppCompatActivity {

    protected static final String DIALOG_TAG = "dialog";

    protected void showAlertDialog(String title, String message, String positive,
                                   String negative, String neutral, boolean cancelable, int id) {
        hideDialog();

        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message, positive,
                negative, neutral, cancelable, id);
        fragment.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    protected void hideDialog() {
        DialogFragment dialogFragment = (DialogFragment)getSupportFragmentManager()
                .findFragmentByTag(DIALOG_TAG);
        if(dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }
}
