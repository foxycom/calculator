package com.guliash.parser;

public class DoubleVariable {

    private String name;
    private double value;

    public DoubleVariable(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof StringVariable) && ((StringVariable) o).getName().equals(getName());
    }

    @Override
    public String toString() {
        return getName() + " " + getValue();
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
