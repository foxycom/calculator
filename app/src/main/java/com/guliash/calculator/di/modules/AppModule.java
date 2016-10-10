package com.guliash.calculator.di.modules;

import android.content.Context;

import com.guliash.calculator.CalculatorApplication;
import com.guliash.calculator.state.AppSettings;
import com.guliash.calculator.state.AppSettingsImpl;
import com.guliash.calculator.storage.DBHelper;
import com.guliash.calculator.storage.Storage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private CalculatorApplication mApplication;

    public AppModule(CalculatorApplication application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public CalculatorApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Storage provideStorage() {
        return new DBHelper(mApplication, DBHelper.DATABASE_NAME);
    }

    @Provides
    @Singleton
    public AppSettings provideAppSettings(Context context) {
        return new AppSettingsImpl(context);
    }
}
