package com.example.dreamjournal;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamjournal.data.Data;
import com.example.dreamjournal.data.Dream;
import com.example.dreamjournal.ui.EditDreamActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DreamsAdapter extends RecyclerView.Adapter<DreamsAdapter.ViewHolder> {

    private static final String TAG = "DreamsAdapter";
    private String[] localDataSet;
    ArrayList<Dream> dreamsList = new ArrayList<>();


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder  {

        private Dream currentDream;
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView dateTextView;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            titleTextView = (TextView) view.findViewById(R.id.dreamItemTitle);
            descriptionTextView = (TextView) view.findViewById(R.id.dreamItemDescription);
            dateTextView  = (TextView) view.findViewById(R.id.dreamItemDate);

            ImageButton editDreamButton = view.findViewById(R.id.editDreamButton);
            ImageButton deleteDreamButton = view.findViewById(R.id.deleteDreamButton);

            DatabaseReference database;
            database = FirebaseDatabase.getInstance().getReference();

            editDreamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(view.getContext(), EditDreamActivity.class);
                    i.putExtra("dreamID", currentDream.getDreamID());
                    i.putExtra("title", currentDream.getTitle());
                    i.putExtra("description", currentDream.getDescription());
                    i.putExtra("isPublic", currentDream.getIsPublic());
                    i.putExtra("date", currentDream.getDate());


                    startActivity(view.getContext(), i, null);

                }

            });

            deleteDreamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.child(Data.userID).child("dreams").child(currentDream.getDreamID()).removeValue();


                }
            });

        }


        public Dream getCurrentDream() {
            return currentDream;
        }

        public void setCurrentDream(Dream currentDream) {
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


    }

    public DreamsAdapter(ArrayList<Dream> dataSet) {
        dreamsList = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dream_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        viewHolder.setCurrentDream(dreamsList.get(position));
        viewHolder.getTitleTextView().setText(dreamsList.get(position).getTitle());
        long timeStamp = dreamsList.get(position).getDate();
        Date date = new java.util.Date((long)timeStamp*1000);
        String newstring = new SimpleDateFormat("MM/dd/yyyy").format(date);

        viewHolder.getDateTextView().setText(newstring);
        viewHolder.getDescriptionTextView().setText(dreamsList.get(position).getDescription());



    }

    @Override
    public int getItemCount() {
        return dreamsList.size();
    }
}
