package com.example.dreamjournal.ui.calendar;


import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.dreamjournal.R;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.ViewContainer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

class DayViewContainer extends ViewContainer {


    private TextView textView;

    private CalendarDay day;

    private TextView selectedDateTextView;


    public final MutableLiveData<LocalDate> selectedDate;

    public MutableLiveData<LocalDate> getSelectedDate() {
        return selectedDate;
    }

    public DayViewContainer(@NonNull View view, MutableLiveData<LocalDate> selectedDate, CalendarView calendarView, TextView selectedDateTextView) {
        super(view);
        this.selectedDate = selectedDate;
        this.selectedDateTextView = selectedDateTextView;
        textView = view.findViewById(R.id.calendarDayText);



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
        day = calendarDay;
        textView.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));

        textView.setOnClickListener(new View.OnClickListener() {
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
                            DayViewContainer.this.selectedDateTextView.setText(selectedDate.getValue().format(DateTimeFormatter.ofPattern("MM / dd / yyyy")));
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
                textView.setBackgroundResource(R.drawable.selection_background);
                Log.d("date", "selected date: " + selectedDate.getValue().toString());
            }
            else {
                textView.setTextColor(Color.BLACK);
                textView.setBackground(null);
            }

        }  else {
            textView.setTextColor(Color.GRAY);
        }

//
        if (calendarDay.getDate().toString().equals(todayDate.toString()) && selectedDate.getValue() == null)  {
            selectedDate.setValue(todayDate);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.selection_background);
            if (selectedDate.getValue() != null) {
                DayViewContainer.this.selectedDateTextView.setText(selectedDate.getValue().format(DateTimeFormatter.ofPattern("MM / dd / yyyy")));
            }
        }
    }
}