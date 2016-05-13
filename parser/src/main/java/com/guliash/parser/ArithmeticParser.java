package com.guliash.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.guliash.parser.Functions.*;

public class ArithmeticParser {

    private String s;
    private int pos;
    private char ch;
    private double value;
    private Lexeme lexeme;
    private String word;
    private Angle angleUnits;
    private Set<String> functions;
    private Set<String> constants;
    private List<Variable> variables;

    public ArithmeticParser(String s, List<? extends Variable> variables, Angle angleUnits) {
        this.s = s;
        this.variables = new ArrayList<>(variables);
        this.angleUnits = angleUnits;
        initFunctions();
        initConstants();
    }

    public void initFunctions() {
        functions = new HashSet<>();
        //trigonometric
        functions.add("sin");
        functions.add("cos");
        functions.add("tan");
        functions.add("cot");
        functions.add("asin");
        functions.add("acos");
        functions.add("atan");
        functions.add("acot");
        functions.add("cosh");
        functions.add("sinh");
        functions.add("tanh");
        functions.add("coth");

        //pow
        functions.add("pow");
        functions.add("sqrt");
        functions.add("exp");

        //log
        functions.add("log");
        functions.add("ln");
        functions.add("log10");

        //math
        functions.add("abs");
        functions.add("floor");
        functions.add("ceil");
        functions.add("min");
        functions.add("max");
        functions.add("random");
        functions.add("round");
        functions.add("signum");
        functions.add("fact");
        functions.add("mod");
    }

    public void initConstants() {
        constants = new HashSet<>();
        constants.add("pi");
        constants.add("e");
    }

    public double calculate() {
        nextChar();
        nextLexeme();
        double res = expression();
        if(lexeme != Lexeme.END_OF_LINE) {
            error();
        }
        return res;
    }

    private void nextChar() {
        if(pos < s.length()) {
            ch = s.charAt(pos++);
        } else {
            ch = '\n';
        }
    }

    private void verifyAndRead(Lexeme lexeme) {
        if(lexeme == this.lexeme) {
            nextLexeme();
        } else {
            error();
        }
    }

    private void error() {
        throw new ArithmeticParserException();
    }

    private double expression() {
        double res = term();
        int prevSign;
        while(lexeme == Lexeme.ADDITION || lexeme == Lexeme.SUBTRACTION) {
            if(lexeme == Lexeme.ADDITION) {
                prevSign = 1;
                nextLexeme();
            } else {
                prevSign = -1;
                nextLexeme();
            }
            res += prevSign * term();
        }
        return res;
    }

    private double term() {
        double res = factor();
        int prevSign;
        while(lexeme == Lexeme.MULTIPLICATION || lexeme == Lexeme.DIVISION) {
            if(lexeme == Lexeme.MULTIPLICATION) {
                prevSign = 1;
                nextLexeme();
            } else {
                prevSign = -1;
                nextLexeme();
            }
            if(prevSign == 1) {
                res *= factor();
            } else {
                res /= factor();
            }
        }
        return res;
    }

    private double factor() {
        if(lexeme == Lexeme.NUMBER) {
            double res = value;
            nextLexeme();
            return res;
        } else if(lexeme == Lexeme.ADDITION) {
            nextLexeme();
            return factor();
        } else if(lexeme == Lexeme.SUBTRACTION) {
            nextLexeme();
            return -factor();
        } else if(lexeme == Lexeme.OPEN_BRACKET) {
            nextLexeme();
            double res = expression();
            verifyAndRead(Lexeme.CLOSE_BRACKET);
            return res;
        } else if(lexeme == Lexeme.WORD) {
            if(functions.contains(word)) {
                if(isTrigonometricFunction()) {
                    return readTrigonometricFunction();
                } else if(isPowFunction()) {
                    return readPowFunction();
                } else if(isLogFunction()) {
                    return readLogFunction();
                } else if(isMathFunction()) {
                    return readMathFunction();
                }
            } else if(constants.contains(word)) {
                return readConstant();
            } else if(isVariable()) {
                return readVariable();
            } else {
                error();
            }
        } else {
            error();
        }
        return 0.0;
    }

