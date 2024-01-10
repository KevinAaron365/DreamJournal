package com.example.dreamjournal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamjournal.data.Data;
import com.example.dreamjournal.data.FavoriteDream;
import com.example.dreamjournal.data.SharedDream;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class FavoriteDreamsAdapter extends RecyclerView.Adapter<FavoriteDreamsAdapter.ViewHolder> {

    private static final String TAG = "SharedDreamsAdapter";
    ArrayList<FavoriteDream> favoriteDreamsList = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private FavoriteDream currentDream;
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView dateTextView;

        private ImageView removeFavoriteDreamButton;

        private final TextView sender;

        private static final String TAG = "ViewHolder";

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            titleTextView =  view.findViewById(R.id.favoriteDreamItemTitle);
            descriptionTextView =  view.findViewById(R.id.favoriteDreamItemDescription);
            dateTextView  =  view.findViewById(R.id.favoriteDreamItemDate);
            sender =  view.findViewById(R.id.favoriteDreamSenderUsername);
            removeFavoriteDreamButton = view.findViewById(R.id.removeFavButton);


            removeFavoriteDreamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("favs");

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean contains = false;
                            for (DataSnapshot favorite : snapshot.getChildren()) {
                                String userID = favorite.child("userID").getValue(String.class);
                                String dreamID = favorite.child("dreamID").getValue(String.class);
//                                favorite.getRef().removeValue();

                                String currentDreamID = currentDream.getDreamID();
                                if (Data.userID.equals(userID) && currentDreamID.equals(dreamID)) {
                                    favorite.getRef().removeValue();
                                    contains = true;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            });
        }

        public FavoriteDream getCurrentDream() {
            return currentDream;
        }

        public void setCurrentDream(FavoriteDream currentDream) {
            this.currentDream = currentDream;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDescriptionTextView() {
            return descriptionTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

        public TextView getSender() {
            return sender;
        }

    }

    public FavoriteDreamsAdapter(ArrayList<FavoriteDream> dataSet) {
        favoriteDreamsList = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.favorite_dream_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        viewHolder.setCurrentDream(favoriteDreamsList.get(position));
        viewHolder.getTitleTextView().setText(favoriteDreamsList.get(position).getTitle());
        long timeStamp = favoriteDreamsList.get(position).getDate();
        Date date = new Date((long)timeStamp*1000);
        String newstring = new SimpleDateFormat("MM/dd/yyyy").format(date);

        viewHolder.getDateTextView().setText(newstring);
        viewHolder.getDescriptionTextView().setText(favoriteDreamsList.get(position).getDescription());
        viewHolder.getSender().setText(favoriteDreamsList.get(position).getSender());
    }

    @Override
    public int getItemCount() {
        return favoriteDreamsList.size();
    }
}
