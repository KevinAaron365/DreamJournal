package com.example.dreamjournal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.dreamjournal.R;
import com.example.dreamjournal.data.Data;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class EditDreamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dream);

        Button editDreamButton = findViewById(R.id.editDreamPageEditDreamButton);
        Button cancelEditDreamButton = findViewById(R.id.cancelEditDreamButton);
        EditText editTextEditDreamTitle = findViewById(R.id.editTextEditDreamTitle);
        EditText editTextEditDreamDescription = findViewById(R.id.editTextEditDreamDescription);
        Switch editDreamPublicSwitch = findViewById(R.id.editDreamPublicSwitch);

        String dreamID = getIntent().getStringExtra("dreamID");
        String dreamTitle = getIntent().getStringExtra("title");
        String dreamDescription = getIntent().getStringExtra("description");
        Long dreamDate = getIntent().getLongExtra("date", 0);
        String isPublic = getIntent().getStringExtra("isPublic");
        Log.d("myTag", "This is my message" + isPublic);


        editTextEditDreamTitle.setText(dreamTitle);
        editTextEditDreamDescription.setText(dreamDescription);
        if (isPublic.equals("true")) {
            editDreamPublicSwitch.setChecked(true);
        } else if (isPublic.equals("false")) {
            editDreamPublicSwitch.setChecked(false);
        }


        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference();

        editDreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child(Data.userID).child("dreams").child(dreamID).child("title").setValue(editTextEditDreamTitle.getText().toString());
                database.child(Data.userID).child("dreams").child(dreamID).child("description").setValue(editTextEditDreamDescription.getText().toString());
                database.child(Data.userID).child("dreams").child(dreamID).child("date").setValue(dreamDate);
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