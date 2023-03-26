package com.example.qrquest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.Objects;

/**
 * This class defines the adapter for displaying the leaderboard.
 * @author Dang Viet Anh Dinh
 */
public class LeaderboardAdapter extends ListAdapter<Rank, LeaderboardViewHolder> {

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return LeaderboardViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        Rank item = getItem(position);
        holder.bind(item);
    }

    protected LeaderboardAdapter(@NonNull DiffUtil.ItemCallback<Rank> diffCallback) {
        super(diffCallback);
    }

    protected LeaderboardAdapter(@NonNull AsyncDifferConfig<Rank> config) {
        super(config);
    }

    static class rankDiff extends DiffUtil.ItemCallback<Rank>{

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
