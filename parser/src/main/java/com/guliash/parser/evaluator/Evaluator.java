package com.guliash.parser.evaluator;

import java.util.List;

public interface Evaluator {

    double evaluateFunction(String name, List<Double> args) throws IllegalArgumentException;

    double evaluateConstant(String name) throws IllegalArgumentException;

    boolean hasFunction(String name, List<Double> args);

    boolean hasConstant(String name);

}
