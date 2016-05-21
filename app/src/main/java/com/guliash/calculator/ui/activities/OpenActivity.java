package com.guliash.calculator.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.guliash.calculator.Constants;
import com.guliash.calculator.DBHelper;
import com.guliash.calculator.R;
import com.guliash.calculator.structures.CalculatorDataset;
import com.guliash.calculator.ui.adapters.DatasetsAdapterCV;

import java.util.ArrayList;

public class OpenActivity extends BaseActivity implements DatasetsAdapterCV.Callbacks {

    private ArrayList<CalculatorDataset> mDatasets;
    private DBHelper mDbHelper;
    private RecyclerView mRecyclerView;
    private DatasetsAdapterCV mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mDbHelper = new DBHelper(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatasets = mDbHelper.getDatasets();
        mAdapter = new DatasetsAdapterCV(mDatasets, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == Constants.SAVE_ACTIVITY_REQUEST_CODE) {
                mDatasets = mDbHelper.getDatasets();
                mAdapter = new DatasetsAdapterCV(mDatasets, this);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

    @Override
    public void onRemove(int position) {
        CalculatorDataset dataset = mDatasets.get(position);
        mDbHelper.deleteData(dataset.datasetName);
        mDatasets.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mDatasets.size() - position);
    }

    @Override
    public void onEdit(int position) {
        Intent intent = new Intent(this, SaveActivity.class);
        CalculatorDataset dataset = mDatasets.get(position);
        intent.putExtra(Constants.DATASET, dataset);
        startActivityForResult(intent, Constants.SAVE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onUse(int position) {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATASET, mDatasets.get(position));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
