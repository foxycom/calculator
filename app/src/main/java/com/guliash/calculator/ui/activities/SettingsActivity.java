package com.guliash.calculator.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;

import com.guliash.calculator.App;
import com.guliash.calculator.R;
import com.guliash.calculator.state.AppSettings;
import com.guliash.parser.AngleUnits;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity {

    private RadioGroup angleGroup;

    @Inject
    AppSettings mAppSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        App.get(this).getAppComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        angleGroup = (RadioGroup) findViewById(R.id.angles);
        setChecked();
        angleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.deg:
                        mAppSettings.saveAngleUnits(AngleUnits.DEG);
                        break;
                    case R.id.rad:
                        mAppSettings.saveAngleUnits(AngleUnits.RAD);
                        break;
                    case R.id.grad:
                        mAppSettings.saveAngleUnits(AngleUnits.GRAD);
                        break;
                }
            }
        });
    }

    private void setChecked() {
        AngleUnits angleUnits = mAppSettings.getAngleUnits();
        switch (angleUnits) {
            case DEG:
                angleGroup.check(R.id.deg);
                break;
            case RAD:
                angleGroup.check(R.id.rad);
                break;
            case GRAD:
                angleGroup.check(R.id.grad);
                break;
        }
    }
}
