package com.example.qrquest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.qrquest.databinding.FragmentQrDisplayBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;


/**
 * This class represents the QR Display Screen.
 * @author Thea Nguyen
 * @author Dang Viet Anh Dinh
 */
public class QRDisplayFragment extends Fragment {

    FragmentQrDisplayBinding binding;
    ArrayList<VPItem> arrayList;
    BottomSheetDialog dialog;
    View bottomSheetView;
    ImageButton buttonAdd;
    String hashString, username, qrName;
    double latitude, longitude;
    RecyclerView commentRecyclerView, playerRecyclerView;
    QRDisplayViewModel viewModel;
    FirebaseFirestore db;
    CommentAdapter commentAdapter;
    PlayerAdapter playerAdapter;
    boolean scanned;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQrDisplayBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = getArguments();
        assert bundle != null;

        sharedPreferences = requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        scanned = sharedPreferences.getBoolean("myQR", false);
        hashString = bundle.getString("hashString");
        qrName = bundle.getString("qrName");


        binding.qrNameText.setText(qrName);
        binding.qrScoreText.setText(String.valueOf(bundle.getInt("qrScore")));

        if (bundle.getString("latitude") != null) {
            latitude = Double.parseDouble(bundle.getString("latitude"));
            longitude = Double.parseDouble(bundle.getString("longitude"));
            binding.qrLocationText.setText(String.format(Locale.CANADA,"(%.2f,%.2f)", latitude, longitude));
        }
        else {
            String noLocation = "None";
            binding.qrLocationText.setText(noLocation);
        }

        arrayList = new ArrayList<>();
        arrayList.add(new VPItem(this, Utilities.hashImage(hashString)));
        if (bundle.getBoolean("isCloud", false))
            arrayList.add(new VPItem(this, bundle.getString("uri"),
                        bundle.getBoolean("isCloud", false)));
        VPAdapter vpAdapter = new VPAdapter(arrayList);
        binding.pager.setAdapter(vpAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> tab.setText("")
        ).attach();

        // Initialize Firestore database connection
        db = FirebaseFirestore.getInstance();

        // Initialize view model
        viewModel = new ViewModelProvider(requireActivity()).get(QRDisplayViewModel.class);

        // Set comment(s) for display
        viewModel.setComments(db, username, qrName);

        // Set user view
        viewModel.setScanned(scanned);

        // Set up the list of players who scanned the QR Code
        viewModel.setPlayers(db, qrName);

        // button back
        binding.buttonBack.setOnClickListener(v -> {
            if (!QRDisplayViewModel.getRefreshPermission()){
                viewModel.refreshHistory();
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("othersQR", false);
            editor.apply();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        // button arrow up
        binding.buttonOpen.setOnClickListener(v -> {

            // create dialog
            dialog = new BottomSheetDialog(requireActivity());
            bottomSheetView = LayoutInflater.from(requireActivity())
                    .inflate(R.layout.bottom_sheet_layout, container, false);
            dialog.setContentView(bottomSheetView);
            dialog.show();

            // Add button
            buttonAdd = bottomSheetView.findViewById(R.id.button_add);

            // Comment list
            commentRecyclerView = bottomSheetView.findViewById(R.id.user_list);
            commentAdapter = new CommentAdapter(new CommentAdapter.commentDiff());
            commentRecyclerView.setAdapter(commentAdapter);
            commentRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

            // Player list
            playerRecyclerView = bottomSheetView.findViewById(R.id.player_list);
            playerAdapter = new PlayerAdapter(new PlayerAdapter.playerDiff());
            playerRecyclerView.setAdapter(playerAdapter);
            playerRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

            // Get the comment(s) to observe
            viewModel.getComments().observe(requireActivity(), qrCodeComments -> commentAdapter.submitList(qrCodeComments));

            // Get if the user has scanned the QR Code to observe changes
            viewModel.getScanned().observe(requireActivity(), aBoolean -> {
                if (aBoolean){
                    buttonAdd.setVisibility(View.VISIBLE);
                }
                else{
                    buttonAdd.setVisibility(View.INVISIBLE);
                }
                buttonAdd.setEnabled(aBoolean);
            });

            // Get the player(s) who scanned the QR Code to observe
            viewModel.getPlayers().observe(requireActivity(), players -> playerAdapter.submitList(players));

            // Add/Modify comment
            buttonAdd.setOnClickListener(v1 -> {
                AddCommentFragment fragment = new AddCommentFragment();
                fragment.show(requireActivity().getSupportFragmentManager(), "Dialog");
            });

        });

        return view;
    }

}