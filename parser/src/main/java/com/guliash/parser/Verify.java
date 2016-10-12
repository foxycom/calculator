package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.stemmer.Stemmer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Verify {

    public static class BadVariableException extends RuntimeException {

        private StringVariable variable;

        public BadVariableException(StringVariable variable) {
            this.variable = variable;
        }

        public StringVariable getVariable() {
            return variable;
        }

        @Override
        public String getMessage() {
            return String.format("Bad variable named '%s'", variable.getName());
        }
    }

    public static class VariableClashesWithConstantException extends RuntimeException {
        private StringVariable variable;

        public VariableClashesWithConstantException(StringVariable variable) {
            this.variable = variable;
        }

        public StringVariable getVariable() {
            return variable;
        }

        @Override
        public String getMessage() {
            return String.format("Variable '%s' clashes with constant", variable.getName());
        }
    }

    public static class NotUniqueVariablesException extends RuntimeException {
        private String collisionName;

        public NotUniqueVariablesException(String collisionName) {
            this.collisionName = collisionName;
        }

        public String getCollisionName() {
            return collisionName;
        }

        @Override
        public String getMessage() {
            return String.format("Two or more variables with the name '%s'", collisionName);
        }
    }

    public static void checkVariablesCorrectness(List<? extends StringVariable> variables)
            throws BadVariableException {
        for(StringVariable variable : variables) {
            if(!isCorrectVariable(variable)) {
                throw new BadVariableException(variable);
            }
        }
    }

    public static boolean isCorrectVariable(StringVariable variable) {
        String name = variable.getName();
        if (name == null || name.length() == 0) {
            return false;
        }
        if (!Stemmer.isWordOnlyCharacter(name.charAt(0))) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (!Stemmer.isWordOnlyCharacter(ch) && !Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    public static void checkVariablesDoNotClashWithConstants(
            List<? extends StringVariable> variables, Evaluator evaluator)
            throws VariableClashesWithConstantException {
        for(StringVariable variable : variables) {
            if(variableNameClashesWithConstants(variable, evaluator)) {
                throw new VariableClashesWithConstantException(variable);
            }
        }
    }

    public static boolean variableNameClashesWithConstants(StringVariable variable, Evaluator evaluator) {
        return evaluator.hasConstant(variable.getName());
    }

    public static void checkVariablesUnique(List<? extends StringVariable> variables)
            throws NotUniqueVariablesException {
        Set<StringVariable> variableSet = new HashSet<>();
        for (StringVariable variable : variables) {
            if (variableSet.contains(variable)) {
                throw new NotUniqueVariablesException(variable.getName());
            }
            variableSet.add(variable);
        }
    }

}
