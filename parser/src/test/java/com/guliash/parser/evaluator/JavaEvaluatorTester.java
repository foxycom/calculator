package com.guliash.parser.evaluator;

import com.guliash.parser.AngleUnits;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.guliash.parser.Functions.acot;
import static com.guliash.parser.Functions.cot;
import static com.guliash.parser.Functions.coth;
import static com.guliash.parser.Functions.factorial;
import static com.guliash.parser.Functions.logarithm;
import static com.guliash.parser.Functions.mod;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.cosh;
import static java.lang.Math.exp;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.signum;
import static java.lang.Math.sin;
import static java.lang.Math.sinh;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static java.lang.Math.tanh;
import static org.junit.Assert.assertEquals;

public class JavaEvaluatorTester extends BaseEvaluatorTester {

    public Evaluator evaluator;
    public List<Double> args0, args1, args2;

    @Before
    public void setUp() {
        evaluator = new JavaEvaluator(AngleUnits.RAD);
        args0 = new ArrayList<>();
        args1 = new ArrayList<>();
        args1.add(25d);
        args2 = new ArrayList<>();
        args2.add(25d);
        args2.add(35d);
    }

    @Test
    public void evaluatorHasSin() {
        assertEquals(sin(args1.get(0)), evaluator.evaluateFunction("sin", args1), EPS);
    }

    @Test
    public void evaluatorHasCos() {
        assertEquals(cos(args1.get(0)), evaluator.evaluateFunction("cos", args1), EPS);
    }

    @Test
    public void evaluatorHasTan() {
        assertEquals(tan(args1.get(0)), evaluator.evaluateFunction("tan", args1), EPS);
    }

    @Test
    public void evaluatorHasCot() {
        assertEquals(cot(args1.get(0)), evaluator.evaluateFunction("cot", args1), EPS);
    }

    @Test
    public void evaluatorHasAsin() {
        assertEquals(asin(args1.get(0)), evaluator.evaluateFunction("asin", args1), EPS);
    }

    @Test
    public void evaluatorHasAcos() {
        assertEquals(acos(args1.get(0)), evaluator.evaluateFunction("acos", args1), EPS);
    }

    @Test
    public void evaluatorHasAtan() {
        assertEquals(atan(args1.get(0)), evaluator.evaluateFunction("atan", args1), EPS);
    }

    @Test
    public void evaluatorHasAcot() {
        assertEquals(acot(args1.get(0)), evaluator.evaluateFunction("acot", args1), EPS);
    }

    @Test
    public void evaluatorHasSinh() {
        assertEquals(sinh(args1.get(0)), evaluator.evaluateFunction("sinh", args1), EPS);
    }

    @Test
    public void evaluatorHasCosh() {
        assertEquals(cosh(args1.get(0)), evaluator.evaluateFunction("cosh", args1), EPS);
    }

    @Test
    public void evaluatorHasTanh() {
        assertEquals(tanh(args1.get(0)), evaluator.evaluateFunction("tanh", args1), EPS);
    }

    @Test
    public void evaluatorHasCoth() {
        assertEquals(coth(args1.get(0)), evaluator.evaluateFunction("coth", args1), EPS);
    }

    @Test
    public void evaluatorHasPow() {
        assertEquals(pow(args2.get(0), args2.get(1)), evaluator.evaluateFunction("pow", args2), EPS);
    }

    @Test
    public void evaluatorHasSqrt() {
        assertEquals(sqrt(args1.get(0)), evaluator.evaluateFunction("sqrt", args1), EPS);
    }

    @Test
    public void evaluatorHasExp() {
        assertEquals(exp(args1.get(0)), evaluator.evaluateFunction("exp", args1), EPS);
    }

    @Test
    public void evaluatorHasLn() {
        assertEquals(log(args1.get(0)), evaluator.evaluateFunction("ln", args1), EPS);
    }

    @Test
    public void evaluatorHasLog() {
        assertEquals(logarithm(args2.get(0), args2.get(1)), evaluator.evaluateFunction("log", args2), EPS);
    }

    @Test
    public void evaluatorHasLog10() {
        assertEquals(log10(args1.get(0)), evaluator.evaluateFunction("log10", args1), EPS);
    }

    @Test
    public void evaluatorHasAbs() {
        assertEquals(abs(args1.get(0)), evaluator.evaluateFunction("abs", args1), EPS);
    }

    @Test
    public void evaluatorHasFloor() {
        assertEquals(floor(args1.get(0)), evaluator.evaluateFunction("floor", args1), EPS);
    }

    @Test
    public void evaluatorHasCeil() {
        assertEquals(ceil(args1.get(0)), evaluator.evaluateFunction("ceil", args1), EPS);
    }

    @Test
    public void evaluatorHasMin() {
        assertEquals(min(args2.get(0), args2.get(1)), evaluator.evaluateFunction("min", args2), EPS);
    }

    @Test
    public void evaluatorHasMax() {
        assertEquals(max(args2.get(0), args2.get(1)), evaluator.evaluateFunction("max", args2), EPS);
    }

    @Test
    public void evaluatorHasRandom() {
        evaluator.evaluateFunction("random", args0);
    }

    @Test
    public void evaluatorHasRound() {
        assertEquals(round(args1.get(0)), evaluator.evaluateFunction("round", args1), EPS);
    }

    @Test
    public void evaluatorHasSignum() {
        assertEquals(signum(args1.get(0)), evaluator.evaluateFunction("sgn", args1), EPS);
    }

    @Test
    public void evaluatorHasFact() {
        assertEquals(factorial(args1.get(0)), evaluator.evaluateFunction("fact", args1), EPS);
    }

    @Test
    public void evaluatorHasMod() {
        assertEquals(mod(args2.get(0), args2.get(1)), evaluator.evaluateFunction("mod", args2), EPS);
    }
}
