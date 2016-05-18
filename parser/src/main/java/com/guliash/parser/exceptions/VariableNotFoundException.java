package com.guliash.parser.exceptions;

import java.util.Locale;

public class VariableNotFoundException extends RuntimeException {

    private String name;

    public VariableNotFoundException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return String.format(Locale.US, "Variable %s was not found", name);
    }

    public String getName() {
        return name;
    }
}
