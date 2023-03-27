package com.example.qrquest;

import android.content.Context;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrquest.databinding.FragmentDialogLayoutBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


/**
 * This class represent the Add Comment Dialog.It prompts for the user's comment.
 * @author Thea Nguyen
 * @author Dang Viet Anh Dinh
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

            // Initialize database connection, view model and get username
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            QRDisplayViewModel viewModel = new ViewModelProvider(requireActivity()).get(QRDisplayViewModel.class);
            String username = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE).getString("username", "");

            // Add/Override comment
            viewModel.addComment(db, username, comment);

            dismiss();
        });

        return view;
    }
}
