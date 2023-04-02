package com.example.qrquest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
* This class defines the view holder for QR Code
* @author Dang Viet Anh Dinh
*/
public class HistoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView QRName, QRScore;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        QRName = itemView.findViewById(R.id.qr_code_display_name);
        QRScore = itemView.findViewById(R.id.qr_code_display_score);
    }

    public void bind(History history){
        QRName.setText(history.getQrCode());
        QRScore.setText(String.valueOf(history.getScore()));
    }

    static HistoryViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qr_code_display, parent, false);
        return new HistoryViewHolder(view);
    }

}
