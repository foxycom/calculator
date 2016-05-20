package com.guliash.calculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DescriptionActivity extends AppCompatActivity {

    private TextView mDescText, mNameText;
    private ListView mExamplesList;

    private Topic mTopic;

    public static final String TOPIC = "topic";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle args = (savedInstanceState != null ? savedInstanceState : getIntent().getExtras());

        mTopic = args.getParcelable(TOPIC);

        mNameText = (TextView)findViewById(R.id.name);
        mDescText = (TextView)findViewById(R.id.desc);
        mExamplesList = (ListView)findViewById(R.id.examples);

        mNameText.setText(mTopic.name);
        mDescText.setText(mTopic.description);
        mExamplesList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                mTopic.examples));
    }
}
