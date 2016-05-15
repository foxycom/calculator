package com.guliash.calculator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

public class VariablesAdapterRemove extends RecyclerView.Adapter<VariablesAdapterRemove.ViewHolder> {

    @Nullable private List<VariableWrapper> mVariables;
    @NonNull private Callbacks mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText valueEditText, nameEditText;
        public ImageButton removeButton;
        public ViewHolder(View view) {
            super(view);
            valueEditText = (EditText)view.findViewById(R.id.variable_value);
            nameEditText = (EditText)view.findViewById(R.id.variable_name);
            removeButton = (ImageButton)view.findViewById(R.id.remove_button);
        }
    }

    public VariablesAdapterRemove(@Nullable List<VariableWrapper> variables,
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
                if(position != RecyclerView.NO_POSITION) {
                    mVariables.get(position).name = s.toString();
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
                if(position != RecyclerView.NO_POSITION) {
                    mVariables.get(position).value = s.toString();
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
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onVariableRemove(position);
                }
            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VariableWrapper variable = mVariables.get(position);
        holder.nameEditText.setText(variable.name);
        holder.valueEditText.setText(variable.value);
    }

    @Override
    public int getItemCount() {
        return mVariables == null ? 0 : mVariables.size();
    }

    public interface Callbacks {
        void onVariableRemove(int position);
    }
}
