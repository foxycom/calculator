package com.guliash.parser;

import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.exceptions.ArithmeticParserException;
import com.guliash.parser.exceptions.VariableNotFoundException;
import com.guliash.parser.exceptions.WordNotFoundException;
import com.guliash.parser.stemmer.Stemmer;
import com.guliash.parser.stemmer.Stemmer.Lexeme;
import com.guliash.parser.stemmer.VerifyAssertionException;

import java.util.ArrayList;
import java.util.List;

public class ArithmeticParser {

    private Evaluator evaluator;

    private List<? extends DoubleVariable> variables;

    private Stemmer stemmer;

    private ArithmeticParser(String s, List<? extends DoubleVariable> variables, Evaluator evaluator) {
        this.stemmer = new Stemmer(s);
        this.variables = variables;
        this.evaluator = evaluator;
    }

    public static double calculate(String s, List<? extends StringVariable> variables, Evaluator evaluator) {
        VariablesResolver variablesResolver = new VariablesResolver(variables, evaluator);
        List<StringVariable> topologicallySortedVariables = variablesResolver.resolveDependencies();
        List<DoubleVariable> calculatedList = new ArrayList<>();
        for(StringVariable variable : topologicallySortedVariables) {
            ArithmeticParser parser = new ArithmeticParser(variable.getValue(), calculatedList, evaluator);
            DoubleVariable doubleVariable = new DoubleVariable(variable.getName(), parser.calculate());
            calculatedList.add(doubleVariable);
        }
        return new ArithmeticParser(s, calculatedList, evaluator).calculate();
    }

    private double calculate() {
        stemmer.start();
        double res = expression();
        if(stemmer.getLexeme() != Lexeme.END_OF_LINE) {
            throw  new VerifyAssertionException(Lexeme.END_OF_LINE, stemmer.getLexeme());
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
                DoubleVariable variable = getVariable(temp);
                if (variable != null) {
                   return variable.getValue();
                } else {
                    throw new VariableNotFoundException(temp);
                }
            } else {
                throw new WordNotFoundException(temp);
            }
        } else {
            //TODO?
            throw new ArithmeticParserException("error");
        }
    }

    private List<Double> readFunctionArgs() {
        stemmer.verifyAndRead(Lexeme.OPEN_BRACKET);
        List<Double> args = new ArrayList<>();
        while(stemmer.getLexeme() != Lexeme.CLOSE_BRACKET) {
            args.add(expression());
            if(stemmer.getLexeme() == Lexeme.COMMA) {
                stemmer.readLexeme();
            } else if(stemmer.getLexeme() != Lexeme.CLOSE_BRACKET) {
                throw new VerifyAssertionException(Lexeme.CLOSE_BRACKET, stemmer.getLexeme());
            }
        }
        stemmer.verifyAndRead(Lexeme.CLOSE_BRACKET);
        return args;
    }

    private boolean hasVariable(String word) {
        return getVariable(word) != null;
    }

    private DoubleVariable getVariable(String name) {
        for(DoubleVariable variable : variables) {
            if(variable.getName().equals(name)) {
                return variable;
            }
        }
        return null;
    }

}