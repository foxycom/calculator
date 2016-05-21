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
import com.guliash.calculator.structures.CalculatorDataset;

import java.util.List;

public class DatasetsAdapterCV extends RecyclerView.Adapter<DatasetsAdapterCV.DatasetViewHolder> {

    @Nullable private List<CalculatorDataset> mObjects;
    @NonNull private Callbacks mListener;

    public static class DatasetViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, dateTextView, expTextView, varTextView;
        public ImageButton overflowButton;
        public DatasetViewHolder(View view) {
            super(view);
            nameTextView = (TextView)view.findViewById(R.id.dataset_name);
            dateTextView = (TextView)view.findViewById(R.id.date);
            expTextView = (TextView)view.findViewById(R.id.expression);
            varTextView = (TextView)view.findViewById(R.id.variables);
            overflowButton = (ImageButton)view.findViewById(R.id.overflow_button);
        }
    }

    public interface Callbacks {
        void onRemove(int position);
        void onEdit(int position);
        void onUse(int position);
    }

    public DatasetsAdapterCV(@Nullable List<CalculatorDataset> objects, @NonNull Callbacks listener) {
        this.mObjects = objects;
        this.mListener = listener;
    }

    @Override
    public DatasetsAdapterCV.DatasetViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final DatasetViewHolder viewHolder =  new DatasetViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.dataset_item_cv, parent,
                        false));
        viewHolder.overflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(viewHolder);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DatasetsAdapterCV.DatasetViewHolder holder, final int position) {
        CalculatorDataset dataset = mObjects.get(position);
        holder.nameTextView.setText(dataset.datasetName);
        holder.varTextView.setText(Helper.variablesToString(dataset.variables));
        holder.expTextView.setText(dataset.expression);
        holder.dateTextView.setText(Helper.getFormattedDate(dataset.timestamp));
    }

    @Override
    public int getItemCount() {
        return mObjects == null ? 0 : mObjects.size();
    }

    private void showPopup(final DatasetViewHolder viewHolder) {
        final PopupMenu popupMenu = new PopupMenu(viewHolder.itemView.getContext(),
                viewHolder.overflowButton);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = viewHolder.getAdapterPosition();
                if(position == RecyclerView.NO_POSITION) {
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
