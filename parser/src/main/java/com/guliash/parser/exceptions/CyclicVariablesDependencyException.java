package com.guliash.parser.exceptions;

public class CyclicVariablesDependencyException extends RuntimeException {

    public String firstName, secondName;

    public CyclicVariablesDependencyException(String firstName, String  secondName) {
        super();
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @Override
    public String getMessage() {
        return String.format("Cyclic dependency found for variables %s and %s", firstName, secondName);
    }
}
