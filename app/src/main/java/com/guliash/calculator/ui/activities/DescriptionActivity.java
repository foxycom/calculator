package com.guliash.calculator.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.guliash.calculator.R;
import com.guliash.calculator.structures.Topic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionActivity extends BaseActivity {

    public static final String TOPIC = "topic";

    private Topic mTopic;

    @BindView(R.id.name)
    TextView mNameText;

    @BindView(R.id.desc)
    TextView mDescText;

    @BindView(R.id.examples)
    TextView mExamplesText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        mNameText.setText(mTopic.name);
        mDescText.setText(mTopic.description);
        mExamplesText.setText(TextUtils.join("\n\n", transformExamples()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TOPIC, mTopic);
    }

    private List<String> transformExamples() {
        List<String> examples = new ArrayList<>();
        for(String example : mTopic.examples) {
            examples.add("●  " + example);
        }
        return examples;
    }

}
