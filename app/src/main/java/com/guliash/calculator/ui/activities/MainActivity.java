package com.guliash.calculator.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.guliash.calculator.Constants;
import com.guliash.calculator.R;
import com.guliash.calculator.structures.CalculatorDataSet;
import com.guliash.calculator.ui.fragments.CalculatorFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.open) {
            showOpenActivity();
        }
        if (id == R.id.save) {
            showSaveActivity();
        }
        if (id == R.id.help) {
            showHelpActivity();
        }
        if (id == R.id.settings) {
            showSettingsActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            CalculatorFragment calculatorFragment = (CalculatorFragment) getSupportFragmentManager().
                    findFragmentById(R.id.calculator_fragment);
            if (requestCode == Constants.OPEN_ACTIVITY_REQUEST_CODE) {
                calculatorFragment.setDataset((CalculatorDataSet) data.getParcelableExtra(Constants.DATASET));
            }
            if (requestCode == Constants.SAVE_ACTIVITY_REQUEST_CODE) {
                calculatorFragment.setDataset((CalculatorDataSet) data.getParcelableExtra(Constants.DATASET));
            }
        }
    }

    private void showOpenActivity() {
        Intent intent = new Intent(this, OpenActivity.class);
        startActivityForResult(intent, Constants.OPEN_ACTIVITY_REQUEST_CODE);
    }

    private void showSaveActivity() {
        Intent intent = new Intent(this, SaveActivity.class);
        CalculatorFragment fragment = (CalculatorFragment)
                getSupportFragmentManager().findFragmentById(R.id.calculator_fragment);
        intent.putExtra(Constants.DATASET, fragment.getDataset());
        startActivityForResult(intent, Constants.SAVE_ACTIVITY_REQUEST_CODE);
    }

    private void showHelpActivity() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}
