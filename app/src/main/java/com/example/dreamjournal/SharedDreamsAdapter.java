package com.example.dreamjournal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamjournal.data.Data;
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

public class SharedDreamsAdapter extends RecyclerView.Adapter<SharedDreamsAdapter.ViewHolder> {

    private static final String TAG = "SharedDreamsAdapter";
    ArrayList<SharedDream> sharedDreamsList = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SharedDream currentDream;
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView dateTextView;

        private final TextView sender;

        private final ImageView favoriteSharedDreamButton;
        private static final String TAG = "ViewHolder";

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            titleTextView =  view.findViewById(R.id.sharedDreamItemTitle);
            descriptionTextView =  view.findViewById(R.id.sharedDreamItemDescription);
            dateTextView  =  view.findViewById(R.id.sharedDreamItemDate);
            sender =  view.findViewById(R.id.sharedDreamSenderUsername);

            favoriteSharedDreamButton = view.findViewById(R.id.favoriteSharedDreamButton);



            favoriteSharedDreamButton.setOnClickListener(new View.OnClickListener() {
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
                                String currentDreamID = currentDream.getDreamID();
                                if (Data.userID.equals(userID) && currentDreamID.equals(dreamID)) {
                                    favorite.getRef().removeValue();
                                    contains = true;

                                }

                            }
                            if (!contains) {
                                String uuid = UUID.randomUUID().toString();
                                myRef.child(uuid).child("userID").setValue(Data.userID);
                                myRef.child(uuid).child("dreamID").setValue(currentDream.getDreamID());

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            });
        }

        public SharedDream getCurrentDream() {
            return currentDream;
        }

        public void setCurrentDream(SharedDream currentDream) {
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

        public ImageView getFavoriteSharedDreamButton() {
            return favoriteSharedDreamButton;
        }
    }

    public SharedDreamsAdapter(ArrayList<SharedDream> dataSet) {
        sharedDreamsList = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shared_dream_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        viewHolder.setCurrentDream(sharedDreamsList.get(position));
        viewHolder.getTitleTextView().setText(sharedDreamsList.get(position).getTitle());
        long timeStamp = sharedDreamsList.get(position).getDate();
        Date date = new java.util.Date((long)timeStamp*1000);
        String newstring = new SimpleDateFormat("MM/dd/yyyy").format(date);

        if (sharedDreamsList.get(position).isStarred()){
            viewHolder.getFavoriteSharedDreamButton().setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.ic_filled_star));
        } else {
            viewHolder.getFavoriteSharedDreamButton().setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.ic_star));
        }

        viewHolder.getDateTextView().setText(newstring);
        viewHolder.getDescriptionTextView().setText(sharedDreamsList.get(position).getDescription());
        viewHolder.getSender().setText(sharedDreamsList.get(position).getSender());
    }

    @Override
    public int getItemCount() {
        return sharedDreamsList.size();
    }
}
