package com.guliash.calculator.di.components;

import com.guliash.calculator.CalculatorApplication;
import com.guliash.calculator.di.modules.AppModule;
import com.guliash.calculator.storage.Storage;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    CalculatorApplication app();

    Storage storage();

}
