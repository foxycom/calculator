package com.guliash.calculator.ui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.guliash.calculator.R;
import com.guliash.calculator.structures.Topic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    private ListView mTopicsList;
    private List<Topic> mTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTopics = getTopics();
        mTopicsList = (ListView) findViewById(R.id.topics_list);
        mTopicsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               itemClicked(mTopics.get(position));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTopicsList.setAdapter(new ArrayAdapter<>(this, R.layout.topic_item, mTopics));
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

    private void itemClicked(Topic topic) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra(DescriptionActivity.TOPIC, topic);
        startActivity(intent);
    }
}
