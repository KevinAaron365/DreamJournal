package com.example.dreamjournal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.dreamjournal.R;
import com.example.dreamjournal.data.Data;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddDreamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dream);

        Button addNewDreamButton = findViewById(R.id.newDreamPageNewDreamButton);
        Button cancelNewDreamButton = findViewById(R.id.cancelNewDreamButton);
        EditText editTextNewDreamTitle = findViewById(R.id.editTextNewDreamTitle);
        EditText editTextNewDreamDescription = findViewById(R.id.editTextNewDreamDescription);
        Switch makeNewDreamPublicSwitch = findViewById(R.id.makeNewDreamPublicSwitch);

        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference();

        addNewDreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = UUID.randomUUID().toString();
                database.child(Data.userID).child("dreams").child(uuid).child("title").setValue(editTextNewDreamTitle.getText().toString());
                database.child(Data.userID).child("dreams").child(uuid).child("description").setValue(editTextNewDreamDescription.getText().toString());
                database.child(Data.userID).child("dreams").child(uuid).child("date").setValue(System.currentTimeMillis() / 1000L);
                database.child(Data.userID).child("dreams").child(uuid).child("isPublic").setValue(String.valueOf(makeNewDreamPublicSwitch.isChecked()));

                finish();

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