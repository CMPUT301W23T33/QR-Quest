package com.example.qrquest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class defines the view holder for the player
 * @author Dang Viet Anh Dinh
 */
public class PlayerViewHolder extends RecyclerView.ViewHolder {

    private final TextView username;

    public PlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.name_text_display);
    }

    public void bind(Player player){
        username.setText(player.getUsername());
    }

    static PlayerViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scanned_user_display, parent, false);
        return new PlayerViewHolder(view);
    }

}
