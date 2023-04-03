package com.example.qrquest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.Objects;

/**
 * This class defines the adapter for displaying the search
 * @author Dang Viet Anh Dinh
 */
public class SearchAdapter extends ListAdapter<Rank, SearchViewHolder> {

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SearchViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Rank item = getItem(position);
        holder.bind(item);
    }

    protected SearchAdapter(@NonNull DiffUtil.ItemCallback<Rank> diffCallback) {
        super(diffCallback);
    }

    protected SearchAdapter(@NonNull AsyncDifferConfig<Rank> config) {
        super(config);
    }

    static class searchDiff extends DiffUtil.ItemCallback<Rank>{

        @Override
        public boolean areItemsTheSame(@NonNull Rank oldItem, @NonNull Rank newItem) {
            return Objects.equals(oldItem.getIdentifier(), newItem.getIdentifier());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Rank oldItem, @NonNull Rank newItem) {
            return oldItem.getValue() == newItem.getValue();
        }
    }

}
