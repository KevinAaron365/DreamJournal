package com.example.dreamjournal.ui.calendar;


import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.dreamjournal.R;
import com.example.dreamjournal.data.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.ViewContainer;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

class DayViewContainer extends ViewContainer {

    private static final String TAG = "DayViewContainer";
    private TextView textView;

    private CalendarDay day;
    private boolean checked = false;

    int count = 0;

    private TextView selectedDateTextView;

    private ConstraintLayout calendarDayLayout;

    private TextView dreamsAmountTextView;

    public final MutableLiveData<LocalDate> selectedDate;

    public MutableLiveData<LocalDate> getSelectedDate() {
        return selectedDate;
    }

    public DayViewContainer(@NonNull View view, MutableLiveData<LocalDate> selectedDate, CalendarView calendarView, TextView selectedDateTextView) {
        super(view);
        this.selectedDate = selectedDate;
        this.selectedDateTextView = selectedDateTextView;
        textView = view.findViewById(R.id.calendarDayText);
        calendarDayLayout = view.findViewById(R.id.calendarDayLayout);
        dreamsAmountTextView = view.findViewById(R.id.dreamsAmountTextView);



        selectedDate.observeForever(new Observer<LocalDate>() {
            @Override
            public void onChanged(LocalDate todayDate) {
                if(day != null) {
                    onBindView(day, calendarView);
                }
            }
        });
    }

    public void onBindView(CalendarDay calendarDay, CalendarView calendarView) {
        if (!checked) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child(Data.userID).child("dreams");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dreamsAmountTextView.setText("");

                    for (DataSnapshot d : snapshot.getChildren()) {

                        long dreamDate = !d.child("date").exists() ? 0 : d.child("date").getValue(Long.class);
                        Instant instant = Instant.ofEpochSecond(dreamDate);
                        ZoneId zoneId = ZoneId.systemDefault();
                        LocalDate dreamLocalDate = instant.atZone(zoneId).toLocalDate();

                        Log.d(TAG, "database date: " + calendarDay.getDate().toString());

                        if (selectedDate.getValue() != null) {
                            Log.d(TAG, "Selected date: " + selectedDate.getValue().toString());
                            if (dreamLocalDate.isEqual(calendarDay.getDate())) {
                                count++;
                                if (count <= 3) {
                                    dreamsAmountTextView.setText(dreamsAmountTextView.getText() + ".");
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }


            });
            checked = true;

        }



        day = calendarDay;
        textView.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));

        calendarDayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Log.d("Message", "onClick: Day clicked ");

                if (calendarDay.getPosition() == DayPosition.MonthDate) {
                    LocalDate currentSelection = selectedDate.getValue();
                    if (currentSelection == calendarDay.getDate()) {
//                        selectedDate.setValue(null);
                    } else {
                        selectedDate.setValue(calendarDay.getDate());


                        if (selectedDate.getValue() != null) {
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL dd, yyyy");
//                            String formattedDate = selectedDate.getValue().format(formatter);
//                            DayViewContainer.this.selectedDate.setValue(selectedDate.getValue());
                            DayViewContainer.this.selectedDateTextView.setText(selectedDate.getValue().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
                        }
                            calendarView.notifyDateChanged(calendarDay.getDate());
                        if (currentSelection != null) {
                            calendarView.notifyDateChanged(currentSelection);
                        }
                    }
                }
            }
        });

        Date date = new Date();
        LocalDate todayDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Log.d("date", "day date: " + todayDate.toString());

        textView.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));

        if (calendarDay.getPosition() == DayPosition.MonthDate) {
            textView.setVisibility(View.VISIBLE);
            if (calendarDay.getDate()==selectedDate.getValue()) {
                textView.setTextColor(Color.WHITE);
                dreamsAmountTextView.setTextColor(Color.WHITE);
                calendarDayLayout.setBackgroundResource(R.drawable.selection_background);
                Log.d("date", "selected date: " + selectedDate.getValue().toString());
            }
            else {
                textView.setTextColor(Color.BLACK);
                dreamsAmountTextView.setTextColor(Color.BLACK);

                calendarDayLayout.setBackground(null);
            }

        }  else {
            textView.setTextColor(Color.GRAY);
        }

//
        if (calendarDay.getDate().toString().equals(todayDate.toString()) && selectedDate.getValue() == null)  {
            selectedDate.setValue(todayDate);
            textView.setTextColor(Color.WHITE);
            dreamsAmountTextView.setTextColor(Color.WHITE);

            calendarDayLayout.setBackgroundResource(R.drawable.selection_background);
            if (selectedDate.getValue() != null) {
                DayViewContainer.this.selectedDateTextView.setText(selectedDate.getValue().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
            }
        }
    }
}