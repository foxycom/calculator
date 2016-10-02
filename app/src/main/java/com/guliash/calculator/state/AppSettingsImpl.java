package com.guliash.calculator.state;

import android.content.Context;
import android.content.SharedPreferences;

import com.guliash.parser.AngleUnits;

public class AppSettingsImpl implements AppSettings {

    private static final String APP_PREFERENCES = "prefs";
    private static final String ANGLE_UNITS = "angle";
    private static final String REVIEW = "review";

    private final Context mContext;
    private final SharedPreferences mPreferences;

    public AppSettingsImpl(Context context) {
        mContext = context;
        mPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public AngleUnits getAngleUnits() {
        int ord = mPreferences.getInt(ANGLE_UNITS, -1);
        if(ord ==  -1) {
            return AngleUnits.DEG;
        } else {
            return AngleUnits.values()[ord];
        }
    }

    @Override
    public void saveAngleUnits(AngleUnits angleUnits) {
        mPreferences.edit().putInt(ANGLE_UNITS, angleUnits.ordinal()).apply();
    }

    @Override
    public boolean isReviewInviteShown() {
        return mPreferences.getBoolean(REVIEW, false);
    }

    @Override
    public void shownReviewInvite() {
        mPreferences.edit().putBoolean(REVIEW, true).apply();;
    }
}
