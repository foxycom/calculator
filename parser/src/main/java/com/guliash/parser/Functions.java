package com.guliash.parser;

/**
 * Created by gulash on 19.12.15.
 */
public class Functions {

    public static double factorial(double n) {
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

    public static double atanToAcot(double atan) {
        return Math.PI / 2 - atan;
    }

    public static double cot(double rads) {
        return 1 / Math.tan(rads);
    }

}
