package com.example.qrquest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrquest.databinding.FragmentLeaderboardBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class represents a leaderboard screen.
 * @author Thea Nguyen
 * @author Dang Viet Anh Dinh
 */
public class LeaderboardFragment extends Fragment {

    RecyclerView recyclerView;
    FragmentLeaderboardBinding binding;
    LeaderboardAdapter adapter;
    FirebaseFirestore db;
    LeaderboardViewModel viewModel;
    String username, region;
    private static boolean first = true;
    private static boolean second = true;
    private static boolean third = true;
    private static boolean last = true;

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

        recyclerView = view.findViewById(R.id.user_list);
        db = FirebaseFirestore.getInstance();
        adapter = new LeaderboardAdapter(new LeaderboardAdapter.rankDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // Get username and region
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        region = sharedPreferences.getString("region", "");

        // Initialize view model
        viewModel = new ViewModelProvider(requireActivity()).get(LeaderboardViewModel.class);

        // Get leaderboard to observe
        viewModel.getLeaderboard().observe(requireActivity(), ranks -> adapter.submitList(ranks));

        // Get the user to observe
        viewModel.getUser().observe(requireActivity(), this::updateScreen);

        // Get the top 1 player to observe
        viewModel.getFirst().observe(requireActivity(), rank -> {
            binding.nameTextDisplay1.setText(rank.getIdentifier());
            binding.scoreTextDisplay1.setText(String.valueOf(rank.getValue()));
        });

        // Get the top 2 player to observe
        viewModel.getSecond().observe(requireActivity(), rank -> {
            binding.nameTextDisplay2.setText(rank.getIdentifier());
            binding.scoreTextDisplay2.setText(String.valueOf(rank.getValue()));
        });

        // Get the top 3 player to observe
        viewModel.getThird().observe(requireActivity(), rank -> {
            binding.nameTextDisplay3.setText(rank.getIdentifier());
            binding.scoreTextDisplay3.setText(String.valueOf(rank.getValue()));
        });

        // Get type of leaderboard to observe
        viewModel.getLeaderboardPosition().observe(requireActivity(), this::updateScreen);

        // LEADERBOARD 0 -> the highest scoring QR codes
        if (first) {
            viewModel.setFirstLeaderboard(db, username);
            first = false;
        }

        // LEADERBOARD 1 -> highest scoring QR codes in a region
        if (intPos == 1)
            binding.nameLeaderboard.setText(getString(R.string.leaderboard_1, region));

        // LEADERBOARD 2 -> the most QR codes
        else if (intPos == 2)
            binding.nameLeaderboard.setText(R.string.leaderboard_2);

        // LEADERBOARD 3 -> the highest sum of QR codes
        else if (intPos == 3)
            binding.nameLeaderboard.setText(R.string.leaderboard_3);

        return view;
    }

    /**
     * This methods resets the type of leaderboard history
     */
    public static void refreshHistory(){
        first = true;
        second = true;
        third = true;
        last = true;
    }

    // Update user statistics
    private void updateScreen(Rank rank){
        binding.ranking.setText(String.valueOf(rank.getRank()));
        binding.nameTextDisplay.setText(username);
        binding.scoreTextDisplay.setText(String.valueOf(rank.getValue()));
    }

    // Update screen based on the type of leaderboard
    private void updateScreen(int integer){

        // First leaderboard
        if (integer == 0){
            if (first) {
                first = false;
                viewModel.setFirstLeaderboard(db, username);
            }
            second = true;
            third = true;
            last = true;
        }

        // Second leaderboard
        else if (integer == 1){
            if (second) {
                second = false;
                viewModel.setSecondLeaderboard(db, username, region);
            }

            first = true;
            third = true;
            last = true;
        }

        // Third leaderboard
        else if (integer == 2){
            if (third) {
                third = false;
                viewModel.setThirdLeaderboard(db, username);
            }
            first = true;
            second = true;
            last = true;
        }

        // Last leaderboard
        else{
            if (last) {
                last = false;
                viewModel.setLastLeaderboard(db, username);
            }
            first = true;
            second = true;
            third = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshHistory();
        refreshHistory();
        Log.d("LeaderboardFragment", "On Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.refreshHistory();
        refreshHistory();
    }
}
