package com.example.dreamjournal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import com.example.dreamjournal.R;
import com.example.dreamjournal.data.Data;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class EditDreamActivity extends AppCompatActivity {

    DatePickerDialog picker;
    Date selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dream);

        Button editDreamButton = findViewById(R.id.editDreamPageEditDreamButton);
        Button cancelEditDreamButton = findViewById(R.id.cancelEditDreamButton);
        EditText editTextEditDreamTitle = findViewById(R.id.editTextEditDreamTitle);
        EditText editTextEditDreamDescription = findViewById(R.id.editTextEditDreamDescription);
        Switch editDreamPublicSwitch = findViewById(R.id.editDreamPublicSwitch);
        Button selectDateButton = findViewById(R.id.selectDateButtonEditDream);
        EditText editTextEditDreamDate = findViewById(R.id.editTextEditDreamDate);


        String dreamID = getIntent().getStringExtra("dreamID");
        String dreamTitle = getIntent().getStringExtra("title");
        String dreamDescription = getIntent().getStringExtra("description");
        Long dreamDate = getIntent().getLongExtra("date", 0);
        String isPublic = getIntent().getStringExtra("isPublic");
        Log.d("myTag", "This is my message" + isPublic);


        editTextEditDreamTitle.setText(dreamTitle);
        editTextEditDreamDescription.setText(dreamDescription);

        java.util.Date time = new java.util.Date((long)dreamDate*1000);
        SimpleDateFormat sm = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = sm.format(time);

        editTextEditDreamDate.setText(strDate);
        if (isPublic.equals("true")) {
            editDreamPublicSwitch.setChecked(true);
        } else if (isPublic.equals("false")) {
            editDreamPublicSwitch.setChecked(false);
        }


        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference();

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(EditDreamActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextEditDreamDate.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                        Calendar myCalendar = new GregorianCalendar(year, month, dayOfMonth);
                        selectedDate = myCalendar.getTime();
                    }
                }, year, month, day);
                picker.show();
            }
        });

        editDreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child(Data.userID).child("dreams").child(dreamID).child("title").setValue(editTextEditDreamTitle.getText().toString());
                database.child(Data.userID).child("dreams").child(dreamID).child("description").setValue(editTextEditDreamDescription.getText().toString());
                String sDate = editTextEditDreamDate.getText().toString();
                try {
                    selectedDate = new SimpleDateFormat("MM/dd/yyyy").parse(sDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                database.child(Data.userID).child("dreams").child(dreamID).child("date").setValue(selectedDate.getTime() / 1000);
                database.child(Data.userID).child("dreams").child(dreamID).child("isPublic").setValue(String.valueOf(editDreamPublicSwitch.isChecked()));

               finish();

            }
        });

        cancelEditDreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}