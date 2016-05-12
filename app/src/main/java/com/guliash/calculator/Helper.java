package com.guliash.calculator;

import android.text.TextUtils;

import com.guliash.parser.Variable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public static String getFormattedDate(long timestamp) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(timestamp);
    }

    public static ArrayList<Variable> stringValueVariablesToSimple(ArrayList<StringValueVariable> list) {
        ArrayList<Variable> variables = new ArrayList<>();
        for(StringValueVariable variable : list) {
            variables.add(new Variable(variable.name, Double.parseDouble(variable.strVal)));
        }
        return variables;
    }

    public static String variablesToString(List<StringValueVariable> variables) {
        String[] tokens = new String[variables.size()];
        for(int i = 0; i < variables.size(); i++) {
            tokens[i] = variableToString(variables.get(i));
        }
        return TextUtils.join("; ", tokens);
    }

    public static String variableToString(StringValueVariable variable) {
        return variable.name + " = " + variable.strVal;
    }
}
