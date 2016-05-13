package com.guliash.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PowTester extends BaseTester {

    @Test
    public void powTest1() {
        assertEquals(Math.pow(1, 2), calculate("pow(1,2)"), EPS);
    }

    @Test
    public void powTest2() {
        assertEquals(Math.pow(-2d, 2d), calculate("pow(-2,2)"), EPS);
    }

    @Test
    public void powTest3() {
        assertEquals(Math.pow(-2d, 2.999d), calculate("pow(-2,2.999)"), EPS);
    }

    @Test
    public void powTest4() {
        assertEquals(Math.pow(1000, 0), calculate("pow(1000, 0)"), EPS);
    }

    @Test
    public void sqrtTest1() {
        assertEquals(Math.sqrt(25), calculate("sqrt(25)"), EPS);
    }

    @Test
    public void sqrtTest2() {
        assertEquals(Math.sqrt(0), calculate("sqrt(0)"), EPS);
    }

    @Test
    public void sqrtTest3() {
        assertEquals(Math.sqrt(-1), calculate("sqrt(-1)"), EPS);
    }

    @Test
    public void expTest1() {
        assertEquals(Math.exp(3), calculate("exp(3)"), EPS);
    }

    @Test
    public void expTest2() {
        assertEquals(Math.exp(Math.E), calculate("exp(e)"), EPS);
    }

    @Test
    public void expTest3() {
        assertEquals(Math.exp(0), calculate("exp(0)"), EPS);
    }

    @Test
    public void expTest4() {
        assertEquals(Math.exp(-3), calculate("exp(-3)"), EPS);
    }
}
