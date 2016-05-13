package com.guliash.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberTester extends BaseTester {
    @Test
    public void numberTest1() {
        assertEquals(1e+9, calculate("1e+9"), EPS);
    }

    @Test
    public void numberTest2() {
        assertEquals(1e-9, calculate("1e-9"), EPS);
    }

    @Test
    public void numberTest3() {
        assertEquals(1e9, calculate("1e9"), EPS);
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void numberTest4() {
        calculate("1e++9");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void numberTest5() {
        calculate("1e--9");
    }

    @Test
    public void numberTest6() {
        assertEquals(1e+10, calculate("1e10"), EPS);
    }

    @Test
    public void numberTest7() {
        calculate("1e1000000000");
    }

    @Test
    public void numberTest8() {
        assertEquals(0.02, calculate("2e-2"), EPS);
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void numberTest9() {
        calculate("2e-2.");
    }

    @Test(expected = ArithmeticParser.ArithmeticParserException.class)
    public void numberTest10() {
        calculate("2e-2e2");
    }

    @Test
    public void numberTest11() {
        assertEquals(0.02, calculate("2e-002"), EPS);
    }

    @Test
    public void numberTest12() {
        assertEquals(2e-11, calculate("2e-0011"), EPS);
    }

    @Test
    public void eCaseDoesNotMatter() {
        assertEquals(2E-1, calculate("2E-1"), EPS);
    }

    @Test
    public void parserAbleToDiffExpAndE() {
        assertEquals(Math.E * 2e-1, calculate("e*2e-1"), EPS);
    }

    @Test
    public void exponentCalculatedCorrectly() {
        assertEquals(2e-10, calculate("2e-10"), EPS);
    }

    @Test
    public void zeroExponentHandledCorrectly() {
        assertEquals(435e0, calculate("435E0"), EPS);
    }

}
