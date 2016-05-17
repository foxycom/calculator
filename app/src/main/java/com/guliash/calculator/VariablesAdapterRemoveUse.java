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

public class VariablesAdapterRemoveUse extends RecyclerView.Adapter<VariablesAdapterRemoveUse.ViewHolder> {

    @Nullable private List<StringVariableWrapper> mVariables;
    @NonNull private Callbacks mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText valueEditText, nameEditText;
        public ImageButton removeButton, checkButton;
        public ViewHolder(View view) {
            super(view);
            valueEditText = (EditText)view.findViewById(R.id.variable_value);
            nameEditText = (EditText)view.findViewById(R.id.variable_name);
            removeButton = (ImageButton)view.findViewById(R.id.remove_button);
            checkButton = (ImageButton)view.findViewById(R.id.check_button);
        }
    }

    public VariablesAdapterRemoveUse(@Nullable List<StringVariableWrapper> variables,
                                     @NonNull Callbacks listener) {
        this.mVariables = variables;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.variable_item_remove_use,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mVariables.get(position).name = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.valueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final  int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mVariables.get(position).value = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onVariableRemove(position);
                }
            }
        });

        holder.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onVariableUse(position);
                }
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StringVariableWrapper variable = mVariables.get(position);
        holder.nameEditText.setText(variable.name);
        holder.valueEditText.setText(variable.value);
    }

    @Override
    public int getItemCount() {
        return mVariables == null ? 0 : mVariables.size();
    }

    public interface Callbacks {
        void onVariableRemove(int position);
        void onVariableUse(int position);
    }

}
