package com.guliash.parser.check;

public class Preconditions {

    public static <T> T checkNotNull(T object) {
        if(object == null) {
            throw new NullPointerException();
        }
        return object;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static void throwIfEmpty(String str) {
        if(isEmpty(str)) {
            throw new NullPointerException();
        }
    }

}
