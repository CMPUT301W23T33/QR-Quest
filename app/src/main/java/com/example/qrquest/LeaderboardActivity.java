package com.example.qrquest;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * This class represents the Leaderboard Screen of the app. It displays four fragments representing
 * four different leaderboards using swipe (viewPager2 and TabLayout).
 * @author Thea Nguyen
 */
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

        // setup Tab Layout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText("")
        ).attach();

        // Initialize view model
        LeaderboardViewModel viewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        // Observe type of leaderboard
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewModel.setLeaderboardPosition(position);
            }
        });

        // button back
        ImageButton buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(v -> {
            viewModel.refreshHistory();
            LeaderboardFragment.refreshHistory();
            finish();
        });

    }

    /**
     * This class acts as an adapter for the viewPager2.
     */
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
