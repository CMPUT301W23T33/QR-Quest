package com.example.qrquest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.Objects;

/**
 * This class defines the adapter for displaying the player
 * @author Dang Viet Anh Dinh
 */
public class PlayerAdapter extends ListAdapter<Player, PlayerViewHolder> {

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlayerViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player item = getItem(position);
        holder.bind(item);
    }

    protected PlayerAdapter(@NonNull DiffUtil.ItemCallback<Player> diffCallback) {
        super(diffCallback);
    }

    protected PlayerAdapter(@NonNull AsyncDifferConfig<Player> config) {
        super(config);
    }

    static class playerDiff extends DiffUtil.ItemCallback<Player>{

        @Override
        public boolean areItemsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
            return Objects.equals(oldItem.getUsername(), newItem.getUsername());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
            return Objects.equals(oldItem.getEmailAddress(), newItem.getEmailAddress());
        }
    }

}
