package com.guliash.calculator.di.components;

import com.guliash.calculator.CalculatorApplication;
import com.guliash.calculator.di.modules.AppModule;
import com.guliash.calculator.state.AppSettings;
import com.guliash.calculator.storage.Storage;
import com.guliash.calculator.ui.activities.OpenActivity;
import com.guliash.calculator.ui.activities.SaveActivity;
import com.guliash.calculator.ui.activities.SettingsActivity;
import com.guliash.calculator.ui.fragments.CalculatorFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    CalculatorApplication app();

    Storage storage();

    AppSettings appSettings();

    void inject(SaveActivity saveActivity);

    void inject(OpenActivity openActivity);

    void inject(SettingsActivity settingsActivity);

    void inject(CalculatorFragment calculatorFragment);
}
