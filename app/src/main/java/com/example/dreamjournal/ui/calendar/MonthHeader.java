package com.example.dreamjournal.ui.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dreamjournal.R;
import com.kizitonwose.calendar.view.ViewContainer;

public class MonthHeader extends ViewContainer {

    public TextView textView;
    DayViewContainer dayViewContainer;

    public MonthHeader(@NonNull View view) {
        super(view);
        textView = view.findViewById(R.id.monthHeaderTextView);

    }


}