    public boolean isTrigonometricFunction() {
        return word.equals("sin") || word.equals("cos") || word.equals("tan") || word.equals("cot")
                || word.equals("asin") || word.equals("acos") || word.equals("atan") ||
                word.equals("acot") || word.equals("cosh") || word.equals("sinh") ||
                word.equals("tanh") || word.equals("coth") || word.equals("pi");
    }

    public boolean isPowFunction() {
        return word.equals("pow") || word.equals("sqrt") || word.equals("exp");
    }

    public boolean isLogFunction() {
        return word.equals("log") || word.equals("ln") || word.equals("log10");
    }

    public boolean isMathFunction() {
        return word.equals("abs") || word.equals("floor") || word.equals("ceil") ||
                word.equals("min") || word.equals("max") || word.equals("random") ||
                word.equals("round") || word.equals("signum") || word.equals("fact") ||
                word.equals("mod");
    }

    public boolean isVariable() {
        return getVariable(word) != null;
    }

    public double readConstant() {
        String constantName = word;
        nextLexeme();
        double val = 0.0;
        switch(constantName) {
            case "pi":
                val = Math.PI;
                break;
            case "e":
                val = Math.E;
                break;
        }
        return val;
    }

    public double readVariable() {
        Variable variable = getVariable(word);
        if(variable == null) {
            error();
        }
        nextLexeme();
        return variable.value;
    }

    public double readTrigonometricFunction() {
        String funcName = word;
        nextLexeme();
        if(funcName.equals("pi")) {
            return Math.PI;
        }
        verifyAndRead(Lexeme.OPEN_BRACKET);
        double val = expression();
        verifyAndRead(Lexeme.CLOSE_BRACKET);
        switch(funcName) {
            case "sin":
                val = Math.sin(convertAngles(val, angleUnits, Angle.RAD));
                break;
            case "cos":
                val = Math.cos(convertAngles(val, angleUnits, Angle.RAD));
                break;
            case "tan":
                val = Math.tan(convertAngles(val, angleUnits, Angle.RAD));
                break;
            case "cot":
                val = 1.0 / Math.tan(convertAngles(val, angleUnits, Angle.RAD));
                break;
            case "asin":
                val = convertAngles(Math.asin(val), Angle.RAD, angleUnits);
                break;
            case "acos":
                val = convertAngles(Math.acos(val), Angle.RAD, angleUnits);
                break;
            case "atan":
                val = convertAngles(Math.atan(val), Angle.RAD, angleUnits);
                break;
            case "acot":
                val = convertAngles(Math.PI / 2 - Math.atan(val), Angle.RAD, angleUnits);
                break;
            case "sinh":
                val = Math.sinh(val);
                break;
            case "cosh":
                val = Math.cosh(val);
                break;
            case "tanh":
                val = Math.tanh(val);
                break;
            case "coth":
                val = 1 / Math.tanh(val);
                break;
        }
        return val;

    }

    public double readPowFunction() {
        String funcName = word;
        nextLexeme();
        verifyAndRead(Lexeme.OPEN_BRACKET);
        double res = 0.0;
        switch(funcName) {
            case "pow":
                double x = expression();
                verifyAndRead(Lexeme.COMMA);
                double y = expression();
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                res = Math.pow(x, y);
                break;
            case "sqrt":
                res = Math.sqrt(expression());
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
            case "exp":
                res = Math.exp(expression());
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
        }
        return res;
    }

    public double readLogFunction() {
        String funcName = word;
        nextLexeme();
        verifyAndRead(Lexeme.OPEN_BRACKET);
        double res = 0.0;
        switch (funcName) {
            case "ln":
                res = Math.log(expression());
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
            case "log":
                double x = expression();
                verifyAndRead(Lexeme.COMMA);
                double y = expression();
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                res = Math.log(y) / Math.log(x);
                break;
            case "log10":
                res = Math.log10(expression());
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
        }
        return res;
    }

