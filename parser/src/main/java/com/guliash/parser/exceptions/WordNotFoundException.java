package com.guliash.parser.exceptions;

import java.util.Locale;

public class WordNotFoundException extends RuntimeException {

    private String word;

    public WordNotFoundException(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String getMessage() {
        return String.format(Locale.ENGLISH, "Can't find word %s", word);
    }
}
