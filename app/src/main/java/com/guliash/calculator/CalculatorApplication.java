package com.guliash.calculator;

import android.app.Application;
import android.content.Context;

import com.guliash.calculator.di.components.AppComponent;
import com.guliash.calculator.di.components.DaggerAppComponent;
import com.guliash.calculator.di.modules.AppModule;

public class CalculatorApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpAppComponent();
    }

    private void setUpAppComponent() {
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public static CalculatorApplication get(Context context) {
        return (CalculatorApplication)context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public void setAppComponent(AppComponent component) {
        this.mAppComponent = component;
    }
}
