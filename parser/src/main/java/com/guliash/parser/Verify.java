package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Verify {

    public static boolean variable(StringVariable variable) {
        String name = variable.getName();
        if(name == null || name.length() == 0) {
            return false;
        }
        if(!isWordOnlyCharacter(name.charAt(0))) {
            return false;
        }
        for(int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if(!isWordOnlyCharacter(ch) && !Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    public static boolean variableNameClashesWithConstants(StringVariable variable, Evaluator evaluator) {
        return evaluator.hasConstant(variable.getName());
    }

    public static boolean variablesNamesClashWithConstants(List<? extends StringVariable> variables, Evaluator evaluator) {
        for(StringVariable variable : variables) {
            if(variableNameClashesWithConstants(variable, evaluator)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWordOnlyCharacter(char ch) {
        return Character.isLetter(ch) || ch == '$' || ch == '_';
    }

    public static boolean checkVariablesUnique(List<? extends StringVariable> variables) {
        Set<StringVariable> variableSet = new HashSet<>();
        for(StringVariable variable : variables) {
            if(variableSet.contains(variable)) {
                return false;
            }
            variableSet.add(variable);
        }
        return true;
    }

}
