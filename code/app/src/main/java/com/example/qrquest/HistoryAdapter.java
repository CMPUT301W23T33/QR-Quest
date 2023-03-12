package com.example.qrquest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import java.util.Objects;

public class HistoryAdapter extends ListAdapter<QRCodeHistory, HistoryViewHolder> {

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return HistoryViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        QRCodeHistory history = getItem(position);
        holder.bind(history);
    }

    protected HistoryAdapter(@NonNull DiffUtil.ItemCallback<QRCodeHistory> diffCallback) {
        super(diffCallback);
    }

    protected HistoryAdapter(@NonNull AsyncDifferConfig<QRCodeHistory> config) {
        super(config);
    }

    static class historyDiff extends DiffUtil.ItemCallback<QRCodeHistory>{

        @Override
        public boolean areItemsTheSame(@NonNull QRCodeHistory oldItem, @NonNull QRCodeHistory newItem) {
            return Objects.equals(oldItem.getQrCode(), newItem.getQrCode());
        }

        @Override
        public boolean areContentsTheSame(@NonNull QRCodeHistory oldItem, @NonNull QRCodeHistory newItem) {
            return Objects.equals(oldItem.getQrCode(), newItem.getQrCode());
        }
    }

}
