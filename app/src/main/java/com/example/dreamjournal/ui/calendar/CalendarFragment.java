package com.example.dreamjournal.ui.calendar;

import static com.kizitonwose.calendar.core.ExtensionsKt.firstDayOfWeekFromLocale;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dreamjournal.CalendarDreamsAdapter;
import com.example.dreamjournal.R;
import com.example.dreamjournal.data.CalendarDream;
import com.example.dreamjournal.data.Data;
import com.example.dreamjournal.databinding.FragmentCalendarBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.TimeZone;

public class CalendarFragment extends Fragment {

    private CalendarViewModel mViewModel;

    private MutableLiveData<LocalDate> selectedDate = new MutableLiveData<>();

    public TextView selectedDateTextView;
    private static final String TAG = "CalendarFragment";

    private FragmentCalendarBinding binding;

    CalendarDreamsAdapter da;
    private RecyclerView recyclerView;

    private ArrayList<CalendarDream> calendarDreamsList;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        selectedDate.setValue(null);


        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        calendarDreamsList = new ArrayList<>();
        calendarDreamsList.add(new CalendarDream("89808980", "dfghjk-fghj", 4567, "title", "This is the description of the dream", "false"));



        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference().child(Data.userID).child("dreams");


        View view  = inflater.inflate(R.layout.fragment_calendar, container, false);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        selectedDateTextView = view.findViewById(R.id.textViewSelectedDate);


        da = new CalendarDreamsAdapter(calendarDreamsList);

        recyclerView = view.findViewById(R.id.calendarRecyclerView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(da);
//        recyclerView.setNestedScrollingEnabled(false);
//        calendarDreamsList.add(new CalendarDream("89808980", "dfghjk-fghj", 4567, "title", "This is the description of the dream", "false"));
        da.notifyDataSetChanged();

        selectedDate.observe(getViewLifecycleOwner(), new Observer<LocalDate>() {
            @Override
            public void onChanged(LocalDate localDate) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        calendarDreamsList.clear();

                        for (DataSnapshot d:
                                dataSnapshot.getChildren()) {

                            long dreamDate = !d.child("date").exists() ? 0 : d.child("date").getValue(Long.class);
                            Instant instant = Instant.ofEpochSecond(dreamDate);
                            ZoneId zoneId = ZoneId.systemDefault();
                            LocalDate localDate = instant.atZone(zoneId).toLocalDate();

                            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                            fmt.setTimeZone(TimeZone.getTimeZone("PST")); // your time zone

                            Log.d(TAG, "database date: " + localDate.toString() );

                            if (selectedDate.getValue() != null) {
                                Log.d(TAG, "Selected date: " + selectedDate.getValue().toString());
                                if (localDate.isEqual(selectedDate.getValue())) {
                                    Log.d("select", "On: Date selected");
                                    String dreamID = d.getKey();
                                    String dreamTitle = d.child("title").getValue(String.class);
                                    long date = !d.child("date").exists() ? 0 : d.child("date").getValue(Long.class);
                                    String dreamDescription = d.child("description").getValue(String.class);
                                    String isPublic = d.child("isPublic").getValue(String.class);

                                    calendarDreamsList.add(new CalendarDream(Data.userID, dreamID, dreamDate, dreamTitle, dreamDescription, isPublic));

                                }
                            }

                        }
                        da.notifyDataSetChanged();
                        view.invalidate();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view, selectedDate, calendarView, selectedDateTextView);

            }

            @Override
            public void bind(@NonNull DayViewContainer container, CalendarDay calendarDay) {
                container.onBindView(calendarDay, calendarView);
            }
        });

        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthHeader>() {
            @NonNull
            @Override
            public MonthHeader create(@NonNull View view) {
                return new MonthHeader(view);
            }

            @Override
            public void bind(@NonNull MonthHeader container, CalendarMonth calendarMonth) {
                container.textView.setText(calendarMonth.getYearMonth().getMonth().toString());
            }
        });

        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(100);  // Adjust as needed
        YearMonth endMonth = currentMonth.plusMonths(100);  // Adjust as needed
        DayOfWeek firstDayOfWeek = firstDayOfWeekFromLocale(); // Available from the library
        calendarView.setup(startMonth, endMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        // TODO: Use the ViewModel
    }



}