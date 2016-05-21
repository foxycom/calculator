package com.guliash.calculator.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

public class AlertDialogFragment extends DialogFragment {

    private String mTitle, mMessage, mPositive, mNegative, mNeutral;

    private boolean mCancelable;
    private int mId;

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String POSITIVE = "positive";
    private static final String NEGATIVE = "negative";
    private static final String NEUTRAL = "neutral";
    private static final String CANCELABLE = "cancelable";
    private static final String ID = "id";

    private Callbacks mCallbacks;

    public static AlertDialogFragment newInstance(String title, String message, String positive,
                                                  String negative, String neutral, boolean cancelable, int id) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putString(MESSAGE, message);
        bundle.putString(POSITIVE, positive);
        bundle.putString(NEGATIVE, negative);
        bundle.putString(NEUTRAL, neutral);
        bundle.putBoolean(CANCELABLE, cancelable);
        bundle.putInt(ID, id);

        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getTargetFragment() instanceof Callbacks) {
            mCallbacks = (Callbacks)getTargetFragment();
        } else if(activity instanceof Callbacks) {
            mCallbacks = (Callbacks)activity;
        } else {
            throw new RuntimeException("Must implement AlertDialogFragment.Callbacks");
        }
    }

    public interface Callbacks {
        void onPositive(int id);
        void onNegative(int id);
        void onNeutral(int id);
        void onCancel(int id);
        void onDismiss(int id);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = (savedInstanceState != null ? savedInstanceState : getArguments());

        mTitle = args.getString(TITLE);
        mMessage = args.getString(MESSAGE);
        mPositive = args.getString(POSITIVE);
        mNegative = args.getString(NEGATIVE);
        mNeutral = args.getString(NEUTRAL);
        mCancelable = args.getBoolean(CANCELABLE);
        mId = args.getInt(ID);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(TITLE, mTitle);
        bundle.putString(MESSAGE, mMessage);
        bundle.putString(POSITIVE, mPositive);
        bundle.putString(NEGATIVE, mNegative);
        bundle.putString(NEUTRAL, mNeutral);
        bundle.putBoolean(CANCELABLE, mCancelable);
        bundle.putInt(ID, mId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        setCancelable(mCancelable);

        if(!TextUtils.isEmpty(mTitle)) {
            builder.setTitle(mTitle);
        }

        if(!TextUtils.isEmpty(mMessage)) {
            builder.setTitle(mMessage);
        }

        if(!TextUtils.isEmpty(mPositive)) {
            builder.setPositiveButton(mPositive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCallbacks.onPositive(mId);
                }
            });
        }

        if(!TextUtils.isEmpty(mNegative)) {
            builder.setNegativeButton(mNegative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCallbacks.onNegative(mId);
                }
            });
        }

        if(!TextUtils.isEmpty(mNeutral)) {
            builder.setNeutralButton(mNeutral, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCallbacks.onNeutral(mId);
                }
            });
        }

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        mCallbacks.onDismiss(mId);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        mCallbacks.onCancel(mId);
    }
}
