package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.exceptions.CyclicVariablesDependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guliash.parser.Stemmer.Lexeme;

public class VariablesResolver {

    private Collection<Variable> variables;
    private Evaluator evaluator;

    enum State {
        ACTIVE, NOT_VISITED, VISITED
    }

    public VariablesResolver(Collection<Variable> variables, Evaluator evaluator) throws IllegalArgumentException {
        this.variables = variables;
        this.evaluator = evaluator;
        if(!Verify.checkVariablesUnique(variables)) {
            throw new IllegalArgumentException("variables are not unique");
        }
        if(Verify.variablesNamesClashWithConstants(variables, evaluator)) {
            throw new IllegalArgumentException("variables clashes with constants");
        }
    }

    /**
     * Returns list of variables in the topological order. First come variables, which has no
     * dependencies and so on
     * @return topologically sorted variables
     */
    public List<Variable> resolveDependencies() throws IllegalArgumentException {
        Map<Variable, Set<Variable>> dependencyGraph = new HashMap<>();
        for(Variable variable : variables) {
            dependencyGraph.put(variable, findDependencies(variable));
        }

        Map<Variable, State> stateMap = new HashMap<>();

        for(Variable variable : variables) {
            stateMap.put(variable, State.NOT_VISITED);
        }

        List<Variable> result = new ArrayList<>();
        for(Variable variable : variables) {
            if(stateMap.get(variable) == State.NOT_VISITED) {
                dfs(dependencyGraph, variable, stateMap, result);
            }
        }

        return result;

    }

    private void dfs(Map<Variable, Set<Variable>> graph, Variable current,
                     Map<Variable, State> stateMap, List<Variable> topologicallySortedList) {
        stateMap.put(current, State.ACTIVE);
        for(Variable adjVar : graph.get(current)) {
            State state = stateMap.get(adjVar);
            if(state == State.ACTIVE) {
                throw new CyclicVariablesDependency(String.format(
                        "Cyclic dependency found for variables %s and %s", current.name, adjVar.name));
            }
            if(state == State.NOT_VISITED) {
                dfs(graph, adjVar, stateMap, topologicallySortedList);
            }
        }
        stateMap.put(current, State.VISITED);
        topologicallySortedList.add(current);
    }

    /**
     * Find dependency for a variable
     * @param variable variable to find dependencies in
     * @return set of dependencies
     * @throws IllegalArgumentException if expression contains not correct lexemes or dependency
     * variable can't be found in {@link variables}
     */
    public Set<Variable> findDependencies(Variable variable) throws IllegalArgumentException {
        Stemmer stemmer = new Stemmer(variable.value);
        stemmer.start();
        Set<String> dependencies = new HashSet<>();
        while(stemmer.getLexeme() != Lexeme.END_OF_LINE) {
            if(stemmer.getLexeme() == Lexeme.WORD) {
                String wordName = stemmer.getWord();
                stemmer.readLexeme();
                if(stemmer.getLexeme() != Lexeme.OPEN_BRACKET && !evaluator.hasConstant(wordName)) {
                    dependencies.add(wordName);
                }
            } else {
                stemmer.readLexeme();
            }
        }
        Set<Variable> result = new HashSet<>();
        for(String name : dependencies) {
            boolean found = false;
            for(Variable var : variables) {
                if(var.name.equals(name)) {
                    result.add(var);
                    found = true;
                    break;
                }
            }
            if(!found) {
                throw new IllegalArgumentException(String.format("Can't find variable %s", name));
            }
        }
        return result;
    }

}
