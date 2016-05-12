package com.guliash.calculator;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HelpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private FunctionsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static class Function {
        public String name, description;
        public Function(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        public String toString() {
            return name + " = " + description;
        }
    }

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
        mAdapter = new FunctionsAdapter(getFunctions());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<Function> getFunctions() {
        Resources res = getResources();
        TypedArray functionsNames = res.obtainTypedArray(R.array.functions);
        TypedArray descriptions = res.obtainTypedArray(R.array.description);
        ArrayList<Function> functions = new ArrayList<>();
        for(int i = 0; i < functionsNames.length(); i++) {
            functions.add(new Function(functionsNames.getString(i), descriptions.getString(i)));
        }
        Collections.sort(functions, new Comparator<Function>() {
            @Override
            public int compare(Function lhs, Function rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });
        return functions;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
