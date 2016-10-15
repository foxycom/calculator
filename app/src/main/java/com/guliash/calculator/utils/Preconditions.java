package com.guliash.calculator.utils;

import java.util.Locale;

public class Preconditions {

    public static void assertEquals(int x, int y) {
        if(x != y) {
            throw new AssertionError(String.format(Locale.ENGLISH, "%d != %d", x, y));
        }
    }

    public static void assertNotEquals(int x, int y) {
        if(x == y) {
            throw new AssertionError(String.format(Locale.ENGLISH, "%d == %d", x, y));
        }
    }

}
