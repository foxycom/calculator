package com.guliash.parser.evaluator.java;

import com.guliash.parser.Angle;

import org.junit.Test;

import static com.guliash.parser.Functions.convertAngles;
import static org.junit.Assert.assertEquals;

public class AngleTester extends BaseTester {

    @Test
    public void angleTest1() {
        assertEquals(Math.cos(convertAngles(45, Angle.DEG, Angle.RAD)),
                calculate("cos(45)", Angle.DEG), EPS);
    }

    @Test
    public void angleTest2() {
        assertEquals(Math.sqrt(2), calculate("cos(45) + sin(45)", Angle.DEG), EPS);
    }

    @Test
    public void angleTest3() {
        assertEquals(-1, calculate("cos(pi)", Angle.RAD), EPS);
    }

    @Test
    public void angleTest4() {
        assertEquals(Math.cos(convertAngles(40.5, Angle.GRAD, Angle.RAD)),
                calculate("cos(40.5)", Angle.GRAD), EPS);
    }
}
