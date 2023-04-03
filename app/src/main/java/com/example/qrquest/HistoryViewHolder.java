package com.example.qrquest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
* This class defines the view holder for QR Code.
*/
public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static ItemClickListener listener;
    private final TextView QRName, QRScore;
    private final ImageView QRImage;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        QRName = itemView.findViewById(R.id.qr_code_display_name);
        QRScore = itemView.findViewById(R.id.qr_code_display_score);
        QRImage = itemView.findViewById(R.id.qr_code_display_picture);

        itemView.setOnClickListener(this);
    }

    public void bind(History history){
        QRName.setText(history.getQrCode());
        QRScore.setText(String.valueOf(history.getScore()));
        QRImage.setImageBitmap(Utilities.hashImage(history.getHashString()));
    }

    static HistoryViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qr_code_display, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) listener.onClick(v, getAbsoluteAdapterPosition());
    }
}
