package com.example.qrquest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class defines the view holder for the search
 * @author Dang Viet Anh Dinh
 */
public class SearchViewHolder extends RecyclerView.ViewHolder {

    private final TextView username, value;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.search_screen_name_text);
        value = itemView.findViewById(R.id.search_screen_score_text);
    }

    public void bind(Rank rank){
        username.setText(rank.getIdentifier());
        value.setText(String.valueOf(rank.getValue()));
    }

    static SearchViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_display, parent, false);
        return new SearchViewHolder(view);
    }

}
