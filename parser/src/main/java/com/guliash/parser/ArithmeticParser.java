package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;

import java.util.ArrayList;
import java.util.List;

public class ArithmeticParser {

    private String s;
    private int pos;
    private char ch;
    private double value;
    private Lexeme lexeme;
    private String word;

    private Evaluator evaluator;

    private List<Variable> variables;

    public ArithmeticParser(String s, List<? extends Variable> variables, Evaluator evaluator) {
        this.s = s;
        this.variables = new ArrayList<>(variables);
        this.evaluator = evaluator;
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
        throw new ArithmeticParserException("error");
    }

    private void error(String message) {
        throw new ArithmeticParserException(message);
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
            String temp = word;
            nextLexeme();
            if(lexeme == Lexeme.OPEN_BRACKET) {
                List<Double> args = readFunctionArgs();
                return evaluator.evaluateFunction(temp, args);
            } else if(evaluator.hasConstant(temp)) {
                return evaluator.evaluateConstant(temp);
            } else if(isVariable(temp)) {
                Variable variable = getVariable(temp);
                if (variable != null) {
                    return variable.value;
                } else {
                    error(String.format("Can't find variable %s", temp));
                }
            } else {
                error(String.format("Can't find nor variable nor function with %s name", temp));
            }
        } else {
            error();
        }
        return 0.0;
    }

    public List<Double> readFunctionArgs() {
        verifyAndRead(Lexeme.OPEN_BRACKET);
        List<Double> args = new ArrayList<>();
        while(lexeme  != Lexeme.CLOSE_BRACKET) {
            args.add(expression());
            if(lexeme == Lexeme.COMMA) {
                nextLexeme();
            } else if(lexeme != Lexeme.CLOSE_BRACKET) {
                error();
            }
        }
        verifyAndRead(Lexeme.CLOSE_BRACKET);
        return args;
    }

    public boolean isVariable(String word) {
        return getVariable(word) != null;
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

        public ArithmeticParserException(String message) {
            super(message);
        }
    }


}