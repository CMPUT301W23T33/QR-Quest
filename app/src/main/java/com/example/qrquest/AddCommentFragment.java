package com.example.qrquest;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.qrquest.databinding.FragmentDialogLayoutBinding;


/**
 * This class represent the Add Comment Dialog.It prompts for the user's comment.
 * @author Thea Nguyen
 */
public class AddCommentFragment extends DialogFragment {
    FragmentDialogLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // transparent background
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // button close
        binding.buttonClose.setOnClickListener(v -> dismiss());

        // button add
        binding.buttonCheck.setOnClickListener(v -> {
            String comment = binding.commentText.getText().toString();
            Log.d("COMMENT", comment);
            // PUSH COMMENT TO DATABASE HERE


            dismiss();
        });

        return view;
    }
}
