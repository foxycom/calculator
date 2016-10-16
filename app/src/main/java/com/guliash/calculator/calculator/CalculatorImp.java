package com.guliash.calculator.calculator;

import android.content.Context;

import com.guliash.calculator.R;
import com.guliash.calculator.state.AppSettings;
import com.guliash.parser.StringVariable;
import com.guliash.parser.VariablesResolver;
import com.guliash.parser.Verify;
import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.evaluator.JavaEvaluator;
import com.guliash.parser.exceptions.VariableNotFoundException;
import com.guliash.parser.exceptions.WordNotFoundException;
import com.guliash.parser.parser.Parser;
import com.guliash.parser.stemmer.InvalidNumberException;

import java.util.List;

public class CalculatorImp implements Calculator {

    private AppSettings appSettings;
    private Context context;

    public CalculatorImp(Context context, AppSettings appSettings) {
        this.appSettings = appSettings;
        this.context = context;
    }

    @Override
    public CalculateResult calculate(String expression, List<? extends StringVariable> variables) {
        Evaluator evaluator = new JavaEvaluator(appSettings.getAngleUnits());

        try {
            double result = Parser.calculate(expression, variables, evaluator);
            return CalculateResult.buildSuccessResult(result);
        } catch (Verify.BadVariableException e) {
            return CalculateResult.buildErrorResult(context.getString(
                    R.string.variable_name_not_correct, e.getVariable().getName()));
        } catch (Verify.VariableClashesWithConstantException e) {
            return CalculateResult.buildErrorResult(context.getString(
                    R.string.variable_name_clashes_constant, e.getVariable().getName()));
        } catch (Verify.NotUniqueVariablesException e) {
            return CalculateResult.buildErrorResult(context.getString(R.string.variables_names_not_unique));
        } catch (VariablesResolver.CyclicVariablesDependencyException e) {
            return CalculateResult.buildErrorResult(context.getString(
                    R.string.cyclic_variables, e.getFirstName(), e.getSecondName()));
        } catch (VariableNotFoundException e) {
            return CalculateResult.buildErrorResult(context.getString(
                    R.string.variable_not_found, e.getName()));
        } catch (WordNotFoundException e) {
            return CalculateResult.buildErrorResult(context.getString(
                    R.string.word_not_found, e.getWord()));
        } catch (InvalidNumberException e) {
            return CalculateResult.buildErrorResult(context.getString(R.string.invalid_number));
        } catch (Exception e) {
            return CalculateResult.buildErrorResult(context.getString(R.string.bad_expression));
        }
    }
}
