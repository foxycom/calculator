package com.guliash.calculator.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.guliash.calculator.App;
import com.guliash.calculator.Constants;
import com.guliash.calculator.Helper;
import com.guliash.calculator.R;
import com.guliash.calculator.state.AppSettings;
import com.guliash.calculator.storage.Storage;
import com.guliash.calculator.structures.CalculatorDataSet;
import com.guliash.calculator.structures.StringVariableWrapper;
import com.guliash.calculator.ui.adapters.VariablesAdapterRemove;
import com.guliash.calculator.ui.fragments.AlertDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveActivity extends BaseActivity implements VariablesAdapterRemove.Callbacks,
        AlertDialogFragment.Callbacks {

    private static final int DIALOG_REVIEW_ID = 1;
    private static final int DIALOG_DATASET_UNIQUE = 2;

    @BindView(R.id.expression)
    EditText mExpressionInput;

    @BindView(R.id.dataset_name)
    EditText mDataSetNameInput;

    private VariablesAdapterRemove mAdapter;
    private CalculatorDataSet mDataset;

    @Inject
    Storage mStorage;

    @Inject
    AppSettings mAppSettings;

    private RecyclerView mVariablesRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        ButterKnife.bind(this);
        App.get(this).getAppComponent().inject(this);

        Bundle args = (savedInstanceState != null ? savedInstanceState : getIntent().getExtras());
        mDataset = args.getParcelable(Constants.DATASET);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mVariablesRV = (RecyclerView) findViewById(R.id.variables_rv);
        mVariablesRV.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        mExpressionInput.setText(mDataset.getExpression());
        mDataSetNameInput.setText(mDataset.getName());

        mAdapter = new VariablesAdapterRemove(mDataset.getVariables(), this);
        mVariablesRV.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDataset.setExpression(mExpressionInput.getText().toString());
        mDataset.setName(mDataSetNameInput.getText().toString());
        outState.putParcelable(Constants.DATASET, mDataset);
    }

    @OnClick(R.id.add)
    void onAddClick() {
        mDataset.getVariables().add(StringVariableWrapper.defaultVariable());
        mAdapter.notifyItemInserted(mDataset.getVariables().size() - 1);
    }

    @OnClick(R.id.save)
    void onSaveClick() {
        mDataset.setName(mDataSetNameInput.getText().toString());
        mDataset.setExpression(mExpressionInput.getText().toString());
        if (TextUtils.isEmpty(mDataset.getName())) {
            Toast.makeText(getApplicationContext(), R.string.name_is_empty, Toast.LENGTH_SHORT).
                    show();
            return;
        }
        if (mStorage.hasDataSet(mDataset)) {
            showAlertDialog(getString(R.string.dialog_error),
                    getString(R.string.unique_dataset_name_error, mDataset.getName()),
                    getString(R.string.OK), getString(R.string.NO), null, true, DIALOG_DATASET_UNIQUE);
        } else {
            mDataset.setTimestamp(Helper.getCurrentTimestamp());
            mStorage.addDataSet(mDataset);
            setResult(RESULT_OK);
            if (showReviewDialogIfNeed()) {
                finish();
            }
        }
    }

    @Override
    public void onVariableRemove(int position) {
        mDataset.getVariables().remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mDataset.getVariables().size());
    }

    private boolean showReviewDialogIfNeed() {
        boolean reviewed = mAppSettings.isReviewInviteShown();
        if (!reviewed) {
            showAlertDialog(getString(R.string.review_title), getString(R.string.review_message),
                    getString(R.string.review_positive), getString(R.string.review_negative), null, true,
                    DIALOG_REVIEW_ID);
        }
        return reviewed;
    }

    private void openReview() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    @Override
    public void onPositive(int id) {
        switch (id) {
            case DIALOG_REVIEW_ID:
                mAppSettings.shownReviewInvite();
                openReview();
                finish();
                break;
            case DIALOG_DATASET_UNIQUE:
                mStorage.updateDataSet(mDataset);
                setResult(RESULT_OK);
                if (showReviewDialogIfNeed()) {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onNegative(int id) {
        switch (id) {
            case DIALOG_REVIEW_ID:
                mAppSettings.shownReviewInvite();
                finish();
                break;
        }
    }

    @Override
    public void onNeutral(int id) {

    }

    @Override
    public void onCancel(int id) {

    }

    @Override
    public void onDismiss(int id) {

    }
}
