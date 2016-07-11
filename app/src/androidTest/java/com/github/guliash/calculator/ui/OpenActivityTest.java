package com.github.guliash.calculator.ui;

import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.guliash.calculator.CalculatorApplication;
import com.guliash.calculator.storage.DBHelper;
import com.guliash.calculator.Helper;
import com.guliash.calculator.R;
import com.guliash.calculator.storage.Storage;
import com.guliash.calculator.structures.CalculatorDataSet;
import com.guliash.calculator.structures.StringVariableWrapper;
import com.guliash.calculator.ui.activities.OpenActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.guliash.calculator.Actions.clickChildViewWithId;
import static com.github.guliash.calculator.Matchers.atPosition;
import static com.github.guliash.calculator.Matchers.atPositionDoesNotExist;

@RunWith(AndroidJUnit4.class)
public class OpenActivityTest {

    @Rule
    public ActivityTestRule<OpenActivity> mActivityRule = new ActivityTestRule<>(
            OpenActivity.class, true, false);

    private Storage mStorage;

    private CalculatorDataSet mCalculatorDataSet1;
    private CalculatorDataSet mCalculatorDataSet2;
    private CalculatorDataSet mCalculatorDataSet3;
    private CalculatorDataSet mCalculatorDataSet4;
    private CalculatorDataSet mCalculatorDataSet5;

    @Before
    public void addDatasets() {
        mStorage = new DBHelper(getInstrumentation().getTargetContext()
                .getApplicationContext(), CalculatorApplication.DATABASE_NAME);
        ArrayList<StringVariableWrapper> variables1 = new ArrayList<>();
        variables1.add(new StringVariableWrapper("x", "1"));
        variables1.add(new StringVariableWrapper("y", "2"));

        ArrayList<StringVariableWrapper> variables2 = new ArrayList<>();
        variables2.add(new StringVariableWrapper("x", "1"));
        variables2.add(new StringVariableWrapper("y", "2"));

        mCalculatorDataSet1 = new CalculatorDataSet("x + y", "first", System.currentTimeMillis(), variables1);
        mCalculatorDataSet2 = new CalculatorDataSet("x / y", "second", System.currentTimeMillis(), variables2);
        mCalculatorDataSet3 = new CalculatorDataSet("x * y", "third", System.currentTimeMillis(), variables1);
        mCalculatorDataSet4 = new CalculatorDataSet("x - y", "fourth",  System.currentTimeMillis(), variables2);
        mCalculatorDataSet5 = new CalculatorDataSet("pow(x, y)", "fifth", System.currentTimeMillis(), variables2);
        mStorage.addDataSet(mCalculatorDataSet1);
        mStorage.addDataSet(mCalculatorDataSet2);
        mStorage.addDataSet(mCalculatorDataSet3);
        mStorage.addDataSet(mCalculatorDataSet4);
        mStorage.addDataSet(mCalculatorDataSet5);

        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void checkThatDatasetsAreDisplayed() {
        onView(withId(R.id.rv)).perform(scrollTo(hasDescendant(withText(mCalculatorDataSet5.getDataSetName()))));
        onView(withId(R.id.rv)).perform(scrollTo(hasDescendant(withText(mCalculatorDataSet1.getDataSetName()))));
    }

    @Test
    public void checkThatDatasetExpressionDisplayedCorrectly() {
        onView(withId(R.id.rv)).perform(scrollTo(hasDescendant(withText(mCalculatorDataSet5.getDataSetName()))));
        onView(withId(R.id.rv)).check(matches(atPosition(4, hasDescendant(
                withText(mCalculatorDataSet5.getExpression())))));
    }

    @Test
    public void checkThatDatasetTimestampDisplayedCorrectly() {
        onView(withId(R.id.rv)).perform(scrollTo(hasDescendant(withText(mCalculatorDataSet5.getDataSetName()))));
        onView(withId(R.id.rv)).check(matches(atPosition(4, hasDescendant(
                withText(Helper.getFormattedDate(mCalculatorDataSet5.getTimestamp()))))));
    }

    @Test
    public void checkThatDatasetVariablesDisplayedCorrectly() {
        onView(withId(R.id.rv)).perform(scrollTo(hasDescendant(withText(mCalculatorDataSet5.getDataSetName()))));
        onView(withId(R.id.rv)).check(matches(atPosition(4, hasDescendant(
                withText(Helper.variablesToString(mCalculatorDataSet5.getVariables()))))));
    }

    @Test
    public void checkThatOverflowButtonOpensPopupMenu() {
        onView(withId(R.id.rv)).perform(scrollToPosition(0));
        onView(withId(R.id.rv)).perform(actionOnItemAtPosition(0, clickChildViewWithId(R.id.overflow_button)));
        onView(withText(R.string.use)).check(matches(isDisplayed()));
        onView(withText(R.string.edit)).check(matches(isDisplayed()));
        onView(withText(R.string.delete)).check(matches(isDisplayed()));
    }

    @Test
    public void checkThatRemoveWorks() {
        onView(withId(R.id.rv)).perform(scrollToPosition(4));
        onView(withId(R.id.rv)).perform(actionOnItemAtPosition(4, clickChildViewWithId(R.id.overflow_button)));
        onView(withText(R.string.delete)).perform(ViewActions.click());
        onView(withId(R.id.rv)).check(matches(atPositionDoesNotExist(4)));
    }

    @After
    public void removeDatasets() {
        mStorage.deleteDataSet(mCalculatorDataSet1);
        mStorage.deleteDataSet(mCalculatorDataSet2);
        mStorage.deleteDataSet(mCalculatorDataSet3);
        mStorage.deleteDataSet(mCalculatorDataSet4);
        mStorage.deleteDataSet(mCalculatorDataSet5);
    }

}
