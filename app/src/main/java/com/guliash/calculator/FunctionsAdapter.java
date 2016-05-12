package com.guliash.calculator;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FunctionsAdapter extends RecyclerView.Adapter<FunctionsAdapter.FunctionViewHolder> {

    private ArrayList<HelpActivity.Function> mFunctions;

    public FunctionsAdapter(@Nullable ArrayList<HelpActivity.Function> functions) {
        this.mFunctions = functions;
    }

    public static class FunctionViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public FunctionViewHolder(View view) {
            super(view);
            tv = (TextView)view.findViewById(R.id.text);
        }
    }

    @Override
    public FunctionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FunctionViewHolder functionViewHolder = new FunctionViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.function_item, parent, false));
        return functionViewHolder;
    }

    @Override
    public void onBindViewHolder(FunctionViewHolder holder, int position) {
        holder.tv.setText(mFunctions.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mFunctions == null ? 0 : mFunctions.size();
    }
}
