package com.example.dreamjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dreamjournal.data.Data;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    boolean userError = false;
    boolean passError = false;

    boolean loginError = true;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.createNewAccountButton);
        Button createAccountButton = findViewById(R.id.loginPageCreateAccountButton);
        TextInputEditText username = findViewById(R.id.editTextLoginUsername);
        TextInputEditText password = findViewById(R.id.editTextLoginPassword);
        TextInputLayout usernameLayout = findViewById(R.id.editTextLoginUsernameLayout);
        TextInputLayout passwordLayout = findViewById(R.id.editTextLoginPasswordLayout);



        username.addTextChangedListener(new TextWatcher() {
            String user = username.getText().toString();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userError == true) {
                    if (!username.getText().toString().equals("")) {
                        usernameLayout.setError(null);
                        userError = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            String pass = password.getText().toString();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passError == true) {
                    if (!password.getText().toString().equals("")) {
                        passwordLayout.setError(null);
                        passError = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("")) {

                    usernameLayout.setError("Enter username");
//                    usernameLayout.setErrorIconDrawable(0);
                    userError = true;

                }
                if (pass.equals("")) {
                    passwordLayout.setError("Enter password");
//                    passwordLayout.setErrorIconDrawable(0);
                    passError = true;

                }
                if (!user.equals("")) {
                    usernameLayout.setError(null);
                    userError = false;

                }
                if (!pass.equals("")) {
                    passwordLayout.setError(null);
                    passError = false;

                }




                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();




                // Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d:
                             dataSnapshot.getChildren()) {
                            String usernameData = d.child("username").getValue(String.class);
                            Log.d(TAG, "Value is: " + usernameData);

                            String passwordData = d.child("password").getValue(String.class);
                            Log.d(TAG, "Value is: " + passwordData);

                            if (user.equals(usernameData) && pass.equals(passwordData)) {
                                Log.d(TAG, "Logged In");
                                usernameLayout.setError(null);
                                passwordLayout.setError(null);
                                loginError = false;

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                Data.userID = d.getKey();
                                startActivity(i);
                            } else {
                                if (!userError && loginError) {
                                    usernameLayout.setError("Invalid username or password");
                                }
                                if (!passError && loginError) {
                                    passwordLayout.setError("Invalid username or password");
                                }
                            }
                        }







                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value

                        Log.w(TAG, "Failed to read value.", error.toException());
                    }

                });
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NewAccountActivity.class);
                startActivity(i);
            }
        });

    }
}