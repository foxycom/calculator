package com.guliash.parser.stemmer;

import java.util.Locale;

public class InvalidNumberException extends RuntimeException {

    private int errorPosition;

    public InvalidNumberException(int errorPosition) {
        this.errorPosition = errorPosition;
    }

    @Override
    public String getMessage() {
        return String.format(Locale.ENGLISH, "Symbol wasn't expected at pos %d", errorPosition);
    }

    public int getErrorPosition() {
        return errorPosition;
    }
}
