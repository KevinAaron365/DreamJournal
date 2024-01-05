package com.example.dreamjournal.ui.home;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamjournal.DreamsAdapter;
import com.example.dreamjournal.R;
import com.example.dreamjournal.SharedDreamsAdapter;
import com.example.dreamjournal.data.Data;
import com.example.dreamjournal.data.Dream;
import com.example.dreamjournal.data.SharedDream;
import com.example.dreamjournal.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        ArrayList<SharedDream> dreamsList = new ArrayList<>();
        String userData = getActivity().getIntent().getStringExtra("id");

        SharedDreamsAdapter da = new SharedDreamsAdapter(dreamsList);


        RecyclerView recyclerView = root.findViewById(R.id.sharedDreamsRecyclerView);
        recyclerView.setAdapter(da);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));





//        TextView user = root.findViewById(R.id.userData);userData

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dreamsList.clear();
                for (DataSnapshot d:
                        dataSnapshot.getChildren()) {
                    String userID = d.getKey();
                    String username = d.child("username").getValue(String.class);

                    DataSnapshot dreamData = d.child("dreams");

                    for (DataSnapshot dreamD: dreamData.getChildren()) {

                        String dreamTitle = dreamD.child("title").getValue(String.class);
                        long dreamDate = !dreamD.child("date").exists() ? 0 : dreamD.child("date").getValue(Long.class);
                        String dreamDescription = dreamD.child("description").getValue(String.class);
                        String isPublic = dreamD.child("isPublic").getValue(String.class);


                        if (isPublic != null ? isPublic.equals("true") : false) {
                            // load liked data
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference().child("favs");

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean contains = false;

                                    for (DataSnapshot favorite : snapshot.getChildren()) {
                                        String userID = favorite.child("userID").getValue(String.class);
                                        String dreamID = favorite.child("dreamID").getValue(String.class);
                                        String currentDreamID = dreamD.getKey();
                                        if (Data.userID.equals(userID) && currentDreamID.equals(dreamID)) {
                                            contains = true;
                                        }

                                    }


                                    dreamsList.add(new SharedDream(userID, dreamD.getKey(), dreamDate, dreamTitle, dreamDescription, isPublic, username, contains) );
                                    da.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}