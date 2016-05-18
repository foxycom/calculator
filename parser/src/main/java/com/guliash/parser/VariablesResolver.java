package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.exceptions.CyclicVariablesDependencyException;
import com.guliash.parser.stemmer.Stemmer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guliash.parser.stemmer.Stemmer.Lexeme;

public class VariablesResolver {

    private List<StringVariable> variables;
    private Evaluator evaluator;

    enum State {
        ACTIVE, NOT_VISITED, VISITED
    }

    public VariablesResolver(List<StringVariable> variables, Evaluator evaluator) throws IllegalArgumentException {
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
    public List<StringVariable> resolveDependencies() throws IllegalArgumentException {
        Map<StringVariable, Set<StringVariable>> dependencyGraph = new HashMap<>();
        for(StringVariable variable : variables) {
            dependencyGraph.put(variable, findDependencies(variable));
        }

        Map<StringVariable, State> stateMap = new HashMap<>();

        for(StringVariable variable : variables) {
            stateMap.put(variable, State.NOT_VISITED);
        }

        List<StringVariable> result = new ArrayList<>();
        for(StringVariable variable : variables) {
            if(stateMap.get(variable) == State.NOT_VISITED) {
                dfs(dependencyGraph, variable, stateMap, result);
            }
        }

        return result;

    }

    private void dfs(Map<StringVariable, Set<StringVariable>> graph, StringVariable current,
                     Map<StringVariable, State> stateMap, List<StringVariable> topologicallySortedList) {
        stateMap.put(current, State.ACTIVE);
        for(StringVariable adjVar : graph.get(current)) {
            State state = stateMap.get(adjVar);
            if(state == State.ACTIVE) {
                throw new CyclicVariablesDependencyException(current.name, adjVar.name);
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
    public Set<StringVariable> findDependencies(StringVariable variable) throws IllegalArgumentException {
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
        Set<StringVariable> result = new HashSet<>();
        for(String name : dependencies) {
            boolean found = false;
            for(StringVariable var : variables) {
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
