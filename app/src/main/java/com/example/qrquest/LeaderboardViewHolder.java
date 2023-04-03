package com.example.qrquest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class defines the view holder for the leaderboard
 * @author Dang Viet Anh Dinh
 */
public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

    private final TextView ranking, username, value;

    public LeaderboardViewHolder(@NonNull View itemView) {
        super(itemView);
        ranking = itemView.findViewById(R.id.ranking);
        username = itemView.findViewById(R.id.name_text_display);
        value = itemView.findViewById(R.id.score_text_display);
    }

    public void bind(Rank rank){
        ranking.setText(String.valueOf(rank.getRank()));
        username.setText(rank.getIdentifier());
        value.setText(String.valueOf(rank.getValue()));
    }

    static LeaderboardViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display, parent, false);
        return new LeaderboardViewHolder(view);
    }

}
