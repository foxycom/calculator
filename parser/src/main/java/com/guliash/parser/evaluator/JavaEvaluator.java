package com.guliash.parser.evaluator;

import com.guliash.parser.Angle;
import com.guliash.parser.Functions;

import java.util.List;

import static com.guliash.parser.Functions.convertAngles;
import static java.lang.Math.E;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.cosh;
import static java.lang.Math.exp;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static java.lang.Math.signum;
import static java.lang.Math.sin;
import static java.lang.Math.sinh;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static java.lang.Math.tanh;

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
                return sin(convertAngles(args.get(0), angleUnits, Angle.RAD));
            case COS:
                return cos(convertAngles(args.get(0), angleUnits, Angle.RAD));
            case TAN:
                return tan(convertAngles(args.get(0), angleUnits, Angle.RAD));
            case COT:
                return 1.0 / tan(convertAngles(args.get(0), angleUnits, Angle.RAD));
            case ASIN:
                return convertAngles(asin(args.get(0)), Angle.RAD, angleUnits);
            case ACOS:
                return convertAngles(acos(args.get(0)), Angle.RAD, angleUnits);
            case ATAN:
                return convertAngles(atan(args.get(0)), Angle.RAD, angleUnits);
            case ACOT:
                return convertAngles(PI / 2 - atan(args.get(0)), Angle.RAD, angleUnits);
            case SINH:
                return sinh(args.get(0));
            case COSH:
                return cosh(args.get(0));
            case TANH:
                return tanh(args.get(0));
            case COTH:
                return 1 / tanh(args.get(0));

            case POW:
                return pow(args.get(0), args.get(1));
            case SQRT:
                return sqrt(args.get(0));
            case EXP:
                return exp(args.get(0));

            case LN:
                return log(args.get(0));
            case LOG:
                return log(args.get(1)) / log(args.get(0));
            case LOG10:
                return log10(args.get(0));

            case ABS:
                return abs(args.get(0));
            case FLOOR:
                return floor(args.get(0));
            case CEIL:
                return ceil(args.get(0));
            case MIN:
                return min(args.get(0), args.get(1));
            case MAX:
                return max(args.get(0), args.get(1));
            case RANDOM:
                return random();
            case ROUND:
                return round(args.get(0));
            case SIGNUM:
                return signum(args.get(0));
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
    public boolean hasFunction(String name, List<Double> args) throws IllegalArgumentException {
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
                return PI;
            case E:
                return E;
        }
        throw new IllegalArgumentException(String.format("Missed switch branch for constant %s", constant.name));
    }
}
