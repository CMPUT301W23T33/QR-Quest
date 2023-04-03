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
public class HistoryAdapter extends ListAdapter<History, HistoryViewHolder> {

    private ItemClickListener listener;
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return HistoryViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = getItem(position);
        holder.bind(history);

        holder.itemView.setOnClickListener(v -> listener.onClick(v, holder.getAbsoluteAdapterPosition()));
    }

    protected HistoryAdapter(@NonNull DiffUtil.ItemCallback<History> diffCallback) {
        super(diffCallback);
    }

    protected HistoryAdapter(@NonNull AsyncDifferConfig<History> config) {
        super(config);
    }

    static class historyDiff extends DiffUtil.ItemCallback<History>{

        @Override
        public boolean areItemsTheSame(@NonNull History oldItem, @NonNull History newItem) {
            return Objects.equals(oldItem.getQrCode(), newItem.getQrCode());
        }

        @Override
        public boolean areContentsTheSame(@NonNull History oldItem, @NonNull History newItem) {
            return oldItem.getScore() == newItem.getScore();
        }
    }

    public void setClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

}
