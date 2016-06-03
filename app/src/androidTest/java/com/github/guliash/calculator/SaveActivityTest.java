package com.github.guliash.calculator;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.guliash.calculator.Constants;
import com.guliash.calculator.R;
import com.guliash.calculator.structures.CalculatorDataset;
import com.guliash.calculator.structures.StringVariableWrapper;
import com.guliash.calculator.ui.activities.SaveActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class SaveActivityTest {

    private CalculatorDataset dataset;

    @Rule
    public ActivityTestRule<SaveActivity> mActivityRule = new ActivityTestRule<>(
            SaveActivity.class, true, false);

    @Before
    public void setup() {

        String expression = "x + y + z * sin(k)";
        String datasetName = "dataset";

        ArrayList<StringVariableWrapper> variables = new ArrayList<>();
        variables.add(new StringVariableWrapper("x", "2"));
        variables.add(new StringVariableWrapper("y", "5.4"));
        variables.add(new StringVariableWrapper("z", "3.2"));
        variables.add(new StringVariableWrapper("k", "3.14"));


        dataset = new CalculatorDataset(expression, datasetName, variables, System.currentTimeMillis());

        Intent intent = new Intent();
        intent.putExtra(Constants.DATASET, dataset);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testThatExpressionCorrectlyShown() {
        Espresso.onView(ViewMatchers.withId(R.id.expression)).check(matches(ViewMatchers.withText(dataset.expression)));
    }

}
