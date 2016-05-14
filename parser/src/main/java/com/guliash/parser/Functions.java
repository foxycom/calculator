package com.guliash.parser;

public class Functions {

    public static double factorial(double n) throws IllegalArgumentException {
        if(n < 0) {
            throw new IllegalArgumentException(String.format("Negative %f arg for factorial", n));
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

    public static double convertAngles(double angle, Angle from, Angle to) {
        if(from == Angle.DEG) {
            if(to == Angle.DEG) {
                return angle;
            } else if(to == Angle.RAD) {
                return angle * Math.PI / 180.0;
            } else if(to == Angle.GRAD) {
                return angle / 0.9;
            }
        } else if(from == Angle.RAD) {
            if(to == Angle.DEG) {
                return angle * 180.0 / Math.PI;
            } else if(to == Angle.RAD) {
                return angle;
            } else if(to == Angle.GRAD) {
                return angle / (Math.PI / 200);
            }
        } else if(from == Angle.GRAD) {
            if(to == Angle.DEG) {
                return 0.9 * angle;
            } else if(to == Angle.RAD) {
                return (Math.PI / 200) * angle;
            } else if(to == Angle.GRAD) {
                return angle;
            }
        }
        return 0.0;
    }

    /**
     * Acot in rads
     * @param val
     * @return acot in rads
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
