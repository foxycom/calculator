package com.guliash.calculator.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;

import com.guliash.calculator.R;
import com.guliash.parser.Angle;

public class SettingsActivity extends BaseActivity {

    private RadioGroup angleGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        angleGroup = (RadioGroup)findViewById(R.id.angles);
        setChecked();
        angleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.deg:
                        getApp().saveAngleUnit(Angle.DEG);
                        break;
                    case R.id.rad:
                        getApp().saveAngleUnit(Angle.RAD);
                        break;
                    case R.id.grad:
                        getApp().saveAngleUnit(Angle.GRAD);
                        break;
                }
            }
        });
    }

    private void setChecked() {
        Angle angle = getApp().angleUnits;
        switch (angle) {
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