    public double readMathFunction() {
        String funcName = word;
        nextLexeme();
        verifyAndRead(Lexeme.OPEN_BRACKET);
        double res = 0.0;
        double x, y;
        switch (funcName) {
            case "abs":
                res = Math.abs(expression());
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
            case "floor":
                res = Math.floor(expression());
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
            case "ceil":
                res = Math.ceil(expression());
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
            case "min":
                x = expression();
                verifyAndRead(Lexeme.COMMA);
                y = expression();
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                res = Math.min(x, y);
                break;
            case "max":
                x = expression();
                verifyAndRead(Lexeme.COMMA);
                y = expression();
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                res = Math.max(x, y);
                break;
            case "random":
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                res = Math.random();
                break;
            case "round":
                res = Math.round(expression());
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
            case "signum":
                res = Math.signum(expression());
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
            case "fact":
                x = expression();
                if(x < 0) {
                    error();
                }
                res = Functions.factorial(x);
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                break;
            case "mod":
                x = expression();
                verifyAndRead(Lexeme.COMMA);
                y = expression();
                verifyAndRead(Lexeme.CLOSE_BRACKET);
                res = Functions.mod(x, y);
                break;
        }
        return res;
    }

    private Variable getVariable(String name) {
        for(Variable variable : variables) {
            if(variable.name.equals(name)) {
                return variable;
            }
        }
        return null;
    }

    private void number() {
        if(!Character.isDigit(ch)) {
            error();
        }
        double res = 0;
        while(Character.isDigit(ch)) {
            res *= 10;
            res += Character.getNumericValue(ch);
            nextChar();
        }
        if(ch == '.') {
            nextChar();
            double pow = 0.1;
            while(Character.isDigit(ch)) {
                res += Character.getNumericValue(ch) * pow;
                pow *= 0.1;
                nextChar();
            }
            if(ch == 'e' || ch == 'E') {
                res *= exponent();
            }
        } else if(ch == 'e' || ch == 'E') {
            res *= exponent();
        }
        value = res;
    }

    public double exponent() {
        if(ch != 'e' && ch != 'E') {
            error();
        }
        nextChar();
        double m = 10;
        int pow2 = 0;
        double res = 1;
        if(ch == '-') {
            m = 0.1;
            nextChar();
        } else if(ch == '+') {
            m = 10;
            nextChar();
        } else if(!Character.isDigit(ch)) {
            error();
        }
        if(!Character.isDigit(ch)) {
            error();
        }
        while(Character.isDigit(ch)) {
            pow2 *= 10;
            pow2 += Character.getNumericValue(ch);
            nextChar();
        }
        res *= Math.pow(m, pow2);
        return res;
    }

    private void nextLexeme() {
        while(ch == ' ') {
            nextChar();
        }
        if(ch == '*') {
            lexeme = Lexeme.MULTIPLICATION;
            nextChar();
        } else if(ch == '/') {
            lexeme = Lexeme.DIVISION;
            nextChar();
        } else if(ch == '+') {
            lexeme = Lexeme.ADDITION;
            nextChar();
        } else if(ch == '-') {
            lexeme = Lexeme.SUBTRACTION;
            nextChar();
        } else if(Character.isDigit(ch)) {
            lexeme = Lexeme.NUMBER;
            number();
        } else if(ch == '(') {
            lexeme = Lexeme.OPEN_BRACKET;
            nextChar();
        } else if(ch == ')') {
            lexeme = Lexeme.CLOSE_BRACKET;
            nextChar();
        } else if(ch == ',') {
            lexeme = Lexeme.COMMA;
            nextChar();
        } else if(isWordOnlyCharacter(ch)) {
            lexeme = Lexeme.WORD;
            word = "";
            while(isWordOnlyCharacter(ch) || Character.isDigit(ch)) {
                word += ch;
                nextChar();
            }
        } else if(ch == '\n') {
            lexeme = Lexeme.END_OF_LINE;
        } else {
            error();
        }
    }

    public static boolean isWordOnlyCharacter(char ch) {
        return Character.isLetter(ch) || ch == '$' || ch == '_';
    }

    enum Lexeme {
        NUMBER,
        DIVISION,
        MULTIPLICATION,
        ADDITION,
        SUBTRACTION,
        WORD,
        END_OF_LINE,
        OPEN_BRACKET,
        CLOSE_BRACKET,
        COMMA;
    }

    public class ArithmeticParserException extends RuntimeException {

    }


}