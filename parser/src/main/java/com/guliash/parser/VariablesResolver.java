package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.exceptions.VariableNotFoundException;
import com.guliash.parser.stemmer.Stemmer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guliash.parser.stemmer.Stemmer.Lexeme;

public class VariablesResolver {

    private List<? extends StringVariable> variables;
    private Evaluator evaluator;

    public static class CyclicVariablesDependencyException extends RuntimeException {

        private String firstName, secondName;

        public CyclicVariablesDependencyException(String firstName, String secondName) {
            this.firstName = firstName;
            this.secondName = secondName;
        }

        @Override
        public String getMessage() {
            return String.format("Cyclic dependency found for variables %s and %s", firstName, secondName);
        }

        public String getFirstName() {
            return firstName;
        }

        public String getSecondName() {
            return secondName;
        }
    }


    private enum State {
        ACTIVE, NOT_VISITED, VISITED
    }

    public VariablesResolver(List<? extends StringVariable> variables, Evaluator evaluator)
            throws Verify.BadVariableException,
            Verify.VariableClashesWithConstantException,
            Verify.NotUniqueVariablesException {

        this.variables = variables;
        this.evaluator = evaluator;

        Verify.checkVariablesCorrectness(variables);
        Verify.checkVariablesDoNotClashWithConstants(variables, evaluator);
        Verify.checkVariablesUnique(variables);
    }

    /**
     * Returns list of variables in the topological order. First come variables, which has no
     * dependencies and so on.
     *
     * @return topologically sorted variables
     */
    public List<StringVariable> resolveDependencies() throws VariableNotFoundException,
            CyclicVariablesDependencyException {
        Map<StringVariable, Set<StringVariable>> dependencyGraph = new HashMap<>();
        for (StringVariable variable : variables) {
            dependencyGraph.put(variable, findDependencies(variable));
        }

        Map<StringVariable, State> stateMap = new HashMap<>();

        for (StringVariable variable : variables) {
            stateMap.put(variable, State.NOT_VISITED);
        }

        List<StringVariable> result = new ArrayList<>();
        for (StringVariable variable : variables) {
            if (stateMap.get(variable) == State.NOT_VISITED) {
                dfs(dependencyGraph, variable, stateMap, result);
            }
        }

        return result;

    }

    private void dfs(Map<StringVariable, Set<StringVariable>> graph, StringVariable current,
                     Map<StringVariable, State> stateMap, List<StringVariable> topologicallySortedList)
            throws CyclicVariablesDependencyException {
        stateMap.put(current, State.ACTIVE);
        for (StringVariable adjVar : graph.get(current)) {
            State state = stateMap.get(adjVar);
            if (state == State.ACTIVE) {
                throw new CyclicVariablesDependencyException(current.getName(), adjVar.getName());
            }
            if (state == State.NOT_VISITED) {
                dfs(graph, adjVar, stateMap, topologicallySortedList);
            }
        }
        stateMap.put(current, State.VISITED);
        topologicallySortedList.add(current);
    }

    /**
     * Find dependency for the variable
     *
     * @param variable variable to find dependencies in
     * @return set of dependencies
     * @throws VariableNotFoundException if expression contains not correct lexemes or dependency for the
     *                                  variable can't be found in {@link VariablesResolver#variables}
     */
    public Set<StringVariable> findDependencies(StringVariable variable) throws VariableNotFoundException {
        Stemmer stemmer = new Stemmer(variable.getValue());
        stemmer.start();
        Set<String> dependencies = new HashSet<>();
        while (stemmer.getLexeme() != Lexeme.END_OF_LINE) {
            if (stemmer.getLexeme() == Lexeme.WORD) {
                String word = stemmer.getWord();
                stemmer.readLexeme();
                if (stemmer.getLexeme() != Lexeme.OPEN_BRACKET && !evaluator.hasConstant(word)) {
                    dependencies.add(word);
                }
            } else {
                stemmer.readLexeme();
            }
        }
        Set<StringVariable> result = new HashSet<>();
        for (String name : dependencies) {
            boolean found = false;
            for (StringVariable var : variables) {
                if (var.getName().equals(name)) {
                    result.add(var);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new VariableNotFoundException(name);
            }
        }
        return result;
    }

}
