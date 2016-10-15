package com.guliash.calculator.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guliash.calculator.App;
import com.guliash.calculator.Constants;
import com.guliash.calculator.R;
import com.guliash.calculator.calculator.Calculator;
import com.guliash.calculator.structures.CalculatorDataSet;
import com.guliash.calculator.structures.StringVariableWrapper;
import com.guliash.calculator.ui.adapters.VariablesAdapter;
import com.guliash.calculator.ui.utils.Utils;
import com.guliash.calculator.utils.Preconditions;
import com.guliash.parser.StringVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import timber.log.Timber;

public class CalculatorFragment extends Fragment implements VariablesAdapter.Callbacks {

    private static final List<StringVariableWrapper> DEFAULT_VARIABLES_LIST =
            Arrays.asList(new StringVariableWrapper("x", "0"), new StringVariableWrapper("y", "0"));

    private static final long VARIABLE_FIELDS_ANIM_DURATION = 500;

    private static final String ACTIVATED_POS_EXTRA = "activated_pos";

    @BindView(R.id.input_field)
    EditText mInputField;

    @BindView(R.id.result_field)
    TextView mResultField;

    @BindView(R.id.variables_list)
    RecyclerView mVariablesList;

    @BindView(R.id.variable_fields_container)
    ViewGroup mVariableFieldsContainer;

    @BindView(R.id.variable_name)
    EditText mVariableNameInput;

    @BindView(R.id.variable_value)
    EditText mVariableValueInput;

    @BindView(R.id.digit_keyboard)
    ViewGroup mDigitKeyboard;

    @BindView(R.id.functions_keyboard)
    ViewGroup mFunctionsKeyboard;

    @Inject
    Calculator mCalculator;

    private EditText mFocusedInput;

    private VariablesAdapter mAdapter;

    private CalculatorDataSet mDataset;

    private int mActivatedVariable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(getContext()).getAppComponent().inject(this);

        if (savedInstanceState != null) {
            mDataset = savedInstanceState.getParcelable(Constants.DATASET);
            mActivatedVariable = savedInstanceState.getInt(ACTIVATED_POS_EXTRA);
        } else {
            mDataset = new CalculatorDataSet("", "", 0, new ArrayList<>(DEFAULT_VARIABLES_LIST));
            mActivatedVariable = VariablesAdapter.NO_ACTIVATED;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDataset.setExpression(mInputField.getText().toString());
        outState.putInt(ACTIVATED_POS_EXTRA, mActivatedVariable);
        outState.putParcelable(Constants.DATASET, mDataset);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calculator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setupInputs();
        setupKeyboardListeners();
        setupVariableInputs();

        mVariablesList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new VariablesAdapter(mDataset.getVariables(), this);
        mVariablesList.setAdapter(mAdapter);

        if (mActivatedVariable != VariablesAdapter.NO_ACTIVATED) {
            mAdapter.activate(mActivatedVariable);
        }
    }

    private void setupInputs() {
        Utils.disableKeyboardOnEditTextFocus(mInputField);
        Utils.disableKeyboardOnEditTextFocus(mVariableValueInput);
        mInputField.setOnFocusChangeListener(inputsFocusListener);
        mVariableValueInput.setOnFocusChangeListener(inputsFocusListener);
    }

    private void setupKeyboardListeners() {
        addKeyboardButtonsListeners(mDigitKeyboard, digitKeyboardClickListener);
        addKeyboardButtonsListeners(mFunctionsKeyboard, functionKeyboardClickListener);
    }

