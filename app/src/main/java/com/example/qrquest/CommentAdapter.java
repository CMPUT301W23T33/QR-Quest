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
public class CommentAdapter extends ListAdapter<Comment, CommentViewHolder> {

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CommentViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment item = getItem(position);
        holder.bind(item);
    }

    protected CommentAdapter(@NonNull DiffUtil.ItemCallback<Comment> diffCallback) {
        super(diffCallback);
    }

    protected CommentAdapter(@NonNull AsyncDifferConfig<Comment> config) {
        super(config);
    }

    static class commentDiff extends DiffUtil.ItemCallback<Comment>{

        @Override
        public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return Objects.equals(oldItem.getUsername(), newItem.getUsername());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return Objects.equals(oldItem.getComment(), newItem.getComment());
        }
    }

}
