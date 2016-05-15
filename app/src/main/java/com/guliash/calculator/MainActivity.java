package com.guliash.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
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
        if(id == R.id.open) {
            showOpenActivity();
        }
        if(id == R.id.save) {
            showSaveActivity();
        }
        if(id == R.id.help) {
            showHelpActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == Constants.OPEN_ACTIVITY_REQUEST_CODE) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                CalculatorFragment calculatorFragment = (CalculatorFragment)fragmentManager.
                        findFragmentById(R.id.calculator_fragment);
                calculatorFragment.setDataset((CalculatorDataset)data.getParcelableExtra(Constants.DATASET));
            }
        }
    }

    private void showOpenActivity() {
        Intent intent = new Intent(this, OpenActivity.class);
        startActivityForResult(intent, Constants.OPEN_ACTIVITY_REQUEST_CODE);
    }

    private void showSaveActivity() {
        Intent intent = new Intent(this, SaveActivity.class);
        FragmentManager fragmentManager = getSupportFragmentManager();
        CalculatorFragment fragment = (CalculatorFragment)
                fragmentManager.findFragmentById(R.id.calculator_fragment);
        intent.putExtra(Constants.DATASET, fragment.getDataset());
        startActivity(intent);
    }

    private void showHelpActivity() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}
