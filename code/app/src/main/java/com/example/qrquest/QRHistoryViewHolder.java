package com.example.qrquest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QRHistoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView QRName, QRScore;

    public QRHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        QRName = itemView.findViewById(R.id.qr_code_display_name);
        QRScore = itemView.findViewById(R.id.qr_code_display_score);
    }

    public void bind(QRCodeHistory history){
        QRName.setText(history.getQrCode());
        QRScore.setText(String.valueOf(history.getScore()));
    }

    static QRHistoryViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qr_code_display, parent, false);
        return new QRHistoryViewHolder(view);
    }

}
