package com.guliash.calculator;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class HelpActivity extends AppCompatActivity implements TopicsAdapter.Callbacks {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private TopicsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mRecyclerView = (RecyclerView)findViewById(R.id.rv);
        mAdapter = new TopicsAdapter(this, getTopics());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<Topic> getTopics() {
        Resources res = getResources();
        TypedArray names = res.obtainTypedArray(R.array.names);
        TypedArray descriptions = res.obtainTypedArray(R.array.descriptions);
        TypedArray examples = res.obtainTypedArray(R.array.examples);
        ArrayList<Topic> topics = new ArrayList<>();
        int n = names.length();
        for(int i = 0; i < n; i++) {
            int id = examples.getResourceId(i, 0);
            topics.add(new Topic(names.getString(i), descriptions.getString(i),
                    new ArrayList<>(Arrays.asList(res.getStringArray(id)))));
        }
        names.recycle();
        descriptions.recycle();
        examples.recycle();
        return topics;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public void itemClicked(Topic topic) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra(DescriptionActivity.TOPIC, topic);
        startActivity(intent);
    }
}
