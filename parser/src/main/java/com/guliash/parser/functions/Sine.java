package com.guliash.parser.functions;

public class Sine extends BaseFunction<Double, Double> {

    public Sine(String name) {
        super(name);
    }

    @Override
    public Double calc(Double... args) {
        if(args.length > 1) {
            throw new RuntimeException("More than one parameter for sine function");
        }
        return Math.sin(args[0]);
    }
}
