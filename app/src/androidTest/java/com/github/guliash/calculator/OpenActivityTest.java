package com.github.guliash.calculator;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.guliash.calculator.DBHelper;
import com.guliash.calculator.R;
import com.guliash.calculator.structures.CalculatorDataset;
import com.guliash.calculator.structures.StringVariableWrapper;
import com.guliash.calculator.ui.activities.OpenActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class OpenActivityTest {

    @Rule
    public ActivityTestRule<OpenActivity> mActivityRule = new ActivityTestRule<>(
            OpenActivity.class, false, false);

    private DBHelper mDbHelper;

    private CalculatorDataset mCalculatorDataset1;
    private CalculatorDataset mCalculatorDataset2;
    private CalculatorDataset mCalculatorDataset3;
    private CalculatorDataset mCalculatorDataset4;
    private CalculatorDataset mCalculatorDataset5;

    @Before
    public void addDatasets() {
        mDbHelper = new DBHelper(InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getApplicationContext());
        ArrayList<StringVariableWrapper> variables1 = new ArrayList<>();
        variables1.add(new StringVariableWrapper("x", "1"));
        variables1.add(new StringVariableWrapper("y", "2"));

        ArrayList<StringVariableWrapper> variables2 = new ArrayList<>();
        variables2.add(new StringVariableWrapper("x", "1"));
        variables2.add(new StringVariableWrapper("y", "2"));

        mCalculatorDataset1 = new CalculatorDataset("x + y", "first", variables1, System.currentTimeMillis());
        mCalculatorDataset2 = new CalculatorDataset("x / y", "second", variables2, System.currentTimeMillis());
        mCalculatorDataset3 = new CalculatorDataset("x * y", "third", variables1, System.currentTimeMillis());
        mCalculatorDataset4 = new CalculatorDataset("x - y", "fourth", variables2, System.currentTimeMillis());
        mCalculatorDataset5 = new CalculatorDataset("pow(x, y)", "fifth", variables2, System.currentTimeMillis());
        mDbHelper.addDataset(mCalculatorDataset1);
        mDbHelper.addDataset(mCalculatorDataset2);
        mDbHelper.addDataset(mCalculatorDataset3);
        mDbHelper.addDataset(mCalculatorDataset4);
        mDbHelper.addDataset(mCalculatorDataset5);

        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void checkThatDatasetsAreDisplayed() {
        onView(withId(R.id.rv)).perform(scrollTo(hasDescendant(withText("fifth"))));
        onView(withId(R.id.rv)).perform(scrollTo(hasDescendant(withText("first"))));
    }

    @After
    public void removeDatasets() {
        mDbHelper.deleteData(mCalculatorDataset1.datasetName);
        mDbHelper.deleteData(mCalculatorDataset2.datasetName);
    }

}
