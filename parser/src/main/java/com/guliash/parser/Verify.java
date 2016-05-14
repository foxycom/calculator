package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;

public class Verify {

    public static boolean variable(Variable variable) {
        String name = variable.name;
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

    public static boolean variableNameClashesWithConstants(Variable variable, Evaluator evaluator) {
        return evaluator.hasConstant(variable.name);
    }

    public static boolean isWordOnlyCharacter(char ch) {
        return Character.isLetter(ch) || ch == '$' || ch == '_';
    }

}
