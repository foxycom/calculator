package com.guliash.calculator.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.guliash.calculator.R;
import com.guliash.calculator.structures.StringVariableWrapper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VariablesAdapterRemove extends RecyclerView.Adapter<VariablesAdapterRemove.ViewHolder> {

    public interface Callbacks {
        void onVariableRemove(int position);
    }

    @Nullable
    private List<StringVariableWrapper> mVariables;
    @NonNull
    private Callbacks mListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.variable_value)
        EditText valueEditText;

        @BindView(R.id.variable_name)
        EditText nameEditText;

        @BindView(R.id.remove_button)
        ImageView removeButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public VariablesAdapterRemove(@Nullable List<StringVariableWrapper> variables,
                                  @NonNull Callbacks listener) {
        this.mVariables = variables;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.variable_item_remove,
                parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mVariables.get(position).setName(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewHolder.valueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mVariables.get(position).setValue(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onVariableRemove(position);
                }
            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StringVariableWrapper variable = mVariables.get(position);
        holder.nameEditText.setText(variable.getName());
        holder.valueEditText.setText(variable.getValue());
    }

    @Override
    public int getItemCount() {
        return mVariables == null ? 0 : mVariables.size();
    }
}
