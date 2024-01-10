package com.example.dreamjournal.ui.favorites;


import static androidx.fragment.app.FragmentManager.TAG;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dreamjournal.DreamsAdapter;
import com.example.dreamjournal.FavoriteDreamsAdapter;
import com.example.dreamjournal.R;
import com.example.dreamjournal.data.Data;
import com.example.dreamjournal.data.Dream;
import com.example.dreamjournal.data.FavoriteDream;
import com.example.dreamjournal.data.SharedDream;
import com.example.dreamjournal.databinding.FragmentDashboardBinding;
import com.example.dreamjournal.databinding.FragmentFavoriteBinding;
import com.example.dreamjournal.ui.AddDreamActivity;
import com.example.dreamjournal.ui.dashboard.DashboardViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class FavoritesFragment extends Fragment {

    private FavoritesViewModel mViewModel;
    private FragmentFavoriteBinding binding;



    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FavoritesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<FavoriteDream> dreamsList = new ArrayList<>();
//        dreamsList.add(new Dream(234234, "Dream Title", "This is the description of the dream"));


        FavoriteDreamsAdapter da = new FavoriteDreamsAdapter(dreamsList);

        RecyclerView recyclerView = root.findViewById(R.id.favoritesRecyclerView);
        recyclerView.setAdapter(da);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference().child("favs");
        // Read from the database

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dreamsList.clear();
                for (DataSnapshot favorite : snapshot.getChildren()) {
                    String userID = favorite.child("userID").getValue(String.class);
                    String favoriteDreamID = favorite.child("dreamID").getValue(String.class);
                    Log.d("UserID", "User id: " + userID);
                    Log.d("CurrentUserID", "Current user id: " + Data.userID);



                    if (Data.userID.equals(userID)) {
                        DatabaseReference myRef = database.getReference();
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot d) {
                                for (DataSnapshot dataSnapshot : d.getChildren()) {
                                    String userID = dataSnapshot.getKey();
                                    String username = dataSnapshot.child("username").getValue(String.class);
//                                    Log.d("NewUserID", "New user id: ");

                                    DataSnapshot dreamData = d.child(userID).child("dreams");

                                    for (DataSnapshot dreamD : dreamData.getChildren()) {

                                        String dreamID = dreamD.getKey().toString();
                                        Log.d("NewUserID", "New user id: ");

                                        String dreamTitle = dreamD.child("title").getValue(String.class);
                                        long dreamDate = !dreamD.child("date").exists() ? 0 : dreamD.child("date").getValue(Long.class);
                                        String dreamDescription = dreamD.child("description").getValue(String.class);
                                        String isPublic = dreamD.child("isPublic").getValue(String.class);

                                        if (favoriteDreamID.equals(dreamID)) {
                                            dreamsList.add(new FavoriteDream(userID, dreamD.getKey(), dreamDate, dreamTitle, dreamDescription, isPublic, username));
                                        }
                                    }

                                }
                                da.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
                da.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}