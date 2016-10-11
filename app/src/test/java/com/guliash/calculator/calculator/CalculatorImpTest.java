package com.guliash.calculator.calculator;

import android.os.Build;

import com.guliash.calculator.BuildConfig;
import com.guliash.calculator.state.AppSettings;
import com.guliash.parser.AngleUnits;
import com.guliash.parser.StringVariable;
import com.guliash.parser.evaluator.JavaEvaluator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static com.guliash.calculator.calculator.Calculator.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CalculatorImpTest {

    @Mock
    private AppSettings appSettings;

    private Calculator calculator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(appSettings.getAngleUnits()).thenReturn(AngleUnits.RAD);
        calculator = new CalculatorImp(RuntimeEnvironment.application, appSettings);
    }

    @Test
    public void calculate_emptyExpression_returnError() {
        CalculateResult result = calculator.calculate(null, new ArrayList<StringVariable>());
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void calculate_notCorrectVariable_returnError() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("", "1"));
        CalculateResult result = calculator.calculate("1", variables);
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void calculate_variablesClashesWithConstant_returnError() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable(JavaEvaluator.Constant.E.getName(), "2"));
        CalculateResult result = calculator.calculate("1", variables);
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void calculate_notUniqueVariables_returnError() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("a", "2"));
        variables.add(new StringVariable("a", "3"));
        CalculateResult result = calculator.calculate("1", variables);
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void calculate_cyclicVariables_returnError() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("a", "b"));
        variables.add(new StringVariable("b", "a"));
        CalculateResult result = calculator.calculate("1", variables);
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void calculate_variablesNotExists_returnError() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("a", "1"));
        CalculateResult result = calculator.calculate("c", variables);
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void calculate_badNumber_returnError() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("a", "1..3"));
        CalculateResult result = calculator.calculate("a", variables);
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void calculate_badExpression_returnError() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("a", "1+/3"));
        CalculateResult result = calculator.calculate("a", variables);
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void calculate_ok_returnError() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("a", "1+3"));
        CalculateResult result = calculator.calculate("a", variables);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(4.0f, result.getValue(), 1e-10);
    }

}
