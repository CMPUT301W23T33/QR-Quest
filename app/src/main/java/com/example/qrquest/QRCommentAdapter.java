package com.example.qrquest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.Objects;

/**
 * This class defines the adapter for displaying the comment
 * @author Dang Viet Anh Dinh
 */
public class QRCommentAdapter extends ListAdapter<QRCodeComment, QRCommentViewHolder> {

    @NonNull
    @Override
    public QRCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return QRCommentViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull QRCommentViewHolder holder, int position) {
        QRCodeComment item = getItem(position);
        holder.bind(item);
    }

    protected QRCommentAdapter(@NonNull DiffUtil.ItemCallback<QRCodeComment> diffCallback) {
        super(diffCallback);
    }

    protected QRCommentAdapter(@NonNull AsyncDifferConfig<QRCodeComment> config) {
        super(config);
    }

    static class commentDiff extends DiffUtil.ItemCallback<QRCodeComment>{

        @Override
        public boolean areItemsTheSame(@NonNull QRCodeComment oldItem, @NonNull QRCodeComment newItem) {
            return Objects.equals(oldItem.getUsername(), newItem.getUsername());
        }

        @Override
        public boolean areContentsTheSame(@NonNull QRCodeComment oldItem, @NonNull QRCodeComment newItem) {
            return Objects.equals(oldItem.getComment(), newItem.getComment());
        }
    }

}
