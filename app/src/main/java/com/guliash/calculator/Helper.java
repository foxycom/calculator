package com.guliash.calculator;

import android.text.TextUtils;

import java.text.DateFormat;
import java.util.List;

public class Helper {

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public static String getFormattedDate(long timestamp) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(timestamp);
    }

    public static String variablesToString(List<StringVariableWrapper> variables) {
        String[] tokens = new String[variables.size()];
        for(int i = 0; i < variables.size(); i++) {
            tokens[i] = variableToString(variables.get(i));
        }
        return TextUtils.join("; ", tokens);
    }

    public static String variableToString(StringVariableWrapper variable) {
        return variable.name + " = " + variable.value;
    }
}
