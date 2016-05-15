package com.guliash.parser.exceptions;

public class CyclicVariablesDependency extends RuntimeException {

    public CyclicVariablesDependency(String message) {
        super(message);
    }

}
