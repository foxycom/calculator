package com.guliash.calculator.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guliash.calculator.Constants;
import com.guliash.calculator.DBHelper;
import com.guliash.calculator.Helper;
import com.guliash.calculator.R;
import com.guliash.calculator.structures.CalculatorDataset;
import com.guliash.calculator.structures.StringVariableWrapper;
import com.guliash.calculator.ui.adapters.VariablesAdapterRemove;

public class SaveActivity extends AppCompatActivity implements VariablesAdapterRemove.Callbacks {

    private EditText mExpressionEditText, mDatasetNameEditText;
    private VariablesAdapterRemove mAdapter;
    private CalculatorDataset mDataset;
    private DBHelper mDbHelper;
    private RecyclerView mVariablesRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

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

        Button addButton = (Button) findViewById(R.id.add);
        Button saveButton = (Button) findViewById(R.id.save);
        addButton.setOnClickListener(mAddVariableClickListener);
        saveButton.setOnClickListener(mSaveClickListener);

        mExpressionEditText = (EditText)findViewById(R.id.expression);
        mDatasetNameEditText = (EditText)findViewById(R.id.dataset_name);

        mVariablesRV = (RecyclerView) findViewById(R.id.variables_rv);
        mVariablesRV.setLayoutManager(new LinearLayoutManager(this));

        mDbHelper = new DBHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mExpressionEditText.setText(mDataset.expression);
        mDatasetNameEditText.setText(mDataset.datasetName);

        mAdapter = new VariablesAdapterRemove(mDataset.variables, this);
        mVariablesRV.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDataset.expression = mExpressionEditText.getText().toString();
        mDataset.datasetName = mDatasetNameEditText.getText().toString();
        outState.putParcelable(Constants.DATASET, mDataset);
    }

    private View.OnClickListener mAddVariableClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDataset.variables.add(new StringVariableWrapper("", "0"));
            mAdapter.notifyItemInserted(mDataset.variables.size() - 1);
        }
    };

    private View.OnClickListener mSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDataset.datasetName = mDatasetNameEditText.getText().toString();
            mDataset.expression = mExpressionEditText.getText().toString();
            if(TextUtils.isEmpty(mDataset.datasetName)) {
                Toast.makeText(getApplicationContext(), R.string.name_is_empty, Toast.LENGTH_SHORT).
                        show();
                return;
            }
            if(mDbHelper.getIdOfRowWithName(mDataset.datasetName) != -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveActivity.this);
                builder.setMessage(String.format(getString(R.string.unique_dataset_name_error),
                        mDataset.datasetName));
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mDbHelper.updateData(mDataset);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else {
                mDataset.timestamp = Helper.getCurrentTimestamp();
                mDbHelper.addDataset(mDataset);
                setResult(RESULT_OK);
                finish();
            }
        }
    };

    @Override
    public void onVariableRemove(int position) {
        mDataset.variables.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mDataset.variables.size());
    }
}
