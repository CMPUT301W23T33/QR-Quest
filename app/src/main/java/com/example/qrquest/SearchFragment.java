package com.example.qrquest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

/**
 * This class defines the search screen
 * @author Dang Viet Anh Dinh
 */
public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    SearchViewModel viewModel;
    TextInputEditText editText;
    FirebaseFirestore db;
    String searchKeyword, lastSearchedKeyword;
    ImageButton buttonBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.search_screen_recycler_view);
        editText = view.findViewById(R.id.search_screen_text);
        buttonBack = view.findViewById(R.id.search_screen_button_back);
        searchAdapter = new SearchAdapter(new SearchAdapter.searchDiff());
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // Adding touch access to the recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Swipe to remove a QR Code from the account
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                String otherPlayerName = viewModel.getPlayerName(position);
                SharedPreferences sharedPref = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String username = sharedPref.getString("username", "");
                if (username.equals(otherPlayerName)) {
                    editor.putBoolean("myProfile", false);
                    editor.putString("otherPlayer", otherPlayerName);
                }
                else{
                    editor.putBoolean("myProfile", true);
                }
                editor.putBoolean("searching", true);
                editor.apply();
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_profileFragment2);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Initialize view model and Firestore database connection
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        db = FirebaseFirestore.getInstance();

        // Set up the search result for display
        searchKeyword = "";
        viewModel.setSearchResult(db, searchKeyword);

        // Get the search result to observe
        viewModel.getSearchResult().observe(requireActivity(), ranks -> searchAdapter.submitList(ranks));

        // Get searching state to observe
        viewModel.getSearchingDone().observe(requireActivity(), aBoolean -> {
            if (aBoolean){
                if (!Objects.equals(searchKeyword, lastSearchedKeyword)){
                    updateScreen();
                }
            }
        });

        // Get the most recent searched keyword to observe
        viewModel.getLastSearchedKeyword().observe(requireActivity(), s -> lastSearchedKeyword = s);

        // Get the most up-to-date keyword entered
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchKeyword = s.toString().trim();
                updateScreen();
            }
        });

        // Navigate back to the main screen
        buttonBack.setOnClickListener(v -> requireActivity().finish());

        return view;

    }

    // Update UI according to the search keyword
    private void updateScreen(){
        viewModel.setSearchResult(db, searchKeyword);
    }

}