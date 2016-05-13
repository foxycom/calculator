package com.guliash.parser.functions;

public class Sine extends BaseFunction {

    public Sine(String name, int numberOfArgs) {
        super(name, numberOfArgs);
    }

    @Override
    protected double evaluate(double... args) {
        return Math.sin(args[0]);
    }
}
