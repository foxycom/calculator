package com.guliash.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static com.guliash.parser.Functions.*;

public class MathTester extends BaseParserTester {

    @Test
    public void absTest1() {
        assertEquals(Math.abs(-1), calculate("abs(-1)"), EPS);
    }

    @Test
    public void absTest2() {
        assertEquals(Math.abs(1), calculate("abs(1)"), EPS);
    }

    @Test
    public void absTest3() {
        assertEquals(Math.abs(0), calculate("abs(0)"), EPS);
    }

    @Test
    public void ceilTest1() {
        assertEquals(Math.ceil(-2.5), calculate("ceil(-2.5)"), EPS);
    }

    @Test
    public void ceilTest2() {
        assertEquals(Math.ceil(2.5), calculate("ceil(2.5)"), EPS);
    }

    @Test
    public void ceilTest3() {
        assertEquals(Math.ceil(0), calculate("ceil(0)"), EPS);
    }

    @Test
    public void floorTest1() {
        assertEquals(Math.floor(-2.5), calculate("floor(-2.5)"), EPS);
    }

    @Test
    public void floorTest2() {
        assertEquals(Math.floor(2.5), calculate("floor(2.5)"), EPS);
    }

    @Test
    public void floorTest3() {
        assertEquals(Math.floor(0), calculate("floor(0)"), EPS);
    }

    @Test
    public void roundTest1() {
        assertEquals(Math.round(-2.5), calculate("round(-2.5)"), EPS);
    }

    @Test
    public void roundTest2() {
        assertEquals(Math.round(2.5), calculate("round(2.5)"), EPS);
    }

    @Test
    public void roundTest3() {
        assertEquals(Math.round(0), calculate("round(0)"), EPS);
    }

    @Test
    public void roundTest4() {
        assertEquals(Math.round(-1.4), calculate("round(-1.4)"), EPS);
    }

    @Test
    public void minTest1() {
        assertEquals(Math.min(-1, 1), calculate("min(-1,1)"), EPS);
    }

    @Test
    public void minTest2() {
        assertEquals(Math.min(2, 5), calculate("min(2,5)"), EPS);
    }

    @Test
    public void minTest3() {
        assertEquals(Math.min(0, 0), calculate("min(0, 0)"), EPS);
    }

    @Test
    public void maxTest1() {
        assertEquals(Math.max(-1, 1), calculate("max(-1,1)"), EPS);
    }

    @Test
    public void maxTest2() {
        assertEquals(Math.max(2, 5), calculate("max(2,5)"), EPS);
    }

    @Test
    public void maxTest3() {
        assertEquals(Math.max(0, 0), calculate("max(0, 0)"), EPS);
    }

    @Test
    public void randomTest1() {
        calculate("random()");
    }

    @Test
    public void signumTest1() {
        assertEquals(Math.signum(-0.5), calculate("signum(-0.5)"), EPS);
    }

    @Test
    public void signumTest2() {
        assertEquals(Math.signum(0.5), calculate("signum(0.5)"), EPS);
    }

    @Test
    public void signumTest3() {
        assertEquals(Math.signum(0), calculate("signum(0)"), EPS);
    }

    @Test
    public void factTest1() {
        assertEquals(factorial(10), calculate("fact(10)"), EPS);
    }

    @Test
    public void factTest2() {
        assertEquals(factorial(0), calculate("fact(0)"), EPS);
    }

    @Test
    public void factTest3() {
        assertEquals(factorial(20), calculate("fact(20)"), EPS);
    }

    @Test
    public void factTest4() {
        assertEquals(factorial(30), calculate("fact(30)"), EPS);
    }

    @Test
    public void modTest1() {
        assertEquals(mod(5, -2), calculate("mod(5, -2)"), EPS);
    }

    @Test
    public void modTest2() {
        assertEquals(2, calculate("mod(-7, 3)"), EPS);
    }

    @Test
    public void modTest3() {
        assertEquals(3, calculate("mod(13, 5)"), EPS);
    }

    @Test
    public void modTest4() {
        assertEquals(2, calculate("mod(-13,5)"), EPS);
    }

    @Test
    public void modTest5() {
        assertEquals(-1.0 % 0.0, calculate("mod(-1, 0)"), EPS);
    }
}
