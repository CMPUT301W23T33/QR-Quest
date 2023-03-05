package com.example.base;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddWordFragment extends Fragment {

    Button button, toCamera, search;
    TextView score, longitude, latitude;
    EditText editText;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    private final ActivityResultLauncher<Intent> useCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

        }
    });


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get the view model under the scope of the activity
        PlayerViewModel playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);

        // Set up Firestore database
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("QR Code");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_add_word, container, false);

        button = view.findViewById(R.id.button_save);
        toCamera = view.findViewById(R.id.add_word_fragment_to_camera);
        search = view.findViewById(R.id.search);
        editText = view.findViewById(R.id.edit_word);
        score = view.findViewById(R.id.qrcodescore);
        latitude = view.findViewById(R.id.qrcodeslatitude);
        longitude = view.findViewById(R.id.qrcodelongitude);

        // Navigate back to the main fragment
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_addWordFragment_to_mainFragment);
            }
        });

        // Launching Camera
        toCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), CameraFeature.class);
                useCamera.launch(intent);
            }
        });

        // Search something in the Firestore database
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionReference.document(editText.getText().toString().trim()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    QRCode qrCode = documentSnapshot.toObject(QRCode.class);
                                    score.setText(String.format("%.2f", qrCode.getScore()));
                                    longitude.setText(String.format("%.2f", qrCode.getLongitude()));
                                    latitude.setText(String.format("%.2f", qrCode.getLatitude()));
                                }
                                else{
                                    Toast.makeText(requireContext(), "Such item does not exist", Toast.LENGTH_SHORT).show();
                                    score.setText(String.format("%s", "N/A"));
                                    longitude.setText(String.format("%s", "N/A"));
                                    latitude.setText(String.format("%s", "N/A"));
                                }
                            }})
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireContext(), "There has been a problem retrieving such item", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;

    }

}