package com.guliash.calculator.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.guliash.calculator.Helper;
import com.guliash.calculator.R;
import com.guliash.calculator.structures.CalculatorDataSet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DatasetsAdapterCV extends RecyclerView.Adapter<DatasetsAdapterCV.DatasetViewHolder> {

    public interface Callbacks {
        void onRemove(int position);

        void onEdit(int position);

        void onUse(int position);
    }

    @Nullable
    private List<CalculatorDataSet> mDatasets;
    @NonNull
    private Callbacks mListener;

    static class DatasetViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.dataset_name)
        TextView name;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.expression)
        TextView expression;

        @BindView(R.id.variables)
        TextView variables;

        @BindView(R.id.overflow_button)
        ImageButton overflow;

        DatasetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public DatasetsAdapterCV(@Nullable List<CalculatorDataSet> datasets, @NonNull Callbacks listener) {
        this.mDatasets = datasets;
        this.mListener = listener;
    }

    @Override
    public DatasetsAdapterCV.DatasetViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final DatasetViewHolder viewHolder = new DatasetViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.dataset_item_cv, parent,
                        false));
        viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(viewHolder);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DatasetsAdapterCV.DatasetViewHolder holder, final int position) {
        CalculatorDataSet dataset = mDatasets.get(position);
        holder.name.setText(dataset.getName());
        holder.variables.setText(Helper.variablesToString(dataset.getVariables()));
        holder.expression.setText(dataset.getExpression());
        holder.date.setText(Helper.getFormattedDate(dataset.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return mDatasets == null ? 0 : mDatasets.size();
    }

    private void showPopup(final DatasetViewHolder viewHolder) {
        final PopupMenu popupMenu = new PopupMenu(viewHolder.itemView.getContext(),
                viewHolder.overflow);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return false;
                }
                switch (item.getItemId()) {
                    case R.id.use:
                        mListener.onUse(position);
                        break;
                    case R.id.edit:
                        mListener.onEdit(position);
                        break;
                    case R.id.delete:
                        mListener.onRemove(position);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        popupMenu.inflate(R.menu.datasets_adapter_menu);
        popupMenu.show();
    }
}
