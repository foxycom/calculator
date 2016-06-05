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

@RunWith(AndroidJUnit4.class)
public class DBStorageTest {

    private Storage storage;

    private static final String TEST_DATABASE = "test.db";
    private CalculatorDataSet dataSet;

    @Before
    public void setup() {
        storage = new DBHelper(InstrumentationRegistry.getTargetContext(), TEST_DATABASE);
        dataSet = new CalculatorDataSet("x + y", "test", new ArrayList<StringVariableWrapper>(),
                System.currentTimeMillis());
    }

    @Test
    public void testThatAddWorks() {
        storage.addDataSet(dataSet);
        Assert.assertEquals(storage.getDataSets().size(), 1);
    }

    @Test
    public void testThatClearWorks() {
        storage.addDataSet(dataSet);
        storage.clear();
        Assert.assertEquals(storage.getDataSets().size(), 0);
    }

    @After
    public void tearDown() {
        storage.clear();
        storage.releaseResources();
    }

}
