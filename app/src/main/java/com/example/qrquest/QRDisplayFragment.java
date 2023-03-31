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

import android.util.Log;
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
    String uri, username, qrName;
    double latitude;
    double longitude;
    RecyclerView recyclerView;
    QRDisplayViewModel viewModel;
    FirebaseFirestore db;
    CommentAdapter adapter;
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

        if (bundle.getString("uri") != null)
            uri = bundle.getString("uri");

        // set up viewPager2
        if (uri != null) {
            String[] imageURIs = {uri};
            arrayList = new ArrayList<>();
            for (String imageURI : imageURIs) arrayList.add(new VPItem(imageURI));
        }
        // for demo (MUST BE MODIFIED AFTER HAVING A VISUAL REPRESENTATION)
        else {
            int[] imageIDs = {R.drawable.qr_logo_big, R.drawable.qr_logo_big};
            arrayList = new ArrayList<>();
            for (int imageID : imageIDs) arrayList.add(new VPItem(imageID));
        }

        VPAdapter vpAdapter = new VPAdapter(arrayList);
        binding.pager.setAdapter(vpAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> tab.setText("")
        ).attach();

        // Initialize Firestore database connection
        db = FirebaseFirestore.getInstance();

        // Initialize view model
        viewModel = new ViewModelProvider(requireActivity()).get(QRDisplayViewModel.class);

        //
        viewModel.setComments(db, username, qrName);

        //
        viewModel.setScanned(scanned);

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

            recyclerView = bottomSheetView.findViewById(R.id.user_list);
            adapter = new CommentAdapter(new CommentAdapter.commentDiff());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            buttonAdd = bottomSheetView.findViewById(R.id.button_add);

            // Get the comment(s) to observe changes
            viewModel.getComments().observe(requireActivity(), qrCodeComments -> adapter.submitList(qrCodeComments));

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

            // button add
            buttonAdd.setOnClickListener(v1 -> {
                AddCommentFragment fragment = new AddCommentFragment();
                fragment.show(requireActivity().getSupportFragmentManager(), "Dialog");
            });

        });

        return view;
    }

}