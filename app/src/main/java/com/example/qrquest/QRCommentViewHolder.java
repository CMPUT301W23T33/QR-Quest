package com.example.qrquest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class defines the view holder for the comment
 * @author Dang Viet Anh Dinh
 */
public class QRCommentViewHolder extends RecyclerView.ViewHolder {

    private final TextView username, comment;

    public QRCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.name_text_display);
        comment = itemView.findViewById(R.id.comment_text_display);
    }

    public void bind(QRCodeComment item){
        username.setText(item.getUsername());
        comment.setText(item.getComment());
    }

    static QRCommentViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_display, parent, false);
        return new QRCommentViewHolder(view);
    }

}
