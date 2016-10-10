package com.guliash.calculator.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guliash.calculator.App;
import com.guliash.calculator.Constants;
import com.guliash.calculator.R;
import com.guliash.calculator.state.AppSettings;
import com.guliash.calculator.structures.CalculatorDataSet;
import com.guliash.calculator.structures.StringVariableWrapper;
import com.guliash.calculator.ui.adapters.VariablesAdapterRemoveUse;
import com.guliash.parser.ArithmeticParser;
import com.guliash.parser.StringVariable;
import com.guliash.parser.Verify;
import com.guliash.parser.evaluator.Evaluator;
import com.guliash.parser.evaluator.JavaEvaluator;
import com.guliash.parser.exceptions.CyclicVariablesDependencyException;
import com.guliash.parser.exceptions.VariableNotFoundException;
import com.guliash.parser.exceptions.WordNotFoundException;
import com.guliash.parser.stemmer.InvalidNumberException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class CalculatorFragment extends Fragment implements VariablesAdapterRemoveUse.Callbacks {

    private ImageView mBackspaceButton, mEqualsImage;

    private EditText mInputField;
    private TextView mResultField;

    private Button mAddVariableButton;
    private VariablesAdapterRemoveUse mAdapter;
    private RecyclerView mVariablesRV;

    @Inject
    AppSettings mAppSettings;

    private static final List<StringVariableWrapper> DEFAULT_VARIABLES_LIST =
            Arrays.asList(new StringVariableWrapper("x", "0"), new StringVariableWrapper("y", "0"));

    private CalculatorDataSet mDataset;

    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    public CalculatorFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(getContext()).getAppComponent().inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDataset.setExpression(mInputField.getText().toString());
        outState.putParcelable(Constants.DATASET, mDataset);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        initViews(view);
        initClickListeners();

        if(savedInstanceState != null) {
            mDataset = savedInstanceState.getParcelable(Constants.DATASET);
        } else {
            mDataset = new CalculatorDataSet("", "", 0, new ArrayList<>(DEFAULT_VARIABLES_LIST));
        }

        mVariablesRV.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new VariablesAdapterRemoveUse(mDataset.getVariables(), this);
        mVariablesRV.setAdapter(mAdapter);

        return view;
    }

    private void initViews(View view) {

        mBackspaceButton = (ImageView)view.findViewById(R.id.backspace);
        mEqualsImage = (ImageView)view.findViewById(R.id.equals_image_button);

        mInputField = (EditText)view.findViewById(R.id.input_field);
        mResultField = (TextView)view.findViewById(R.id.result_field);

        mAddVariableButton = (Button)view.findViewById(R.id.add_variable_button);

        mVariablesRV = (RecyclerView)view.findViewById(R.id.variables_rv);
    }

    private void initClickListeners() {

        mEqualsImage.setOnClickListener(mEqualsButtonClickListener);

        mBackspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = mInputField.getSelectionStart();
                if(start != 0) {
                    mInputField.getEditableText().delete(start - 1, start);
                }
            }
        });

        mAddVariableButton.setOnClickListener(mAddVariableButtonClickListener);
    }

    private void inputString(String str) {
        int start = Math.max(mInputField.getSelectionStart(), 0);
        int end = Math.max(mInputField.getSelectionEnd(), 0);
        mInputField.getText().replace(start, end, str, 0, str.length());
    }

    private View.OnClickListener mEqualsButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Evaluator evaluator = new JavaEvaluator(mAppSettings.getAngleUnits());
            String expression = mInputField.getText().toString();

            if(TextUtils.isEmpty(expression)) {
                Toast.makeText(getContext(), R.string.expression_is_empty,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            List<? extends StringVariable> variables = mDataset.getVariables();
            for(StringVariable variable : variables) {
                if(!Verify.variable(variable)) {
                    Toast.makeText(getContext(), getString(
                            R.string.variable_name_not_correct, variable.getName()), Toast.LENGTH_LONG).show();
                    return;
                }
            }

            for(StringVariable variable : variables) {
                if(Verify.variableNameClashesWithConstants(variable, evaluator)) {
                    Toast.makeText(getActivity().getApplicationContext(), getString(
                            R.string.variable_name_clashes_constant, variable.getName()), Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if(!Verify.checkVariablesUnique(mDataset.getVariables())) {
                Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.variables_names_not_unique),Toast.LENGTH_LONG).show();
                return;
            }

            try {
                double result = ArithmeticParser.calculate(expression, (List<StringVariable>)variables, evaluator);
                mResultField.setText(Double.toString(result));
            } catch(CyclicVariablesDependencyException e) {
                mResultField.setText(getString(R.string.cyclic_variables, e.firstName, e.secondName));
            } catch (VariableNotFoundException e) {
                mResultField.setText(getString(R.string.variable_not_found, e.getName()));
            } catch (WordNotFoundException e) {
                mResultField.setText(getString(R.string.word_not_found, e.getWord()));
            } catch (InvalidNumberException e) {
                mResultField.setText(getString(R.string.invalid_number));
            } catch (Exception e) {
                mResultField.setText(getString(R.string.bad_expression));
            }
        }
    };

    private View.OnClickListener mAddVariableButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDataset.getVariables().add(new StringVariableWrapper("", "0"));
            mAdapter.notifyItemInserted(mDataset.getVariables().size() - 1);
        }
    };


    @Override
    public void onVariableRemove(int position) {
        mDataset.getVariables().remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mDataset.getVariables().size() - position);
    }

    @Override
    public void onVariableUse(int position) {
        inputString(mDataset.getVariables().get(position).getName());
    }

    public CalculatorDataSet getDataset() {
        mDataset.setExpression(mInputField.getText().toString());
        return mDataset;
    }

    public void setDataset(CalculatorDataSet dataset) {
        mDataset = dataset;
        mInputField.setText(dataset.getExpression());
        mAdapter = new VariablesAdapterRemoveUse(mDataset.getVariables(), this);
        mVariablesRV.setAdapter(mAdapter);
        mResultField.setText("");
    }
}
