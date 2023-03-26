package com.example.qrquest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrquest.databinding.FragmentLeaderboardBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class represents a leaderboard screen.
 * @author Thea Nguyen
 */
public class LeaderboardFragment extends Fragment {

    RecyclerView recyclerView;
    FragmentLeaderboardBinding binding;
    LeaderboardAdapter adapter;
    FirebaseFirestore db;
    LeaderboardViewModel viewModel;
    String username, region, ranking;
    private static boolean first = true;
    private static boolean second = true;
    private static boolean third =true;
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

        // Set up leaderboard
        viewModel.setLeaderboard();

        // Get leaderboard to observe
        viewModel.getLeaderboard().observe(requireActivity(), ranks -> adapter.submitList(ranks));

        // Get user and top player to observe
        viewModel.getUserAndTopPlayers().observe(requireActivity(), ranks -> updateScreen(ranks));

        // Get type of leaderboard to observe
        viewModel.getLeaderboardPosition().observe(requireActivity(), integer -> updateScreen(integer));

        // LEADERBOARD 0 -> the highest scoring QR codes
        if (first) {
            viewModel.setFirstLeaderboard(db, username);
            first = false;
        }

        // LEADERBOARD 1 -> highest scoring QR codes in a region
        if (intPos == 1) {
            binding.nameLeaderboard.setText(getString(R.string.leaderboard_1, region));
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

    // Update screen based on the input as the type of leaderboard
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
            binding.nameLeaderboard.setText(R.string.leaderboard_0);
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
            binding.nameLeaderboard.setText(getString(R.string.leaderboard_1, region));
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
            binding.nameLeaderboard.setText(R.string.leaderboard_2);
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
            binding.nameLeaderboard.setText(R.string.leaderboard_3);
        }
    }

    // Update screen based on leaderboard ranking
    private void updateScreen(ArrayList<Rank> ranks){

        // Update top 3 players
        String firstRanking = "1";
        String secondRanking, thirdRanking;
        if (ranks.get(0).getValue() == ranks.get(1).getValue()){
            secondRanking = firstRanking;
        }
        else{
            secondRanking = "2";
        }
        if (ranks.get(1).getValue() == ranks.get(2).getValue()){
            thirdRanking = secondRanking;
        }
        else{
            thirdRanking = "3";
        }
        binding.ranking1.setText(firstRanking);
        binding.ranking2.setText(secondRanking);
        binding.ranking3.setText(thirdRanking);
        binding.nameTextDisplay1.setText(ranks.get(0).getIdentifier());
        binding.scoreTextDisplay1.setText(String.valueOf(ranks.get(0).getValue()));
        binding.nameTextDisplay2.setText(ranks.get(1).getIdentifier());
        binding.scoreTextDisplay2.setText(String.valueOf(ranks.get(1).getValue()));
        binding.nameTextDisplay3.setText(ranks.get(2).getIdentifier());
        binding.scoreTextDisplay3.setText(String.valueOf(ranks.get(2).getValue()));

        // Update user ranking
        Rank user = ranks.get(3);
        ranking = "";
        if (user instanceof HighestScoreRank){
            ranking = String.valueOf(HighestScoreRank.getQueryRank(user.getValue()));
        }
        else if (user instanceof ScannedNumberRank){
            ranking = String.valueOf(ScannedNumberRank.getQueryRank(user.getValue()));
        }
        else if (user instanceof TotalScoreRank){
            ranking = String.valueOf(TotalScoreRank.getQueryRank(user.getValue()));
        }
        else if (user instanceof RegionalHighestScoreRank){
            ranking = String.valueOf(RegionalHighestScoreRank.getQueryRank(user.getValue()));
        }
        else{
            ranking = "0";
        }
        binding.ranking.setText(ranking);
        binding.nameTextDisplay.setText(username);
        binding.scoreTextDisplay.setText(String.valueOf(user.getValue()));
    }

    // Refresh type of leaderboard history
    public static void refreshHistory(){
        first = true;
        second = true;
        third = true;
        last = true;
    }

}
