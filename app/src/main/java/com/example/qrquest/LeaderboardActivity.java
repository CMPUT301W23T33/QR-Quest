package com.example.qrquest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class LeaderboardActivity extends AppCompatActivity {
    private static final int numPages = 4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // setup viewpager2 for navigation between leaderboards
        ViewPager2 viewPager2 = findViewById(R.id.pager);
        FragmentStateAdapter adapter = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(adapter);
    }

    // setup pager adapter
    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(@NonNull AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            LeaderboardFragment fragment = new LeaderboardFragment();

            // send position of leaderboards to change layouts + data accordingly
            Bundle bundle = new Bundle();
            bundle.putString("numLeaderboard", String.valueOf(position));
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getItemCount() {
            return numPages;
        }
    }
}
