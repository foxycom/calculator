package com.guliash.parser.evaluator.java;

import org.junit.Test;

import static java.lang.Math.*;
import static org.junit.Assert.assertEquals;

import static com.guliash.parser.Functions.*;

public class FunctionsTest extends BaseTester {

    @Test
    public void functionsTest1() {
        assertEquals(sin(log(2) * cos(3)), calculate("sin(ln(2) * cos(3))"), EPS);
    }

    @Test
    public void functionsTest2() {
        assertEquals(factorial(3 * ceil(5.5)), calculate("fact(3 * ceil(5.5))"), EPS);
    }

    @Test
    public void functionsTest3() {
        assertEquals(atan(3 * sinh(3) * cosh(5) * mod(-3, 1)),
                calculate("atan(3 * sinh(3) * cosh(5) * mod(-3, 1))"), EPS);
    }

    @Test
    public void functionCanHaveSpacesBetweenNameAndArgs() {
        assertEquals(sin  (cos (  55 )), calculate("sin  (cos (  55 ))"), EPS);
    }

    @Test
    public void functionWithMultipleArgsParsedCorrectly() {
        assertEquals(pow  ( 43 , 534 ) , calculate("pow  ( 43 , 534 ) "), EPS);
    }

    @Test(expected = Exception.class)
    public void functionArgsCannotBeginWithComma() {
        calculate("pow  (  , 43 , 534 ) ");
    }

    @Test
    public void functionCanHaveZeroArgs() {
        calculate("random   (      )");
    }
}

