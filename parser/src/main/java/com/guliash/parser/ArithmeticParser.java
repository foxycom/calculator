package com.guliash.parser;

import com.guliash.parser.Stemmer.Lexeme;
import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.exceptions.ArithmeticParserException;

import java.util.ArrayList;
import java.util.List;

public class ArithmeticParser {

    private Evaluator evaluator;

    private List<Variable> variables;

    private Stemmer stemmer;

    private static final List<Variable> EMPTY_VARIABLES_LIST = new ArrayList<>(0);

    public ArithmeticParser(String s, List<? extends Variable> variables, Evaluator evaluator) {
        this.stemmer = new Stemmer(s);
        this.variables = new ArrayList<>(variables);
        this.evaluator = evaluator;
    }

    public double calculate() {
        stemmer.start();
        double res = expression();
        if(stemmer.getLexeme() != Lexeme.END_OF_LINE) {
            error();
        }
        return res;
    }

    private double expression() {
        double res = term();
        int prevSign;
        while(stemmer.getLexeme() == Lexeme.ADDITION || stemmer.getLexeme() == Lexeme.SUBTRACTION) {
            if(stemmer.getLexeme() == Lexeme.ADDITION) {
                prevSign = 1;
                stemmer.readLexeme();
            } else {
                prevSign = -1;
                stemmer.readLexeme();
            }
            res += prevSign * term();
        }
        return res;
    }

    private double term() {
        double res = factor();
        int prevSign;
        while(stemmer.getLexeme() == Lexeme.MULTIPLICATION || stemmer.getLexeme() == Lexeme.DIVISION) {
            if(stemmer.getLexeme() == Lexeme.MULTIPLICATION) {
                prevSign = 1;
                stemmer.readLexeme();
            } else {
                prevSign = -1;
                stemmer.readLexeme();
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
        if(stemmer.getLexeme() == Lexeme.NUMBER) {
            double res = stemmer.getNumber();
            stemmer.readLexeme();
            return res;
        } else if(stemmer.getLexeme() == Lexeme.ADDITION) {
            stemmer.readLexeme();
            return factor();
        } else if(stemmer.getLexeme() == Lexeme.SUBTRACTION) {
            stemmer.readLexeme();
            return -factor();
        } else if(stemmer.getLexeme() == Lexeme.OPEN_BRACKET) {
            stemmer.readLexeme();
            double res = expression();
            stemmer.verifyAndRead(Lexeme.CLOSE_BRACKET);
            return res;
        } else if(stemmer.getLexeme() == Lexeme.WORD) {
            String temp = stemmer.getWord();
            stemmer.readLexeme();
            if(stemmer.getLexeme() == Lexeme.OPEN_BRACKET) {
                List<Double> args = readFunctionArgs();
                return evaluator.evaluateFunction(temp, args);
            } else if(evaluator.hasConstant(temp)) {
                return evaluator.evaluateConstant(temp);
            } else if(hasVariable(temp)) {
                Variable variable = getVariable(temp);
                if (variable != null) {
                    return new ArithmeticParser(variable.value, EMPTY_VARIABLES_LIST, evaluator)
                            .calculate();
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
        stemmer.verifyAndRead(Lexeme.OPEN_BRACKET);
        List<Double> args = new ArrayList<>();
        while(stemmer.getLexeme() != Lexeme.CLOSE_BRACKET) {
            args.add(expression());
            if(stemmer.getLexeme() == Lexeme.COMMA) {
                stemmer.readLexeme();
            } else if(stemmer.getLexeme() != Lexeme.CLOSE_BRACKET) {
                error();
            }
        }
        stemmer.verifyAndRead(Lexeme.CLOSE_BRACKET);
        return args;
    }

    public boolean hasVariable(String word) {
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

    private void error() {
        throw new ArithmeticParserException("error");
    }

    private void error(String message) {
        throw new ArithmeticParserException(message);
    }

}