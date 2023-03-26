package com.example.qrquest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.Objects;

/**
 * This class defines the adapter for displaying the QR Code history.
 * @author Dang Viet Anh Dinh
 */
public class QRHistoryAdapter extends ListAdapter<QRCodeHistory, QRHistoryViewHolder> {

    @NonNull
    @Override
    public QRHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return QRHistoryViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull QRHistoryViewHolder holder, int position) {
        QRCodeHistory history = getItem(position);
        holder.bind(history);
    }

    protected QRHistoryAdapter(@NonNull DiffUtil.ItemCallback<QRCodeHistory> diffCallback) {
        super(diffCallback);
    }

    protected QRHistoryAdapter(@NonNull AsyncDifferConfig<QRCodeHistory> config) {
        super(config);
    }

    static class historyDiff extends DiffUtil.ItemCallback<QRCodeHistory>{

        @Override
        public boolean areItemsTheSame(@NonNull QRCodeHistory oldItem, @NonNull QRCodeHistory newItem) {
            return Objects.equals(oldItem.getQrCode(), newItem.getQrCode());
        }

        @Override
        public boolean areContentsTheSame(@NonNull QRCodeHistory oldItem, @NonNull QRCodeHistory newItem) {
            return oldItem.getScore() == newItem.getScore();
        }
    }

}
