package com.guliash.parser;

public class DoubleVariable {

    public String name;
    public double value;

    public DoubleVariable() {}

    public DoubleVariable(String name, double value) {
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
