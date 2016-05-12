package com.guliash.calculator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.guliash.parser.Angle;

public class CalculatorApplication extends Application {

    public Angle angleUnits;
    public static final String APP_PREFERENCES = "prefs";
    public static final String ANGLE = "angle";
    @Override
    public void onCreate() {
        super.onCreate();
        loadData();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(APP_PREFERENCES,
                Context.MODE_PRIVATE);
        int angleOrd = sharedPreferences.getInt(ANGLE, -1);
        if(angleOrd ==  -1) {
            angleUnits = Angle.DEG;
        } else {
            angleUnits = Angle.values()[angleOrd];
        }
    }

    public void saveAngleUnit(Angle angleUnit) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(APP_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ANGLE, angleUnit.ordinal());
        editor.commit();
    }
}
