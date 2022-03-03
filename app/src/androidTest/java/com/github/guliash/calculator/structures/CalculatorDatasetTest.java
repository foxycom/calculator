package com.github.guliash.calculator.structures;

import android.support.test.runner.AndroidJUnit4;

import com.guliash.calculator.structures.CalculatorDataSet;
import com.guliash.calculator.structures.StringVariableWrapper;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@Ignore
@RunWith(AndroidJUnit4.class)
public class CalculatorDatasetTest {

    private CalculatorDataSet getTestDataSet() {
        ArrayList<StringVariableWrapper> vars = new ArrayList<>();
        vars.add(new StringVariableWrapper("z", "2"));
        vars.add(new StringVariableWrapper("x", "1"));
        return new CalculatorDataSet("expression", "name", System.currentTimeMillis(), vars);

    }

    @Test
    public void testClone() {
        CalculatorDataSet origin = getTestDataSet();
        CalculatorDataSet copy = origin.clone();
        Assert.assertEquals(origin.getExpression(), copy.getExpression());
        Assert.assertEquals(origin.getName(), copy.getName());
        Assert.assertEquals(origin.getTimestamp(), copy.getTimestamp());
        Assert.assertEquals(origin.getVariables(), copy.getVariables());
    }

    @Test
    public void testGettersAndSetters() {
        String expression = "x + y / z";
        String dataSetName = "testName";
        long timestamp = System.currentTimeMillis();
        ArrayList<StringVariableWrapper> vars = new ArrayList<>();
        vars.add(new StringVariableWrapper("x", "2"));
        vars.add(new StringVariableWrapper("y", "3"));
        CalculatorDataSet dataSet = new CalculatorDataSet(expression, dataSetName, timestamp, vars);

        dataSet.setExpression(expression);
        Assert.assertEquals(expression, dataSet.getExpression());

        dataSet.setName(dataSetName);
        Assert.assertEquals(dataSetName, dataSet.getName());

        dataSet.setTimestamp(timestamp);
        Assert.assertEquals(timestamp, dataSet.getTimestamp());

        dataSet.setVariables(vars);
        Assert.assertEquals(vars, dataSet.getVariables());
    }

    @Test
    public void testEquals() {
        CalculatorDataSet dataSet1 = getTestDataSet();
        CalculatorDataSet dataSet2 = getTestDataSet();

        Assert.assertEquals(true, dataSet1.equals(dataSet2));
        Assert.assertEquals(false, dataSet1.equals(null));
        dataSet2.setName("wrong");
        Assert.assertEquals(false, dataSet1.equals(dataSet2));
    }

}
