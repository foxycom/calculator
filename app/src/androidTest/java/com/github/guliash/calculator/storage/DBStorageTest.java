package com.github.guliash.calculator.storage;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.guliash.calculator.storage.DBHelper;
import com.guliash.calculator.storage.Storage;
import com.guliash.calculator.structures.CalculatorDataSet;
import com.guliash.calculator.structures.StringVariableWrapper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DBStorageTest {

    private Storage storage;

    private static final String TEST_DATABASE = "test.db";
    private CalculatorDataSet dataSet1, dataSet2;

    @Before
    public void setup() {
        storage = new DBHelper(InstrumentationRegistry.getTargetContext(), TEST_DATABASE);
        ArrayList<StringVariableWrapper> variables1 = new ArrayList<>();
        variables1.add(new StringVariableWrapper("x", "2.5"));
        variables1.add(new StringVariableWrapper("y", "55.3"));
        dataSet1 = new CalculatorDataSet("x + y", "test1", System.currentTimeMillis(), variables1);

        ArrayList<StringVariableWrapper> variables2 = new ArrayList<>();
        variables2.add(new StringVariableWrapper("x", "33.5"));
        variables2.add(new StringVariableWrapper("y", "43.3"));
        variables2.add(new StringVariableWrapper("z", "45.3"));
        dataSet2 = new CalculatorDataSet("x + y / z", "test2", System.currentTimeMillis() + 1, variables2);
    }

    @Test
    public void testThatAddWorks() {
        storage.addDataSet(dataSet1);
        storage.addDataSet(dataSet2);
        List<CalculatorDataSet> dataSets = storage.getDataSets();

        Assert.assertEquals(dataSets.size(), 2);
        checkThatDataSetContentsAreEqual(dataSets.get(0), dataSet2);
        checkThatDataSetContentsAreEqual(dataSets.get(1), dataSet1);
    }

    @Test
    public void testThatAddDataSetReturnsFalseIfDataSetAlreadyAdded() {
        storage.addDataSet(dataSet1);
        Assert.assertEquals(storage.addDataSet(dataSet1), false);
    }

    private void checkThatDataSetContentsAreEqual(CalculatorDataSet first, CalculatorDataSet second) {
        Assert.assertEquals(first.getName(), second.getName());
        Assert.assertEquals(first.getExpression(), second.getExpression());
        Assert.assertEquals(first.getTimestamp(), second.getTimestamp());
        Assert.assertEquals(first.getVariables().size(), second.getVariables().size());

        for(int i = 0; i < first.getVariables().size(); i++) {
            Assert.assertEquals(first.getVariables().get(i).getName(), second.getVariables().get(i).getName());
            Assert.assertEquals(first.getVariables().get(i).getValue(), second.getVariables().get(i).getValue());
        }
    }

    @Test
    public void testThatClearWorks() {
        storage.addDataSet(dataSet1);
        storage.addDataSet(dataSet2);
        storage.clear();
        Assert.assertEquals(storage.getDataSets().size(), 0);
    }

    @Test
    public void testThatUpdateWorks() {
        storage.addDataSet(dataSet1);

        dataSet2.setName(dataSet1.getName());

        storage.updateDataSet(dataSet2);

        List<CalculatorDataSet> dataSets = storage.getDataSets();
        Assert.assertEquals(dataSets.size(), 1);

        checkThatDataSetContentsAreEqual(dataSet2, dataSets.get(0));
    }

    @Test
    public void testThatUpdateDataSetReturnsFalseIfDataSetWasNotPreviouslyAdded() {
        Assert.assertEquals(storage.updateDataSet(dataSet1), false);
    }

    @Test
    public void testThatHasDataSetReturnsTrueIfDataSetWasPreviouslyAdded() {
        storage.addDataSet(dataSet1);
        Assert.assertEquals(storage.hasDataSet(dataSet1), true);
    }

    @Test
    public void testThatHasDataSetReturnsFalseIfDataSetWasNotPreviouslyAdded() {
        Assert.assertEquals(storage.hasDataSet(dataSet1), false);
    }

    @Test
    public void testThatDeleteDataSetWorks() {
        storage.addDataSet(dataSet1);
        storage.addDataSet(dataSet2);

        Assert.assertEquals(storage.deleteDataSet(dataSet1), true);

        List<CalculatorDataSet> dataSets = storage.getDataSets();
        Assert.assertEquals(dataSets.size(), 1);
        checkThatDataSetContentsAreEqual(dataSets.get(0), dataSet2);
    }

    @Test
    public void testThatDeleteDataSetReturnsFalseIfTheDataSetWasNotAdded() {
        Assert.assertEquals(storage.deleteDataSet(dataSet1), false);
    }

    @Test
    public void testThatGetDataSetsWorks() {
        storage.addDataSet(dataSet1);
        storage.addDataSet(dataSet2);

        List<CalculatorDataSet> dataSets = storage.getDataSets();

        Assert.assertEquals(dataSets.size(), 2);

        checkThatDataSetContentsAreEqual(dataSets.get(0), dataSet2);
        checkThatDataSetContentsAreEqual(dataSets.get(1), dataSet1);
    }

    @After
    public void tearDown() {
        storage.clear();
        storage.releaseResources();
    }

}
