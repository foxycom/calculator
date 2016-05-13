package com.guliash.parser.functions;

public abstract class BaseFunction {

    public String name;

    public int numberOfArgs;

    public BaseFunction(String name, int numberOfArgs) {
        this.name = name;
        this.numberOfArgs = numberOfArgs;
    }

    public double calc(double...args) {
        check(args);
        return evaluate(args);
    }

    protected abstract double evaluate(double...args);

    public void check(double...args) throws IllegalArgumentException {
        if(args == null || args.length != numberOfArgs) {
            throw new IllegalArgumentException(String.format("Illegal arguments for %s function", name));
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof BaseFunction) && this.name.equals(((BaseFunction)o).name);
    }
}
