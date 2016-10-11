package com.guliash.calculator.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.guliash.calculator.App;
import com.guliash.calculator.Constants;
import com.guliash.calculator.R;
import com.guliash.calculator.calculator.Calculator;
import com.guliash.calculator.structures.CalculatorDataSet;
import com.guliash.calculator.structures.StringVariableWrapper;
import com.guliash.calculator.ui.adapters.VariablesAdapterRemoveUse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculatorFragment extends Fragment implements VariablesAdapterRemoveUse.Callbacks {

    private static final List<StringVariableWrapper> DEFAULT_VARIABLES_LIST =
            Arrays.asList(new StringVariableWrapper("x", "0"), new StringVariableWrapper("y", "0"));

    @BindView(R.id.input_field)
    EditText mInputField;

    @BindView(R.id.result_field)
    TextView mResultField;

    @BindView(R.id.variables_rv)
    RecyclerView mVariablesRV;

    @Inject
    Calculator mCalculator;

    private VariablesAdapterRemoveUse mAdapter;

    private CalculatorDataSet mDataset;

    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(getContext()).getAppComponent().inject(this);

        if (savedInstanceState != null) {
            mDataset = savedInstanceState.getParcelable(Constants.DATASET);
        } else {
            mDataset = new CalculatorDataSet("", "", 0, new ArrayList<>(DEFAULT_VARIABLES_LIST));
        }
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
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mVariablesRV.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new VariablesAdapterRemoveUse(mDataset.getVariables(), this);
        mVariablesRV.setAdapter(mAdapter);
    }

    @OnClick(R.id.add_variable_button)
    void onAddVariableClick() {
        mDataset.getVariables().add(StringVariableWrapper.defaultVariable());
        mAdapter.notifyItemInserted(mDataset.getVariables().size() - 1);
    }

    @OnClick(R.id.equals_image_button)
    void onEqualsClick() {
        String expression = mInputField.getText().toString();

        List<StringVariableWrapper> variables = mDataset.getVariables();

        Calculator.CalculateResult result = mCalculator.calculate(expression, variables);

        if (result.isSuccess()) {
            mResultField.setText(String.format(Locale.ENGLISH, "%f", result.getValue()));
        } else {
            mResultField.setText(result.getErrorMessage());
        }
    }

    @OnClick(R.id.backspace)
    void onBackspaceClick() {
        int start = mInputField.getSelectionStart();
        if (start != 0) {
            mInputField.getEditableText().delete(start - 1, start);
        }
    }

    private void inputString(String str) {
        int start = Math.max(mInputField.getSelectionStart(), 0);
        int end = Math.max(mInputField.getSelectionEnd(), 0);
        mInputField.getText().replace(start, end, str, 0, str.length());
    }

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
