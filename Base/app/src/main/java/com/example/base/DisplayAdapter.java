package com.example.base;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class DisplayAdapter extends ListAdapter<String, DisplayViewHolder>{

    @NonNull
    @Override
    public DisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return DisplayViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayViewHolder holder, int position) {
        String current = getItem(position);
        holder.bind(current);
    }

    protected DisplayAdapter(@NonNull DiffUtil.ItemCallback<String> diffCallback) {
        super(diffCallback);
    }

    static class playerDiff extends DiffUtil.ItemCallback<ExpandedPlayer>{

        @Override
        public boolean areItemsTheSame(@NonNull ExpandedPlayer oldItem, @NonNull ExpandedPlayer newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ExpandedPlayer oldItem, @NonNull ExpandedPlayer newItem) {
            return oldItem.getUniqueIdentifier().equals(newItem.getUniqueIdentifier());
        }
    }

    static class StringDiff extends DiffUtil.ItemCallback<String>{

        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    }

}
