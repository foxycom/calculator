package com.guliash.parser.stemmer;

import java.util.Locale;

public class InvalidNumberException extends RuntimeException {

    /**
     * Error position
     */
    private int position;

    public InvalidNumberException(int position) {
        this.position = position;
    }

    @Override
    public String getMessage() {
        return String.format(Locale.US, "Symbol wasn't expected at pos %d", position);
    }

    public int getPosition() {
        return position;
    }
}
