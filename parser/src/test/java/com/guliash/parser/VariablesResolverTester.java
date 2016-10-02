package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.evaluator.JavaEvaluator;
import com.guliash.parser.exceptions.CyclicVariablesDependencyException;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class VariablesResolverTester {

    @Test
    public void canFindDependencyForVariable() {
        List<StringVariable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(AngleUnits.RAD);

        StringVariable x = new StringVariable("x", "x + y + z + d + w");
        StringVariable y = new StringVariable("y", "1");
        StringVariable z = new StringVariable("z", "1");
        StringVariable d = new StringVariable("d", "1");
        StringVariable w = new StringVariable("w", "1");
        variables.add(x);
        variables.add(y);
        variables.add(z);
        variables.add(d);
        variables.add(w);
        Set<StringVariable> need = new HashSet<>();
        need.add(x);
        need.add(y);
        need.add(z);
        need.add(d);
        need.add(w);

        VariablesResolver resolver = new VariablesResolver(variables, evaluator);
        assertEquals(need, resolver.findDependencies(x));
    }

    @Test(expected = IllegalArgumentException.class)
    public void willThrowExceptionIfCannotFindDependencyForVariable() {
        List<StringVariable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(AngleUnits.RAD);

        StringVariable x = new StringVariable("x", "y + z");
        StringVariable y = new StringVariable("y", "1");
        variables.add(x);
        variables.add(y);

        VariablesResolver resolver = new VariablesResolver(variables, evaluator);
        resolver.findDependencies(x);
    }

    @Test
    public void willNotTakeFunctionAsDependency() {
        List<StringVariable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(AngleUnits.RAD);

        StringVariable x = new StringVariable("x", "y + sin(y)");
        StringVariable y = new StringVariable("y", "1");

        variables.add(x);
        variables.add(y);
        Set<StringVariable> need = new HashSet<>();
        need.add(y);

        VariablesResolver resolver = new VariablesResolver(variables, evaluator);
        assertEquals(need, resolver.findDependencies(x));
    }

    @Test
    public void willNotTakeConstant() {
        List<StringVariable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(AngleUnits.RAD);

        StringVariable x = new StringVariable("x", "y + pi");
        StringVariable y = new StringVariable("y", "1");

        variables.add(x);
        variables.add(y);
        Set<StringVariable> need = new HashSet<>();
        need.add(y);

        VariablesResolver resolver = new VariablesResolver(variables, evaluator);
        assertEquals(need, resolver.findDependencies(x));
    }

    @Test(expected = CyclicVariablesDependencyException.class)
    public void checkThatCycleIsDetected() {
        List<StringVariable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(AngleUnits.RAD);

        StringVariable x = new StringVariable("x", "y + z");
        StringVariable y = new StringVariable("y", "z");
        StringVariable z = new StringVariable("z", "x");

        variables.add(x);
        variables.add(y);
        variables.add(z);

        new VariablesResolver(variables, evaluator).resolveDependencies();
    }

    @Test
    public void willNotThrowForNotOrientedCycle() {
        List<StringVariable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(AngleUnits.RAD);

        StringVariable x = new StringVariable("x", "sin(y) + abs(z)");
        StringVariable y = new StringVariable("y", "cos(f) * floor(g)");
        StringVariable z = new StringVariable("z", "asin(1) * ln(p) + sin(d)");
        StringVariable f = new StringVariable("f", "atan(2)");
        StringVariable g = new StringVariable("g", "pi * pow(1, z) + sin(d)");
        StringVariable p = new StringVariable("p", "e");
        StringVariable d = new StringVariable("d", "pi");

        variables.add(x); variables.add(y);
        variables.add(z); variables.add(f);
        variables.add(g); variables.add(p);
        variables.add(d);

        new VariablesResolver(variables, evaluator).resolveDependencies();
    }

    @Test(expected = CyclicVariablesDependencyException.class)
    public void willFailOnLoop() {
        List<StringVariable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(AngleUnits.RAD);
        StringVariable x = new StringVariable("x", "x");
        variables.add(x);
        new VariablesResolver(variables, evaluator).resolveDependencies();
    }

}
