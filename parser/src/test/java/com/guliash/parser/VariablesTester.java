package com.guliash.parser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VariablesTester extends BaseParserTester {

    @Test
    public void variablesReadCorrectly1() {
        ArrayList<Variable> variables = new ArrayList<>();
        variables.add(new Variable("x", -1.0));
        variables.add(new Variable("y", 2.0));
        assertEquals(Math.exp(-1d) * Math.sin(2d), calculate("exp(x)*sin(y)", variables), EPS);
    }

    @Test
    public void variablesReadCorrectly2() {
        ArrayList<Variable> variables = new ArrayList<>();
        variables.add(new Variable("x", -1.0));
        variables.add(new Variable("y", 2.0));
        assertEquals(Math.min(-1.d, 2.d), calculate("min(x, y)", variables), EPS);
    }

    /**
     * The parser is case-sensitive. So it should be able to calculate a correct expression
     * with same variables
     */
    @Test
    public void sameVariablesButDifferentCaseTest() {
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("x", 2d));
        variables.add(new Variable("X", 3d));
        assertEquals(5d, calculate("x + X", variables), EPS);
    }

    @Test
    public void constantsShouldHaveMorePriorityThanVariables() {
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("e", 2d));
        assertEquals(Math.E, calculate("e", variables), EPS);
    }

    @Test(expected = Exception.class)
    public void variableNameMustNotBeginWithDigit() {
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("1_32ab", 2d));
        calculate("1_32ab + 3", variables);
    }

    @Test
    public void variableNameCanBeginWithDollar() {
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("$23", 2d));
        variables.add(new Variable("ab", 10d));
        assertEquals(12d, calculate("$23 + ab", variables), EPS);
    }

    @Test
    public void variableNameCanBeginWithUnderscore() {
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("_23", 2d));
        variables.add(new Variable("$ab", 10d));
        assertEquals(12d, calculate("_23 + $ab", variables), EPS);
    }

    @Test
    public void testVariableNameConvention() {
        List<Variable> variables = new ArrayList<>();
        double _$12_3$ = 35d;
        double J2eV = 45d;
        double masse = 10043d;
        variables.add(new Variable("_$12_3$", _$12_3$));
        variables.add(new Variable("J2eV", J2eV));
        variables.add(new Variable("masse", masse));
        assertEquals(_$12_3$ / J2eV * masse, calculate("_$12_3$ / J2eV * masse", variables), EPS);
    }

    @Test
    public void testVariablesCaseSensitiveness() {
        double j2ev = 45d;
        double J2eV = 53534;
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("j2ev", j2ev));
        variables.add(new Variable("J2eV", J2eV));
        assertEquals(j2ev / J2eV, calculate("j2ev / J2eV", variables), EPS);
    }

    @Test(expected = Exception.class)
    public void testVariablesCaseSensitivenessWrongVariable() {
        double j2ev = 45d;
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("j2ev", j2ev));
        calculate("J2eV", variables);
    }


    @Test
    public void variableNameCanMatchFunctionName() {
        ArrayList<Variable> variables = new ArrayList<>();
        variables.add(new Variable("exp", 3.0));
        variables.add(new Variable("y", 2.0));
        assertEquals(3.0, calculate("exp", variables), EPS);
    }

    @Test
    public void variableNameCanMatchFunctionName2() {
        ArrayList<Variable> variables = new ArrayList<>();
        double sin = 433;
        double cos = 433;
        variables.add(new Variable("sin", sin));
        variables.add(new Variable("cos", cos));
        assertEquals(Math.cos(cos) * Math.sin(sin), calculate("cos(cos) * sin(sin)", variables), EPS);
    }

    @Test
    public void variableNameCanMatchFunctionName3() {
        ArrayList<Variable> variables = new ArrayList<>();
        double sin = 433;
        double cos = 433;
        variables.add(new Variable("sin", sin));
        variables.add(new Variable("cos", cos));
        assertEquals(Math.cos(Math.cos(sin)) * Math.sin(Math.cos(sin)), calculate("cos(cos(sin)) * sin(cos(sin))", variables), EPS);
    }

}
