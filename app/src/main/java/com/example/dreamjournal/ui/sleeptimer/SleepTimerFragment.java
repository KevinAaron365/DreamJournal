package com.example.dreamjournal.ui.sleeptimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamjournal.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.dreamjournal.databinding.FragmentSleepTimerBinding;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SleepTimerFragment extends Fragment {
    public ProgressBar progressBar;
    public TextView timerProgressTextView;

    public Button startTimerButton;

    public TextInputEditText setTimerHours;

    public TextInputLayout setTimerHoursLayout;

    public Button pauseTimerButton;

    public Button resetTimerButton;

    public boolean pauseTimer = false;

    int i  = 0;

    int increment;
    int progress = 0;

    public Button resumeTimerButton;

    public boolean timerInProgress = false;


    private static final String TAG = "NotificationsFragment";
    private FragmentSleepTimerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SleepTimerViewModel notificationsViewModel =
                new ViewModelProvider(this).get(SleepTimerViewModel.class);

        binding = FragmentSleepTimerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = root.findViewById(R.id.timerProgressBar);
        timerProgressTextView = root.findViewById(R.id.timerProgressTextView);
        startTimerButton =  root.findViewById(R.id.startTimerButton);
        setTimerHours =  root.findViewById(R.id.editTextSetTimerHours);
        setTimerHoursLayout = root.findViewById(R.id.editTextSetHoursLayout);
        pauseTimerButton = root.findViewById(R.id.pauseTimerButton);
        resetTimerButton = root.findViewById(R.id.resetTimerButton);
        resumeTimerButton = root.findViewById(R.id.resumeTimerButton);
//        i=0;
//        progress = 0;
//        progressBar.setProgress(0);

        pauseTimerButton.setClickable(false);
        pauseTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));

        resumeTimerButton.setClickable(false);
        resumeTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));

        resetTimerButton.setClickable(false);
        resetTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));



        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validInput = true;
                if (setTimerHoursLayout.getEditText().getText().toString().equals("")) {
                    validInput = false;
                    setTimerHoursLayout.setError("Enter a number");
                    setTimerHoursLayout.setErrorIconDrawable(0);

                }
                if (validInput) {
                    i=0;
                    progress = 0;
                    progressBar.setProgress(progress);
                    pauseTimer = false;
                    setTimerHours.getBackground().clearColorFilter();
                    setTimerHoursLayout.setError(null);



                    timerInProgress = true;

                    int hours = Integer.parseInt(setTimerHoursLayout.getEditText().getText().toString());
                    int minutes = hours * 60;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (timerInProgress == true) {
                                resetTimerButton.setClickable(true);
                                resetTimerButton.setBackgroundColor(Color.parseColor("#D56F7D"));

                                startTimerButton.setClickable(false);
                                startTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));
                            }
                            if (timerInProgress == false) {
                                resetTimerButton.setClickable(false);
                                resetTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));

                                handler.removeCallbacks(this);
                                startTimerButton.setClickable(true);
                                startTimerButton.setBackgroundColor(Color.parseColor("#54A2C7"));

                                pauseTimerButton.setClickable(false);
                                pauseTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));

                                resumeTimerButton.setClickable(false);
                                resumeTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));

                            }
                            if(timerInProgress) {
                                if (i <= minutes) {
                                    if(pauseTimer) {
                                        pauseTimer = true;
                                        pauseTimerButton.setClickable(false);
                                        pauseTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));
                                        resumeTimerButton.setClickable(true);
                                        resumeTimerButton.setBackgroundColor(Color.parseColor("#54A2C7"));
                                    }
                                    if(!pauseTimer) {
                                        pauseTimer = false;
                                        pauseTimerButton.setClickable(true);
                                        resumeTimerButton.setClickable(false);
                                        pauseTimerButton.setBackgroundColor(Color.parseColor("#54A2C7"));
                                        resumeTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));
                                    }

                                    SimpleDateFormat sdf = new SimpleDateFormat("mm");
                                    try {
                                        Date date = sdf.parse(String.valueOf(i));
                                        sdf = new SimpleDateFormat("HH:mm");
                                        timerProgressTextView.setText(sdf.format(date));


                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    progress = (int) (i / (double) minutes * 100);
                                    progressBar.setProgress(progress);
                                    if (pauseTimer == false) {
                                        i = i + 1;
                                    }


                                    handler.postDelayed(this, 60000);
                                } else {
                                    handler.removeCallbacks(this);
                                    startTimerButton.setClickable(false);
                                    pauseTimerButton.setClickable(false);
                                    pauseTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));
                                    resumeTimerButton.setClickable(false);
                                    resumeTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));
                                    startTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                    builder1.setMessage("Timer Completed");
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            }
                                    );
                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                            }
                        }
                    }, 0);
                }

                }

        });

        pauseTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerInProgress == true) {
                    pauseTimer = true;
                    pauseTimerButton.setClickable(false);
                    pauseTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));
                    resumeTimerButton.setClickable(true);
                    resumeTimerButton.setBackgroundColor(Color.parseColor("#54A2C7"));
                }
            }
        });

        resumeTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerInProgress == true) {
                    pauseTimer = false;
                    pauseTimerButton.setClickable(true);
                    resumeTimerButton.setClickable(false);
                    pauseTimerButton.setBackgroundColor(Color.parseColor("#54A2C7"));
                    resumeTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));
                }

            }
        });



        resetTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                progress = 0;
                progressBar.setProgress(progress);
                pauseTimer = true;
                timerInProgress = false;

                resetTimerButton.setClickable(false);
                resetTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));

                startTimerButton.setClickable(true);
                startTimerButton.setBackgroundColor(Color.parseColor("#54A2C7"));
                timerProgressTextView.setText("00:00");

                pauseTimerButton.setClickable(false);
                pauseTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));
                resumeTimerButton.setClickable(false);
                resumeTimerButton.setBackgroundColor(Color.parseColor("#a6a4a4"));
            }
        });


//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;




    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}