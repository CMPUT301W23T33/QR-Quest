package com.example.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

class DisplayViewHolder extends RecyclerView.ViewHolder {
    private final TextView wordItemView;

    private DisplayViewHolder(View itemView) {
        super(itemView);
        wordItemView = itemView.findViewById(R.id.recyclerview_item);
    }

    public void bind(String text) {
        wordItemView.setText(text);
    }

    static DisplayViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new DisplayViewHolder(view);
    }
}
