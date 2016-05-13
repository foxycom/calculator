package com.guliash.parser;

import com.guliash.parser.Angle;
import com.guliash.parser.ArithmeticParser;
import com.guliash.parser.Variable;

import java.util.ArrayList;
import java.util.List;

public class BaseTester {

    protected static final double EPS = 1e-15;

    /**
     * calculates without variables and at RAD mode
     * @param expression
     * @return result
     */
    protected double calculate(String expression) {
        return calculate(expression, new ArrayList<Variable>(), Angle.RAD);
    }

    protected double calculate(String expression, Angle angle) {
        return calculate(expression, new ArrayList<Variable>(), angle);
    }

    protected double calculate(String expression, List<Variable> variables) {
        return calculate(expression, variables, Angle.RAD);
    }

    protected double calculate(String expression, List<Variable> variables, Angle angle) {
        return new ArithmeticParser(expression, variables, angle).calculate();
    }
}