package com.guliash.calculator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.guliash.calculator.di.components.AppComponent;
import com.guliash.calculator.di.components.DaggerAppComponent;
import com.guliash.calculator.di.modules.AppModule;
import com.guliash.parser.Angle;

public class CalculatorApplication extends Application {

    public Angle angleUnits;
    public static final String APP_PREFERENCES = "prefs";
    public static final String ANGLE = "angle";
    public static final String DATABASE_NAME = "calculator";

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpAppComponent();
        loadData();
    }

    private void setUpAppComponent() {
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public void setAppComponent(AppComponent component) {
        this.mAppComponent = component;
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
        editor.apply();
        this.angleUnits = angleUnit;
    }

    public boolean getBooleanField(String key, boolean defValue) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defValue);
    }

    public void setBooleanField(String key, boolean value) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
