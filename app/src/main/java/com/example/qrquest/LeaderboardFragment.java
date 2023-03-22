package com.example.qrquest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qrquest.databinding.FragmentLeaderboardBinding;

public class LeaderboardFragment extends Fragment {

    FragmentLeaderboardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // get the leaderboard info (its order in the viewPager2 list)
        Bundle bundle = getArguments();
        String pos = "";
        if (bundle != null)
            pos = bundle.getString("numLeaderboard", "");
        int intPos = Integer.parseInt(pos);

        // LEADERBOARD 0 -> the highest scoring QR codes

        // LEADERBOARD 1 -> highest scoring QR codes in a region
        if (intPos == 1) {
            binding.nameLeaderboard.setText(R.string.leaderboard_1);
        }

        // LEADERBOARD 2 -> the most QR codes
        else if (intPos == 2) {
            binding.nameLeaderboard.setText(R.string.leaderboard_2);
        }

        // LEADERBOARD 3 -> the highest sum of QR codes
        else if (intPos == 3) {
            binding.nameLeaderboard.setText(R.string.leaderboard_3);
        }

        return view;
    }
}
