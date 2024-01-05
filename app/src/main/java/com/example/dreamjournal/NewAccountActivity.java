package com.example.dreamjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dreamjournal.data.Data;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.UUID;

public class NewAccountActivity extends AppCompatActivity {

    boolean userError = false;
    boolean passError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        TextInputEditText accountUsername = findViewById(R.id.editTextNewAccountUsername);
        TextInputEditText accountPassword = findViewById(R.id.editTextNewAccountPassword);
        TextInputLayout accountUsernameLayout = findViewById(R.id.editTextNewAccountUsernameLayout);
        TextInputLayout accountPasswordLayout = findViewById(R.id.editTextNewAccountPasswordLayout);

        Button createAccountButton = findViewById(R.id.createNewAccountButton);
        Button cancelNewAccountButton = findViewById(R.id.cancelNewAccountButton);

        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference();

        accountUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userError == true) {
                    if (!accountUsername.getText().toString().equals("")) {
                        accountUsernameLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        accountPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passError == true) {
                    if (!accountPassword.getText().toString().equals("")) {
                        accountPasswordLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = UUID.randomUUID().toString();
                String username = accountUsername.getText().toString();
                String password = accountPassword.getText().toString();

                if (username.equals("")) {
                    accountUsernameLayout.setError("Enter a username");
                    userError = true;
                } else {
                    userError = false;
                }
                if (password.equals("")) {
                    accountPasswordLayout.setError("Enter a password");
                    passError = true;
                } else {
                    passError = false;
                }

                if (!userError && !passError) {
                    database.child(uuid).child("username").setValue(username);
                    database.child(uuid).child("password").setValue(password);

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    Data.userID = uuid;
                    startActivity(i);
                }
            }
        });

        cancelNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}