    private void setupVariableInputs() {
        mVariableNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mActivatedVariable == VariablesAdapter.NO_ACTIVATED) {
                    return;
                }
                StringVariable variable = mDataset.getVariables().get(mActivatedVariable);
                variable.setName(s.toString());
                mAdapter.notifyItemChanged(mActivatedVariable);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mVariableValueInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mActivatedVariable == VariablesAdapter.NO_ACTIVATED) {
                    return;
                }
                StringVariable variable = mDataset.getVariables().get(mActivatedVariable);
                variable.setValue(s.toString());
                mAdapter.notifyItemChanged(mActivatedVariable);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private View.OnFocusChangeListener inputsFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mFocusedInput = (EditText) v;
                Utils.hideSoftKeyboard(getActivity());
            } else {
                if (v == mFocusedInput) {
                    mFocusedInput = null;
                }
            }
        }
    };

    private View.OnClickListener digitKeyboardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            inputString((String) v.getTag());
        }
    };

    private View.OnClickListener functionKeyboardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            inputString(v.getTag() + "(");
        }
    };

    private void addKeyboardButtonsListeners(ViewGroup keyboard, View.OnClickListener listener) {
        for (int i = 0, count = keyboard.getChildCount(); i < count; i++) {
            View view = keyboard.getChildAt(i);
            if (view instanceof Button && isPrintableKey(view)) {
                view.setOnClickListener(listener);
            }
        }
    }

    private boolean isPrintableKey(View view) {
        return view.getId() != R.id.equals && view.getId() != R.id.del;
    }

    @OnClick(R.id.equals)
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

    @OnClick(R.id.del)
    void onDelClick() {
        if(mFocusedInput == null) {
            return;
        }
        int start = mFocusedInput.getSelectionStart();
        if (start != 0) {
            mFocusedInput.getEditableText().delete(start - 1, start);
        }
    }

    @OnLongClick(R.id.del)
    boolean onDelLongClick() {
        if(mFocusedInput == null) {
            return false;
        }
        mFocusedInput.setText("");
        return true;
    }

    @OnClick(R.id.add_variable)
    void onAddVariableClick() {
        mDataset.getVariables().add(StringVariableWrapper.defaultVariable());
        mAdapter.notifyItemInserted(mDataset.getVariables().size() - 1);
        mVariablesList.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @OnClick(R.id.delete)
    void onDeleteClick() {
        Preconditions.assertNotEquals(mActivatedVariable, VariablesAdapter.NO_ACTIVATED);

        mDataset.getVariables().remove(mActivatedVariable);
        mAdapter.notifyItemRemoved(mActivatedVariable);
        mAdapter.notifyItemRangeChanged(mActivatedVariable, mDataset.getVariables().size() - mActivatedVariable);
        mAdapter.clearActivation();
    }

    private void inputString(String str) {
        if (mFocusedInput == null) {
            return;
        }
        int start = Math.max(mFocusedInput.getSelectionStart(), 0);
        int end = Math.max(mFocusedInput.getSelectionEnd(), 0);
        mFocusedInput.getText().replace(start, end, str, 0, str.length());
    }

    public CalculatorDataSet getDataset() {
        mDataset.setExpression(mInputField.getText().toString());
        return mDataset;
    }

    public void setDataset(CalculatorDataSet dataset) {

        if(mActivatedVariable != VariablesAdapter.NO_ACTIVATED) {
            onDeactivated(mActivatedVariable);
        }

        mDataset = dataset;
        mInputField.setText(dataset.getExpression());
        mAdapter = new VariablesAdapter(mDataset.getVariables(), this);
        mVariablesList.setAdapter(mAdapter);
        mResultField.setText("");
    }

    @Override
    public void onActivated(int pos) {
        Timber.d("on activated");
        mActivatedVariable = pos;
        mVariablesList.scrollToPosition(pos);
        displayVariableData(mDataset.getVariables().get(mActivatedVariable));
        showVariableContainer();
    }

    private void displayVariableData(StringVariable variable) {
        mVariableNameInput.setText(variable.getName());
        mVariableValueInput.setText(variable.getValue());
    }

    private void showVariableContainer() {
        ViewCompat.animate(mVariableFieldsContainer).cancel();
        ViewCompat.setTranslationX(mVariableFieldsContainer, mVariableFieldsContainer.getWidth());
        mVariableFieldsContainer.setVisibility(View.VISIBLE);
        ViewCompat.animate(mVariableFieldsContainer)
                .translationX(0f)
                .setDuration(VARIABLE_FIELDS_ANIM_DURATION);
    }

    @Override
    public void onDeactivated(int pos) {
        Timber.d("on deactivated");
        if (mActivatedVariable == pos) {
            mActivatedVariable = VariablesAdapter.NO_ACTIVATED;
            hideVariableContainer();
        }
    }

    private void hideVariableContainer() {
        ViewCompat.animate(mVariableFieldsContainer).cancel();
        ViewCompat.animate(mVariableFieldsContainer)
                .translationX(mVariableFieldsContainer.getWidth())
                .setDuration(VARIABLE_FIELDS_ANIM_DURATION)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mVariableFieldsContainer.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onChosen(int pos) {
        Timber.d("on chosen");
        inputString(mDataset.getVariables().get(pos).getName());
    }
}
