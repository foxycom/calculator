package com.guliash.parser.evaluator;

import com.guliash.parser.Angle;
import com.guliash.parser.Functions;

import java.util.List;

public class JavaEvaluator implements Evaluator {

    private Angle angleUnits;

    public JavaEvaluator(Angle angleUnits) {
        this.angleUnits = angleUnits;
    }

    enum Function {
        SIN("sin", 1),
        COS("cos", 1),
        TAN("tan", 1),
        COT("cot", 1),
        ASIN("asin", 1),
        ACOS("acos", 1),
        ATAN("atan", 1),
        ACOT("acot", 1),
        SINH("sinh", 1),
        COSH("cosh", 1),
        TANH("tanh", 1),
        COTH("coth", 1),

        POW("pow", 2),
        SQRT("sqrt", 1),
        EXP("exp", 1),

        LN("ln", 1),
        LOG("log", 2),
        LOG10("log10", 1),

        ABS("abs", 1),
        FLOOR("floor", 1),
        CEIL("ceil", 1),
        MIN("min", 2),
        MAX("max", 2),
        RANDOM("random", 0),
        ROUND("round", 1),
        SIGNUM("signum", 1),
        FACT("fact", 1),
        MOD("mod", 2);

        private String name;
        private int cntOfArgs;

        Function(String name, int cntOfArgs) {
            this.name = name;
            this.cntOfArgs = cntOfArgs;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    enum Constant {

        PI("pi"), E("e");

        String name;

        Constant(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public double evaluateFunction(String name, List<Double> args) throws IllegalArgumentException {

        if(name == null || args == null) {
            throw new IllegalArgumentException("Illegal arguments for evaluateFunction");
        }

        for(Function function : Function.values()) {
            if(function.name.equals(name) && args.size() == function.cntOfArgs) {
                return evaluateFunction(function, args);
            }
        }

        throw new IllegalArgumentException(String.format("Function %s not found with %d arguments",
                name, args.size()));

    }

    public double evaluateFunction(Function function, List<Double> args) throws IllegalArgumentException {
        switch (function) {
            case SIN:
                return Math.sin(Functions.convertAngles(args.get(0), angleUnits, Angle.RAD));
            case COS:
                return Math.cos(Functions.convertAngles(args.get(0), angleUnits, Angle.RAD));
            case TAN:
                return Math.tan(Functions.convertAngles(args.get(0), angleUnits, Angle.RAD));
            case COT:
                return Functions.cot(Functions.convertAngles(args.get(0), angleUnits, Angle.RAD));
            case ASIN:
                return Functions.convertAngles(Math.asin(args.get(0)), Angle.RAD, angleUnits);
            case ACOS:
                return Functions.convertAngles(Math.acos(args.get(0)), Angle.RAD, angleUnits);
            case ATAN:
                return Functions.convertAngles(Math.atan(args.get(0)), Angle.RAD, angleUnits);
            case ACOT:
                return Functions.convertAngles(Functions.acot(args.get(0)), Angle.RAD, angleUnits);
            case SINH:
                return Math.sinh(args.get(0));
            case COSH:
                return Math.cosh(args.get(0));
            case TANH:
                return Math.tanh(args.get(0));
            case COTH:
                return Functions.coth(args.get(0));

            case POW:
                return Math.pow(args.get(0), args.get(1));
            case SQRT:
                return Math.sqrt(args.get(0));
            case EXP:
                return Math.exp(args.get(0));

            case LN:
                return Math.log(args.get(0));
            case LOG:
                return Functions.logarithm(args.get(0), args.get(1));
            case LOG10:
                return Math.log10(args.get(0));

            case ABS:
                return Math.abs(args.get(0));
            case FLOOR:
                return Math.floor(args.get(0));
            case CEIL:
                return Math.ceil(args.get(0));
            case MIN:
                return Math.min(args.get(0), args.get(1));
            case MAX:
                return Math.max(args.get(0), args.get(1));
            case RANDOM:
                return Math.random();
            case ROUND:
                return Math.round(args.get(0));
            case SIGNUM:
                return Math.signum(args.get(0));
            case FACT:
                return Functions.factorial(args.get(0));
            case MOD:
                return Functions.mod(args.get(0), args.get(1));
        }
        throw new IllegalArgumentException(String.format("Missed switch branch for function %s", function.name));
    }



    @Override
    public double evaluateConstant(String name) throws IllegalArgumentException {
        if(name == null) {
            throw new IllegalArgumentException("Illegal argument for evaluateConstant");
        }
        for(Constant constant : Constant.values()) {
            if(constant.name.equals(name)) {
                return evaluateConstant(constant);
            }
        }
        throw new IllegalArgumentException(String.format("Can't find %s constant", name));
    }

    @Override
    public boolean hasFunction(String name, List<Double> args) {
        for(Function function : Function.values()) {
            if(function.name.equals(name) && function.cntOfArgs == args.size()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasConstant(String name) {
        for(Constant constant : Constant.values()) {
            if(constant.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public double evaluateConstant(Constant constant) throws IllegalArgumentException {
        switch(constant) {
            case PI:
                return Math.PI;
            case E:
                return Math.E;
        }
        throw new IllegalArgumentException(String.format("Missed switch branch for constant %s", constant.name));
    }
}
