package com.example.dreamjournal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import com.example.dreamjournal.R;
import com.example.dreamjournal.data.Data;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class AddDreamActivity extends AppCompatActivity {
    private DatePickerDialog picker;
    private Date selectedDate;

    private boolean addDreamError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dream);

        Button addNewDreamButton = findViewById(R.id.newDreamPageNewDreamButton);
        Button cancelNewDreamButton = findViewById(R.id.cancelNewDreamButton);
        TextInputLayout editTextNewDreamTitleLayout = findViewById(R.id.editTextNewDreamTitleLayout);
        TextInputEditText editTextNewDreamTitle = findViewById(R.id.editTextNewDreamTitle);

        TextInputLayout editTextNewDreamDescriptionLayout = findViewById(R.id.editTextNewDreamDescriptionLayout);
        TextInputEditText editTextNewDescriptionTitle = findViewById(R.id.editTextNewDreamDescription);

        Switch makeNewDreamPublicSwitch = findViewById(R.id.makeNewDreamPublicSwitch);
        Button selectDateButton = findViewById(R.id.selectDateButtonNewDream);

        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference();

        TextInputLayout editTextNewDreamDateLayout = findViewById(R.id.editTextNewDreamDateLayout);
        TextInputEditText editTextNewDreamDate = findViewById(R.id.editTextNewDreamDate);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(AddDreamActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextNewDreamDateLayout.getEditText().setText((month + 1) + "/" + dayOfMonth + "/" + year);
                        Calendar myCalendar = new GregorianCalendar(year, month, dayOfMonth);
                        selectedDate = myCalendar.getTime();
                    }
                }, year, month, day);
                picker.show();
            }
        });


        addNewDreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDreamError = false;
                String uuid = UUID.randomUUID().toString();
                if (editTextNewDreamTitleLayout.getEditText().getText().toString().equals("")) {
                    editTextNewDreamTitleLayout.setError("Enter a title");
                    addDreamError = true;
                } else {
                    addDreamError = false;
                    editTextNewDreamTitleLayout.setError(null);
                }
                if (editTextNewDreamDateLayout.getEditText().getText().toString().equals("")) {
                    editTextNewDreamDateLayout.setError("Enter a date");
                    addDreamError = true;
                } else  {
                    addDreamError = false;
                    editTextNewDreamDateLayout.setError(null);
                }

                if (editTextNewDreamDescriptionLayout.getEditText().getText().toString().equals("")) {
                    editTextNewDreamDescriptionLayout.setError("Enter a description");
                    addDreamError = true;

                } else {
                    addDreamError = false;
                    editTextNewDreamDescriptionLayout.setError(null);
                }

                if (addDreamError == false) {
                    database.child(Data.userID).child("dreams").child(uuid).child("title").setValue(editTextNewDreamTitleLayout.getEditText().getText().toString());
                    database.child(Data.userID).child("dreams").child(uuid).child("description").setValue(editTextNewDreamDescriptionLayout.getEditText().getText().toString());

                    String sDate = editTextNewDreamDateLayout.getEditText().getText().toString();
                    try {
                        selectedDate = new SimpleDateFormat("MM/dd/yyyy").parse(sDate);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    database.child(Data.userID).child("dreams").child(uuid).child("date").setValue(selectedDate.getTime() / 1000F);
                    database.child(Data.userID).child("dreams").child(uuid).child("isPublic").setValue(String.valueOf(makeNewDreamPublicSwitch.isChecked()));

                    finish();

                }

            }
        });

        cancelNewDreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });
    }
}