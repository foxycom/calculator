package com.guliash.parser;

public class StringVariable {
    private String name;
    private String value;

    public StringVariable() {}

    public StringVariable(String name, String value) {
        this.setName(name);
        this.setValue(value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
