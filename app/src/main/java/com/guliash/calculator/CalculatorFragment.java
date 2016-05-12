package com.guliash.calculator;

import android.os.Bundle;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.guliash.parser.Angle;
import com.guliash.parser.ArithmeticParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculatorFragment extends Fragment implements VariablesAdapterRemoveUse.Callbacks {

    //operators
    private Button mEqualsButton;

    //additional
    private Button mClearButton;
    private ImageView mBackspaceButton, mEqualsImage;

    //angles
    private RadioGroup mAngleRadioGroup;

    private EditText mInputField;
    private TextView mResultField;

    private Button mAddVariableButton;
    private VariablesAdapterRemoveUse mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mVariablesRV;

    //switchers
    private RadioGroup mSwitchersRadioGroup;

    //keyboards
    private ViewGroup mMainKeyboard, mPowKeyboard, mTrigonometricKeyboard, mLogKeyboard,
        mMathKeyboard, mVariablesLayout, mAngleKeyboard;

    private String mCurrentKeyboard;

    private static final List<StringValueVariable> DEFAULT_VARIABLES_LIST =
            Arrays.asList(new StringValueVariable("x", "0"), new StringValueVariable("y", "0"));

    private CalculatorDataset mDataset;

    public static CalculatorFragment newInstance() {
        CalculatorFragment fragment = new CalculatorFragment();
        return fragment;
    }

    public CalculatorFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDataset.expression = mInputField.getText().toString();
        outState.putParcelable(Constants.DATASET, mDataset);
        outState.putString(Constants.KEYBOARD, mCurrentKeyboard);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        initViews(view);
        initClickListeners();

        if(savedInstanceState != null) {
            mDataset = savedInstanceState.getParcelable(Constants.DATASET);
            mCurrentKeyboard = savedInstanceState.getString(Constants.KEYBOARD);
        } else {
            mDataset = new CalculatorDataset("", "", new ArrayList<>(DEFAULT_VARIABLES_LIST), 0);
            mCurrentKeyboard = Constants.MAIN_KEYBOARD;
        }

        hideAllKeyboards();
        showKeyboard(mCurrentKeyboard);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mVariablesRV.setLayoutManager(mLayoutManager);
        mAdapter = new VariablesAdapterRemoveUse(mDataset.variables, this);
        mVariablesRV.setAdapter(mAdapter);
        setAngleCheck();

        return view;
    }

    private void initViews(View view) {

        //keyboards
        mMainKeyboard = (ViewGroup)view.findViewById(R.id.main_keyboard);
        mTrigonometricKeyboard = (ViewGroup)view.findViewById(R.id.trigonometric_keyboard);
        mPowKeyboard = (ViewGroup)view.findViewById(R.id.pow_keyboard);
        mAngleKeyboard = (ViewGroup)view.findViewById(R.id.angle_keyboard);
        mLogKeyboard = (ViewGroup)view.findViewById(R.id.log_keyboard);
        mMathKeyboard = (ViewGroup)view.findViewById(R.id.math_keyboard);
        mVariablesLayout = (ViewGroup)view.findViewById(R.id.variables_layout);

        //operators
        mEqualsButton = (Button)view.findViewById(R.id.equals_button);

        //additional
        mClearButton = (Button)view.findViewById(R.id.clear_button);
        mBackspaceButton = (ImageView)view.findViewById(R.id.backspace);
        mEqualsImage = (ImageView)view.findViewById(R.id.equals_image_button);

        //angle
        mAngleRadioGroup = (RadioGroup) view.findViewById(R.id.angle_group);

        mInputField = (EditText)view.findViewById(R.id.input_field);
        mResultField = (TextView)view.findViewById(R.id.result_field);

        mAddVariableButton = (Button)view.findViewById(R.id.add_variable_button);

        //switchers
        mSwitchersRadioGroup = (RadioGroup)view.findViewById(R.id.switchers_group);

        mVariablesRV = (RecyclerView)view.findViewById(R.id.variables_rv);
    }

    private void hideAllKeyboards() {
        mMainKeyboard.setVisibility(View.GONE);
        mPowKeyboard.setVisibility(View.GONE);
        mTrigonometricKeyboard.setVisibility(View.GONE);
        mLogKeyboard.setVisibility(View.GONE);
        mMathKeyboard.setVisibility(View.GONE);
        mAngleKeyboard.setVisibility(View.GONE);
        mVariablesLayout.setVisibility(View.GONE);
    }

    private void showKeyboard(String name) {
        mCurrentKeyboard = name;
        switch (name) {
            case Constants.MAIN_KEYBOARD:
                mMainKeyboard.setVisibility(View.VISIBLE);
                mSwitchersRadioGroup.check(R.id.main_keyboard_switcher);
                break;
            case Constants.VARS_KEYBOARD:
                mVariablesLayout.setVisibility(View.VISIBLE);
                mSwitchersRadioGroup.check(R.id.variables_keyboard_switcher);
                break;
            case Constants.TRIGONOMETRIC_KEYBOARD:
                mTrigonometricKeyboard.setVisibility(View.VISIBLE);
                mSwitchersRadioGroup.check(R.id.trigonometric_keyboard_switcher);
                break;
            case Constants.ANGLE_KEYBOARD:
                mAngleKeyboard.setVisibility(View.VISIBLE);
                mSwitchersRadioGroup.check(R.id.angle_units_switcher);
                break;
            case Constants.POW_KEYBOARD:
                mPowKeyboard.setVisibility(View.VISIBLE);
                mSwitchersRadioGroup.check(R.id.pow_keyboard_switcher);
                break;
            case Constants.LOG_KEYBOARD:
                mLogKeyboard.setVisibility(View.VISIBLE);
                mSwitchersRadioGroup.check(R.id.log_keyboard_switcher);
                break;
            case Constants.MATH_KEYBOARD:
                mMathKeyboard.setVisibility(View.VISIBLE);
                mSwitchersRadioGroup.check(R.id.math_keyboard_switcher);
                break;
        }
    }

    private void initClickListeners() {

        setOnClickListenersForKeyboardButtons(mMainKeyboard, mKeyboardButtonClickListener);

        mEqualsButton.setOnClickListener(mEqualsButtonClickListener);
        mEqualsImage.setOnClickListener(mEqualsButtonClickListener);

        setOnClickListenersForKeyboardButtons(mTrigonometricKeyboard, mKeyboardButtonClickListener);

        setOnClickListenersForKeyboardButtons(mPowKeyboard, mKeyboardButtonClickListener);

        setOnClickListenersForKeyboardButtons(mLogKeyboard, mKeyboardButtonClickListener);

        setOnClickListenersForKeyboardButtons(mMathKeyboard, mKeyboardButtonClickListener);

        //angle
        mAngleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Angle angleUnit = Angle.DEG;
                switch (checkedId) {
                    case R.id.radio_deg:
                        angleUnit = Angle.DEG;
                        break;
                    case R.id.radio_rad:
                        angleUnit = Angle.RAD;
                        break;
                    case R.id.radio_grad:
                        angleUnit = Angle.GRAD;
                        break;
                }
                CalculatorApplication application = (CalculatorApplication) getActivity().getApplication();
                application.saveAngleUnit(angleUnit);
                application.angleUnits = angleUnit;
            }
        });

        mSwitchersRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hideAllKeyboards();
                switch (checkedId) {
                    case R.id.main_keyboard_switcher:
                        mCurrentKeyboard = Constants.MAIN_KEYBOARD;
                        mMainKeyboard.setVisibility(View.VISIBLE);
                        break;
                    case R.id.trigonometric_keyboard_switcher:
                        mCurrentKeyboard = Constants.TRIGONOMETRIC_KEYBOARD;
                        mTrigonometricKeyboard.setVisibility(View.VISIBLE);
                        break;
                    case R.id.pow_keyboard_switcher:
                        mCurrentKeyboard = Constants.POW_KEYBOARD;
                        mPowKeyboard.setVisibility(View.VISIBLE);
                        break;
                    case R.id.angle_units_switcher:
                        mCurrentKeyboard = Constants.ANGLE_KEYBOARD;
                        mAngleKeyboard.setVisibility(View.VISIBLE);
                        break;
                    case R.id.log_keyboard_switcher:
                        mCurrentKeyboard = Constants.LOG_KEYBOARD;
                        mLogKeyboard.setVisibility(View.VISIBLE);
                        break;
                    case R.id.math_keyboard_switcher:
                        mCurrentKeyboard = Constants.MATH_KEYBOARD;
                        mMathKeyboard.setVisibility(View.VISIBLE);
                        break;
                    case R.id.variables_keyboard_switcher:
                        mCurrentKeyboard = Constants.VARS_KEYBOARD;
                        mVariablesLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


        //additional
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputField.setText("");
                mResultField.setText("");
            }
        });

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

    private View.OnClickListener mKeyboardButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = "";
            String openBracket = getString(R.string.open_bracket);
            String closeBracket = getString(R.string.close_bracket);
            switch (v.getId()) {
                //digits
                case R.id.zero_button:
                    str = getString(R.string.zero);
                    break;
                case R.id.one_button:
                    str = getString(R.string.one);
                    break;
                case R.id.two_button:
                    str = getString(R.string.two);
                    break;
                case R.id.three_button:
                    str = getString(R.string.three);
                    break;
                case R.id.four_button:
                    str = getString(R.string.four);
                    break;
                case R.id.five_button:
                    str = getString(R.string.five);
                    break;
                case R.id.six_button:
                    str = getString(R.string.six);
                    break;
                case R.id.seven_button:
                    str = getString(R.string.seven);
                    break;
                case R.id.eight_button:
                    str = getString(R.string.eight);
                    break;
                case R.id.nine_button:
                    str = getString(R.string.nine);
                    break;

                //operators
                case R.id.plus_button:
                    str = getString(R.string.plus);
                    break;
                case R.id.minus_button:
                    str = getString(R.string.minus);
                    break;
                case R.id.times_button:
                    str = getString(R.string.times);
                    break;
                case R.id.division_button:
                    str = getString(R.string.division);
                    break;

                //functions
                //trigonometric
                case R.id.sin_button:
                    str = getString(R.string.sin) + openBracket;
                    break;
                case R.id.cos_button:
                    str = getString(R.string.cos) + openBracket;
                    break;
                case R.id.tan_button:
                    str = getString(R.string.tan) + openBracket;
                    break;
                case R.id.cot_button:
                    str = getString(R.string.cot) + openBracket;
                    break;
                case R.id.asin_button:
                    str = getString(R.string.asin) + openBracket;
                    break;
                case R.id.acos_button:
                    str = getString(R.string.acos) + openBracket;
                    break;
                case R.id.atan_button:
                    str = getString(R.string.atan) + openBracket;
                    break;
                case R.id.acot_button:
                    str = getString(R.string.acot) + openBracket;
                    break;
                case R.id.sinh_button:
                    str = getString(R.string.sinh) + openBracket;
                    break;
                case R.id.cosh_button:
                    str = getString(R.string.cosh) + openBracket;
                    break;
                case R.id.tanh_button:
                    str = getString(R.string.tanh) + openBracket;
                    break;
                case R.id.coth_button:
                    str = getString(R.string.coth) + openBracket;
                    break;
                case R.id.pi_button:
                    str = getString(R.string.pi);
                    break;

                //pow
                case R.id.pow_button:
                    str = getString(R.string.pow) + openBracket;
                    break;
                case R.id.sqrt_button:
                    str = getString(R.string.sqrt) + openBracket;
                    break;
                case R.id.exp_button:
                    str = getString(R.string.exp) + openBracket;
                    break;

                //log functions
                case R.id.log_button:
                    str = getString(R.string.log) + openBracket;
                    break;
                case R.id.log10_button:
                    str = getString(R.string.log10) + openBracket;
                    break;
                case R.id.ln_button:
                    str = getString(R.string.ln) + openBracket;
                    break;
                case R.id.e_button:
                    str = "e";
                    break;

                //math functions
                case R.id.abs_button:
                    str = getString(R.string.abs) + openBracket;
                    break;
                case R.id.floor_button:
                    str = getString(R.string.floor) + openBracket;
                    break;
                case R.id.ceil_button:
                    str = getString(R.string.ceil) + openBracket;
                    break;
                case R.id.min_button:
                    str = getString(R.string.min) + openBracket;
                    break;
                case R.id.max_button:
                    str = getString(R.string.max) + openBracket;
                    break;
                case R.id.random_button:
                    str = getString(R.string.random) + openBracket + closeBracket;
                    break;
                case R.id.round_button:
                    str = getString(R.string.round) + openBracket;
                    break;
                case R.id.signum_button:
                    str = getString(R.string.signum) + openBracket;
                    break;
                case R.id.fact_button:
                    str = getString(R.string.fact) + openBracket;
                    break;
                case R.id.mod_button:
                    str = getString(R.string.mod) + openBracket;
                    break;

                //additional
                case R.id.open_bracket_button:
                    str = getString(R.string.open_bracket);
                    break;
                case R.id.close_bracket_button:
                    str = getString(R.string.close_bracket);
                    break;
                case R.id.point_button:
                    str = getString(R.string.point);
                    break;
                case R.id.comma_button:
                    str = getString(R.string.comma);
                    break;
            }
            inputString(str);
        }
    };

    private void inputString(String str) {
        int start = Math.max(mInputField.getSelectionStart(), 0);
        int end = Math.max(mInputField.getSelectionEnd(), 0);
        mInputField.getText().replace(start, end, str, 0, str.length());
    }

    private View.OnClickListener mEqualsButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String expression = mInputField.getText().toString();
            if(TextUtils.isEmpty(expression)) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.expression_is_empty,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            CalculatorApplication application = (CalculatorApplication)getActivity().getApplication();
            try {
                ArithmeticParser arithmeticParser = new ArithmeticParser(expression,
                    Helper.stringValueVariablesToSimple(mDataset.variables), application.angleUnits);
                mResultField.setText(Double.toString(arithmeticParser.calculate()));
            } catch(Exception e) {
                mResultField.setText(R.string.error);
            }
        }
    };

    private View.OnClickListener mAddVariableButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDataset.variables.add(new StringValueVariable("", "0"));
            mAdapter.notifyItemInserted(mDataset.variables.size() - 1);
        }
    };

    private void setAngleCheck() {
        CalculatorApplication application = (CalculatorApplication)getActivity().getApplication();
        switch(application.angleUnits) {
            case DEG:
                mAngleRadioGroup.check(R.id.radio_deg);
                break;
            case RAD:
                mAngleRadioGroup.check(R.id.radio_rad);
                break;
            case GRAD:
                mAngleRadioGroup.check(R.id.radio_grad);
                break;
        }
    }

    @Override
    public void onVariableRemove(int position) {
        mDataset.variables.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mDataset.variables.size() - position);
    }

    @Override
    public void onVariableUse(int position) {
        inputString(mDataset.variables.get(position).name);
    }

    public CalculatorDataset getDataset() {
        mDataset.expression = mInputField.getText().toString();
        return mDataset;
    }

    public void setDataset(CalculatorDataset dataset) {
        mDataset = dataset;
        mInputField.setText(dataset.expression);
        mAdapter = new VariablesAdapterRemoveUse(mDataset.variables, this);
        mVariablesRV.setAdapter(mAdapter);
        mResultField.setText("");
    }

    private void setOnClickListenersForKeyboardButtons(ViewGroup viewGroup,
                                                       View.OnClickListener listener) {
        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if(view instanceof ViewGroup){
                setOnClickListenersForKeyboardButtons((ViewGroup)view, listener);
            } else {
                if (view instanceof Button) {
                    view.setOnClickListener(listener);
                }
            }
        }
    }
}
