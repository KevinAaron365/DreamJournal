package com.example.dreamjournal.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamjournal.DreamsAdapter;
import com.example.dreamjournal.MainActivity;
import com.example.dreamjournal.R;
import com.example.dreamjournal.data.Data;
import com.example.dreamjournal.data.Dream;
import com.example.dreamjournal.databinding.FragmentDashboardBinding;
import com.example.dreamjournal.ui.AddDreamActivity;
import com.example.dreamjournal.ui.AddDreamActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private static final String TAG = "DashboardFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<Dream> dreamsList = new ArrayList<>();
//        dreamsList.add(new Dream(234234, "Dream Title", "This is the description of the dream"));


        DreamsAdapter da = new DreamsAdapter(dreamsList);

        RecyclerView recyclerView = root.findViewById(R.id.dreamsRecyclerView);
        recyclerView.setAdapter(da);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference().child(Data.userID).child("dreams");
        // Read from the database

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dreamsList.clear();
                for (DataSnapshot d:
                        dataSnapshot.getChildren()) {
                    String dreamID = d.getKey();
                    String dreamTitle = d.child("title").getValue(String.class);
                    long dreamDate = !d.child("date").exists()? 0 : d.child("date").getValue(Long.class);
                    String dreamDescription = d.child("description").getValue(String.class);
                    String isPublic = d.child("isPublic").getValue(String.class);

                    dreamsList.add(new Dream(Data.userID,dreamID, dreamDate, dreamTitle, dreamDescription, isPublic) );
                }

                da.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button addDreamButton = root.findViewById(R.id.addDreamButton);
        addDreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("123", "Button clicked");

                Intent i = new Intent(getContext(), AddDreamActivity.class);

                startActivity(i);
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