package com.guliash.parser;

import java.util.Locale;

public class Functions {

    public static double factorial(double n) throws IllegalArgumentException {
        if(n < 0) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Negative %f arg for factorial", n));
        }
        double res = 1;
        for(double i = 2; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    public static double mod(double a, double b) {
        if(a < 0) {
            a = a % b;
            a += b;
        }
        return a % b;
    }

    public static double convertAngles(double angle, AngleUnits from, AngleUnits to) {
        if(from == AngleUnits.DEG) {
            if(to == AngleUnits.DEG) {
                return angle;
            } else if(to == AngleUnits.RAD) {
                return angle * Math.PI / 180.0;
            } else if(to == AngleUnits.GRAD) {
                return angle / 0.9;
            }
        } else if(from == AngleUnits.RAD) {
            if(to == AngleUnits.DEG) {
                return angle * 180.0 / Math.PI;
            } else if(to == AngleUnits.RAD) {
                return angle;
            } else if(to == AngleUnits.GRAD) {
                return angle / (Math.PI / 200);
            }
        } else if(from == AngleUnits.GRAD) {
            if(to == AngleUnits.DEG) {
                return 0.9 * angle;
            } else if(to == AngleUnits.RAD) {
                return (Math.PI / 200) * angle;
            } else if(to == AngleUnits.GRAD) {
                return angle;
            }
        }
        return 0.0;
    }

    /**
     * Calculates arccotangent in rads
     * @param val value
     * @return arccotangent in rads
     */
    public static double acot(double val) {
        return Math.PI / 2 - Math.atan(val);
    }

    public static double cot(double rads) {
        return 1.0 / Math.tan(rads);
    }

    public static double coth(double val) {
        return 1 / Math.tanh(val);
    }

    public static double logarithm(double x, double y) {
        return Math.log(y) / Math.log(x);
    }

}
