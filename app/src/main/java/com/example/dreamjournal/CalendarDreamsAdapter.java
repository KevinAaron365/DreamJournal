package com.example.dreamjournal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamjournal.R;
import com.example.dreamjournal.data.CalendarDream;
import com.example.dreamjournal.data.Dream;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarDreamsAdapter extends RecyclerView.Adapter<CalendarDreamsAdapter.ViewHolder> {
    private String[] localDataSet;
    private static final String TAG = "CalendarDreamsAdapter";
    ArrayList<CalendarDream> calendarDreamsList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CalendarDream currentDream;
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView dateTextView;

        public ViewHolder(View view) {
            super(view);


            titleTextView = (TextView) view.findViewById(R.id.calendarDreamItemTitle);
            descriptionTextView = (TextView) view.findViewById(R.id.calendarDreamItemDescription);
            dateTextView  = (TextView) view.findViewById(R.id.calendarDreamItemDate);

            DatabaseReference database;
            database = FirebaseDatabase.getInstance().getReference();



        }

        public CalendarDream getCurrentDream() {
            return currentDream;
        }

        public void setCurrentDream(CalendarDream currentDream) {
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


    public CalendarDreamsAdapter(ArrayList<CalendarDream> dataSet) {
        calendarDreamsList = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.calendar_dream_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.setCurrentDream(calendarDreamsList.get(position));
        viewHolder.getTitleTextView().setText(calendarDreamsList.get(position).getTitle());
        long timeStamp = calendarDreamsList.get(position).getDate();
        Date date = new java.util.Date((long)timeStamp*1000);
        String sdf = new SimpleDateFormat("MM/dd/yyyy").format(date);


        viewHolder.getDateTextView().setText(sdf);
        viewHolder.getDescriptionTextView().setText(calendarDreamsList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return calendarDreamsList.size();
    }
}
