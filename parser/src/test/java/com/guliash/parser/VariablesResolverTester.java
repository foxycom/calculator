package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.evaluator.JavaEvaluator;
import com.guliash.parser.exceptions.CyclicVariablesDependency;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class VariablesResolverTester {

    @Test
    public void canFindDependencyForVariable() {
        List<Variable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(Angle.RAD);

        Variable x = new Variable("x", "x + y + z + d + w");
        Variable y = new Variable("y", "1");
        Variable z = new Variable("z", "1");
        Variable d = new Variable("d", "1");
        Variable w = new Variable("w", "1");
        variables.add(x);
        variables.add(y);
        variables.add(z);
        variables.add(d);
        variables.add(w);
        Set<Variable> need = new HashSet<>();
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
        List<Variable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(Angle.RAD);

        Variable x = new Variable("x", "y + z");
        Variable y = new Variable("y", "1");

        VariablesResolver resolver = new VariablesResolver(variables, evaluator);
        resolver.findDependencies(x);
    }

    @Test
    public void willNotTakeFunctionAsDependency() {
        List<Variable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(Angle.RAD);

        Variable x = new Variable("x", "y + sin(y)");
        Variable y = new Variable("y", "1");

        variables.add(x);
        variables.add(y);
        Set<Variable> need = new HashSet<>();
        need.add(y);

        VariablesResolver resolver = new VariablesResolver(variables, evaluator);
        assertEquals(need, resolver.findDependencies(x));
    }

    @Test
    public void willNotTakeConstant() {
        List<Variable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(Angle.RAD);

        Variable x = new Variable("x", "y + pi");
        Variable y = new Variable("y", "1");

        variables.add(x);
        variables.add(y);
        Set<Variable> need = new HashSet<>();
        need.add(y);

        VariablesResolver resolver = new VariablesResolver(variables, evaluator);
        assertEquals(need, resolver.findDependencies(x));
    }

    @Test(expected = CyclicVariablesDependency.class)
    public void checkThatCycleIsDetected() {
        List<Variable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(Angle.RAD);

        Variable x = new Variable("x", "y + z");
        Variable y = new Variable("y", "z");
        Variable z = new Variable("z", "x");

        variables.add(x);
        variables.add(y);
        variables.add(z);

        new VariablesResolver(variables, evaluator).resolveDependencies();
    }

    @Test
    public void willNotThrowForNotOrientedCycle() {
        List<Variable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(Angle.RAD);

        Variable x = new Variable("x", "sin(y) + abs(z)");
        Variable y = new Variable("y", "cos(f) * floor(g)");
        Variable z = new Variable("z", "asin(1) * ln(p) + sin(d)");
        Variable f = new Variable("f", "atan(2)");
        Variable g = new Variable("g", "pi * pow(1, z) + sin(d)");
        Variable p = new Variable("p", "e");
        Variable d = new Variable("d", "pi");

        variables.add(x); variables.add(y);
        variables.add(z); variables.add(f);
        variables.add(g); variables.add(p);
        variables.add(d);

        new VariablesResolver(variables, evaluator).resolveDependencies();
    }

    @Test(expected = CyclicVariablesDependency.class)
    public void willFailOnLoop() {
        List<Variable> variables = new ArrayList<>();
        Evaluator evaluator = new JavaEvaluator(Angle.RAD);
        Variable x = new Variable("x", "x");
        variables.add(x);
        new VariablesResolver(variables, evaluator).resolveDependencies();
    }

}
