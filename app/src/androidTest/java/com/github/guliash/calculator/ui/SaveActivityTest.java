package com.github.guliash.calculator.ui;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.guliash.calculator.Constants;
import com.guliash.calculator.R;
import com.guliash.calculator.structures.CalculatorDataSet;
import com.guliash.calculator.structures.StringVariableWrapper;
import com.guliash.calculator.ui.activities.SaveActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.guliash.calculator.Actions.clickChildViewWithId;
import static com.github.guliash.calculator.Matchers.atPosition;
import static com.github.guliash.calculator.Matchers.atPositionDoesNotExist;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class SaveActivityTest {

    private CalculatorDataSet dataset;

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


        dataset = new CalculatorDataSet(expression, datasetName, System.currentTimeMillis(), variables);

        Intent intent = new Intent();
        intent.putExtra(Constants.DATASET, dataset);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testThatExpressionCorrectlyShown() {
        onView(withId(R.id.expression)).check(matches(withText(dataset.getExpression())));
    }

    @Test
    public void testThatNameCorrectlyShown() {
        onView(withId(R.id.dataset_name)).check(matches(withText(dataset.getName())));
    }

    @Test
    public void testThatVariablesCorrectlyShown() {
        for(int i = 0; i < dataset.getVariables().size(); i++) {
            StringVariableWrapper wrapper = dataset.getVariables().get(i);
            onView(withId(R.id.variables_rv))
                    .perform(scrollToPosition(i));
            onView(withId(R.id.variables_rv))
                    .check(matches(atPosition(i, allOf(hasDescendant(withText(wrapper.getName())),
                            hasDescendant(withText(wrapper.getValue()))))));
        }
    }

    @Test
    public void testThatRemoveWorks() {
        int lastIndex = dataset.getVariables().size() - 1;
        onView(withId(R.id.variables_rv))
                .perform(scrollToPosition(lastIndex))
                .perform(actionOnItemAtPosition(lastIndex, clickChildViewWithId(R.id.remove_button)));
        onView(withId(R.id.variables_rv)).check(matches(atPositionDoesNotExist(lastIndex)));
    }

    @Test
    public void testThatAddVariableWorks() {
        int lastIndex = dataset.getVariables().size() - 1;
        onView(withId(R.id.add)).perform(click());
        onView(withId(R.id.variables_rv)).perform(scrollToPosition(lastIndex + 1));
        onView(withId(R.id.variables_rv)).check(matches(atPosition(lastIndex + 1, isDisplayed())));
    }

}
