package com.guliash.calculator.calculator;

import android.support.annotation.Nullable;

import com.guliash.parser.StringVariable;

import java.util.List;

public interface Calculator {

    class CalculateResult {
        private double value;
        private boolean success;
        private String errorMessage;

        public CalculateResult(double value, boolean success, @Nullable String errorMessage) {
            this.value = value;
            this.success = success;
            this.errorMessage = errorMessage;
        }

        public static CalculateResult buildErrorResult(String message) {
            return new CalculateResult(0d, false, message);
        }

        public static CalculateResult buildSuccessResult(double result) {
            return new CalculateResult(result, true, null);
        }

        public double getValue() {
            return value;
        }

        public boolean isSuccess() {
            return success;
        }

        @Nullable public String getErrorMessage() {
            return errorMessage;
        }
    }

    CalculateResult calculate(String expression, List<? extends StringVariable> variables);

}
