package com.guliash.parser;

import com.guliash.parser.exceptions.CyclicVariablesDependencyException;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VariablesTester extends BaseParserTester {

    @Test
    public void variablesReadCorrectly1() {
        ArrayList<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("x", Double.toString(-1.0)));
        variables.add(new StringVariable("y", Double.toString(2.0)));
        assertEquals(Math.exp(-1d) * Math.sin(2d), calculate("exp(x)*sin(y)", variables), EPS);
    }

    @Test
    public void variablesReadCorrectly2() {
        ArrayList<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("x", Double.toString(-1.0)));
        variables.add(new StringVariable("y", Double.toString(2.0)));
        assertEquals(Math.min(-1.d, 2.d), calculate("min(x, y)", variables), EPS);
    }

    /**
     * The parser is case-sensitive. So it should be able to calculate a correct expression
     * with same variables
     */
    @Test
    public void sameVariablesButDifferentCaseTest() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("x", Double.toString(2d)));
        variables.add(new StringVariable("X", Double.toString(3d)));
        assertEquals(5d, calculate("x + X", variables), EPS);
    }

    @Test(expected = Exception.class)
    public void throwExceptionWhenVariableAndConstantHaveSameName() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("e", Double.toString(2d)));
        assertEquals(Math.E, calculate("e", variables), EPS);
    }

    @Test(expected = Exception.class)
    public void variableNameMustNotBeginWithDigit() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("1_32ab", Double.toString(2d)));
        calculate("1_32ab + 3", variables);
    }

    @Test
    public void variableNameCanBeginWithDollar() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("$23", Double.toString(2d)));
        variables.add(new StringVariable("ab", Double.toString(10d)));
        assertEquals(12d, calculate("$23 + ab", variables), EPS);
    }

    @Test
    public void variableNameCanBeginWithUnderscore() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("_23", Double.toString(2d)));
        variables.add(new StringVariable("$ab", Double.toString(10d)));
        assertEquals(12d, calculate("_23 + $ab", variables), EPS);
    }

    @Test
    public void testVariableNameConvention() {
        List<StringVariable> variables = new ArrayList<>();
        double _$12_3$ = 35d;
        double J2eV = 45d;
        double masse = 10043d;
        variables.add(new StringVariable("_$12_3$", Double.toString(_$12_3$)));
        variables.add(new StringVariable("J2eV", Double.toString(J2eV)));
        variables.add(new StringVariable("masse", Double.toString(masse)));
        assertEquals(_$12_3$ / J2eV * masse, calculate("_$12_3$ / J2eV * masse", variables), EPS);
    }

    @Test
    public void testVariablesCaseSensitiveness() {
        double j2ev = 45d;
        double J2eV = 53534;
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("j2ev", Double.toString(j2ev)));
        variables.add(new StringVariable("J2eV", Double.toString(J2eV)));
        assertEquals(j2ev / J2eV, calculate("j2ev / J2eV", variables), EPS);
    }

    @Test(expected = Exception.class)
    public void testVariablesCaseSensitivenessWrongVariable() {
        double j2ev = 45d;
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("j2ev", Double.toString(j2ev)));
        calculate("J2eV", variables);
    }


    @Test
    public void variableNameCanMatchFunctionName() {
        ArrayList<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("exp", Double.toString(3.0)));
        variables.add(new StringVariable("y", Double.toString(2.0)));
        assertEquals(3.0, calculate("exp", variables), EPS);
    }

    @Test
    public void variableNameCanMatchFunctionName2() {
        ArrayList<StringVariable> variables = new ArrayList<>();
        double sin = 433;
        double cos = 433;
        variables.add(new StringVariable("sin", Double.toString(sin)));
        variables.add(new StringVariable("cos", Double.toString(cos)));
        assertEquals(Math.cos(cos) * Math.sin(sin), calculate("cos(cos) * sin(sin)", variables), EPS);
    }

    @Test
    public void variableNameCanMatchFunctionName3() {
        ArrayList<StringVariable> variables = new ArrayList<>();
        double sin = 433;
        double cos = 433;
        variables.add(new StringVariable("sin", Double.toString(sin)));
        variables.add(new StringVariable("cos", Double.toString(cos)));
        assertEquals(Math.cos(Math.cos(sin)) * Math.sin(Math.cos(sin)), calculate("cos(cos(sin)) * sin(cos(sin))", variables), EPS);
    }

    @Test
    public void variableCanContainExpressions() {
        ArrayList<StringVariable> variables = new ArrayList<>();
        Double a = Math.sin(2*Math.cos(3));
        variables.add(new StringVariable("a", "sin(2*cos(3))"));
        assertEquals(Math.tan(a), calculate("tan(a)", variables), EPS);
    }

    @Test
    public void variablesCanContainExpressions() {
        List<StringVariable> variables = new ArrayList<>();
        double a = Math.abs(Math.cos(3));
        double b = Math.tan(Math.cos(45));
        double c = Math.acos(0.5);
        variables.add(new StringVariable("a", "abs(cos(3))"));
        variables.add(new StringVariable("b", "tan(cos(45))"));
        variables.add(new StringVariable("c", "acos(0.5)"));
        assertEquals(Math.exp(a + b * c), calculate("exp(a + b * c)", variables), EPS);
    }

    @Test
    public void variableExpressionCanContainAnotherVariable() {
        List<StringVariable> variables = new ArrayList<>();
        double a = Math.pow(2, 3);
        double b = Math.exp(a);
        variables.add(new StringVariable("a", "pow(2, 3)"));
        variables.add(new StringVariable("b", "exp(a)"));
        assertEquals(Math.cos(b), calculate("cos(b)", variables), EPS);
    }

    @Test(expected = Exception.class)
    public void variablesCannotContainIncorrectExpression() {
        List<StringVariable> variables = new ArrayList<>();
        double a = Math.cos(25*Math.pow(3, 3)*Math.exp(   3 ))*25;
        double b = Math.cos(Math.sin(25)*Math.exp(3)*Functions.logarithm(2, 3));
        variables.add(new StringVariable("a", "cos(25*pow(3, 3)*exp(   3 ))*25"));
        variables.add(new StringVariable("b", "cos(sin(25)*exp(3)*log(,2, 3))"));
        assertEquals(a + b, calculate("a + b", variables), EPS);
    }

    @Test
    public void variablesCanDependOnAnotherVariables() {
        List<StringVariable> variables = new ArrayList<>();
        double a = Math.pow(33, 3);
        double b = Math.ceil(a) * a;
        double c = Math.abs(b + a);
        double d = Math.floor(a * b * c);
        variables.add(new StringVariable("a", "pow(33, 3)"));
        variables.add(new StringVariable("b", "ceil(a) * a"));
        variables.add(new StringVariable("c", "abs(b + a)"));
        variables.add(new StringVariable("d", "floor(a * b * c)"));
        assertEquals(a + b / c + d, calculate("a + b / c + d", variables), EPS);
    }

    @Test(expected = CyclicVariablesDependencyException.class)
    public void variablesCannotDependCyclically() {
        List<StringVariable> variables = new ArrayList<>();
        variables.add(new StringVariable("x", "y"));
        variables.add(new StringVariable("y", "x"));
        calculate("x + y", variables);
    }

    @Test
    public void willCorrectlyCalcVariablesWithNonOrientedCycle() {
        List<StringVariable> variables = new ArrayList<>();

        StringVariable vx = new StringVariable("x", "sin(y) + abs(z)");
        StringVariable vy = new StringVariable("y", "cos(f) * floor(g)");
        StringVariable vz = new StringVariable("z", "asin(1) * ln(p) + sin(d)");
        StringVariable vf = new StringVariable("f", "atan(2)");
        StringVariable vg = new StringVariable("g", "pi * pow(1, z) + sin(d)");
        StringVariable vp = new StringVariable("p", "e");
        StringVariable vd = new StringVariable("d", "pi");

        variables.add(vx); variables.add(vy);
        variables.add(vz); variables.add(vf);
        variables.add(vg); variables.add(vp);
        variables.add(vd);

        double d = Math.PI;
        double p = Math.E;
        double z = Math.asin(1) * Math.log(p) + Math.sin(d);
        double g = Math.PI * Math.pow(1, z) + Math.sin(d);
        double f = Math.atan(2);
        double y = Math.cos(f) * Math.floor(g);
        double x = Math.sin(y) + Math.abs(z);

        assertEquals(d + p + z - g + y + x * 2 * f, calculate("d + p + z - g + y + x * 2 * f", variables),
                EPS);
    }


}
