package com.guliash.parser;

public class Variable {
    public String name;
    public String value;

    public Variable() {}

    public Variable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Variable) && ((Variable)o).name.equals(name);
    }

    @Override
    public String toString() {
        return name + " " + value;
    }
}
