package com.guliash.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static com.guliash.parser.Functions.*;

public class FunctionsTest extends BaseTester {

    @Test
    public void functionsTest1() {
        assertEquals(Math.sin(Math.log(2) * Math.cos(3)), calculate("sin(ln(2) * cos(3))"), EPS);
    }

    @Test
    public void functionsTest2() {
        assertEquals(factorial(3 * Math.ceil(5.5)), calculate("fact(3 * ceil(5.5))"), EPS);
    }

    @Test
    public void functionsTest3() {
        assertEquals(Math.atan(3 * Math.sinh(3) * Math.cosh(5) * mod(-3, 1)),
                calculate("atan(3 * sinh(3) * cosh(5) * mod(-3, 1))"), EPS);
    }
}

