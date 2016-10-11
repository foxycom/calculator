package com.guliash.parser.stemmer;

import java.util.Locale;

public class VerifyAssertionException extends RuntimeException {

    private Stemmer.Lexeme expected;
    private Stemmer.Lexeme actual;

    public VerifyAssertionException(Stemmer.Lexeme expected, Stemmer.Lexeme actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String getMessage() {
        return String.format(Locale.ENGLISH, "Expected %s but was %s", expected, actual);
    }

    public Stemmer.Lexeme getExpected() {
        return expected;
    }

    public Stemmer.Lexeme getActual() {
        return actual;
    }
}
