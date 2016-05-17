package com.guliash.parser;

public class StringVariable {
    public String name;
    public String value;

    public StringVariable() {}

    public StringVariable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof StringVariable) && ((StringVariable)o).name.equals(name);
    }

    @Override
    public String toString() {
        return name + " " + value;
    }
}
