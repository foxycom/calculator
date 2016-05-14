package com.guliash.parser;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BasicTester extends BaseParserTester {

    @Test
    public void additionTest1() {
        assertEquals(6, calculate("1+2+3"), EPS);
    }

    @Test
    public void additionTest2() {
        assertEquals(1e+9 + 1 + 20 + 1e-9, calculate("1e9 + 1 + 20 + 1e-9"), EPS);
    }

    @Test
    public void additionTest3() {
        assertEquals(1e+9 + 1e-9, calculate("1e+9 + 1e-9"), EPS);
    }

    @Test
    public void additionTest4() {
        assertEquals(1e+9 + 1e-9, calculate("1e+9 + 1e-9"), EPS);
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void badBracket1() {
        calculate("1e9 + 1 + 20 + 1e-9 + ()");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void badBracket2() {
        calculate("((((((((()))))))))");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void badBracket3() {
        calculate("))((");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void badBracket4() {
        calculate("((4)*(2)*3");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void badBracket5() {
        calculate("(([]))");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void badBracket6() {
        calculate("((()))((()))(()()()");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void badBracket7() {
        calculate("(1)(2)");
    }

    @Test
    public void goodBracket1() {
        assertEquals(1, calculate("(((((((((1)))))))))"), EPS);
    }

    @Test
    public void goodBracket2() {
        assertEquals((((((((((1.0)+2.0*(3.0+4.0)*(2.0+2.0)/2.0)))))))),
                calculate("(((((((((1)+2*(3+4)*(2+2)/2))))))))"), EPS);
    }

    @Test
    public void goodBracket3() {
        assertEquals((1d)*(2d)*(4d+(5d+2d)*3d)*(4d-2d/3d)*(5d-3.4/2.5),
                calculate("(1)*(2)*(4+(5+2)*3)*(4-2/3)*(5-3.4/2.5)"), EPS);
    }

    @Test
    public void goodBracket4() {
        assertEquals((1d*2d*3d/4d/5d/6d/7d/8d/10d*2d+(3d/4d*2d+3d)*(4d/5d*(2d/3d))),
                calculate("(1*2*3/4/5/6/7/8/10*2+(3/4*2+3)*(4/5*(2/3)))"), EPS);
    }

    @Test
    public void goodBracket5() {
        assertEquals((2d*(3d+4d)/(3d+2d)*(2d/2d)+3d*2d)/(3d+2d*(3d/2d)),
                calculate("(2*(3+4)/(3+2)*(2/2)+3*2)/(3+2*(3/2))"), EPS);
    }

    @Test
    public void spacesTest1() {
        assertEquals(1 +         2 + 3 *    4 *  (  2  + 3),
                calculate("1 +         2 + 3 *    4 *  (  2  + 3)"), EPS);
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void spacesTest2() {
        calculate("1e43 434434");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void spacesTest3() {
        calculate("1. 2323");
    }

    @Test
    public void operatorsTest1() {
        assertEquals(3, calculate("1------2"), EPS);
    }

    @Test
    public void operatorsTest2() {
        assertEquals(-1, calculate("1+-+2"), EPS);
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void operatorsTest3() {
        calculate("1**2");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void operatorsTest4() {
        calculate("1*/2");
    }

    @Test
    public void operatorsTest5() {
        assertEquals(-2, calculate("1*-2"), EPS);
    }

    @Test
    public void operatorsTest6() {
        assertEquals(2, calculate("1*--2"), EPS);
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void operatorsTest7() {
        calculate("1++*2");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void operatorsTest8() {
        calculate("1+*2");
    }

    @Test
    public void javaCornerCases1() {
        assertEquals(Double.NaN, calculate("0/0"), EPS);
    }

    @Test
    public void javaCornerCases2() {
        assertEquals(Double.NaN, calculate("log(2,-1)"), EPS);
    }

    @Test(expected = Exception.class)
    public void betweenTwoWordsNoSpacesAllowed() {
        ArrayList<Variable> variables = new ArrayList<>();
        variables.add(new Variable("$_31dasd", 0d));
        variables.add(new Variable("faf31fa_", 0d));
        calculate("$_31dasd faf31fa_");
    }

    @Test(expected = Exception.class)
    public void illegalCharactersInWordNotAllowed() {
        ArrayList<Variable> variables = new ArrayList<>();
        variables.add(new Variable("f&4f^af31fa_", 0d));
        calculate("f&4f^af31fa_");
    }

}
