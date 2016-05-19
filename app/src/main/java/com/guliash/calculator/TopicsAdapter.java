package com.guliash.calculator;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicViewHolder> {

    private ArrayList<Topic> mTopics;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void itemClicked(Topic topic);
    }

    public TopicsAdapter(Callbacks callbacks, @Nullable ArrayList<Topic> topics) {
        this.mTopics = topics;
        this.mCallbacks = callbacks;
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TopicViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.name);
        }
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final TopicViewHolder holder = new TopicViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.topic_item, parent, false));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION) {
                    mCallbacks.itemClicked(mTopics.get(pos));
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        holder.name.setText(mTopics.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mTopics == null ? 0 : mTopics.size();
    }
}
