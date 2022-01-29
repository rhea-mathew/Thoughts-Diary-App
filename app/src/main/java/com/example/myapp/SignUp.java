package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String username, password, testing;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    public void buttonSignUp(View view) {
        EditText user = findViewById(R.id.username);
        EditText pass = findViewById(R.id.password);
        username = user.getText().toString();
        password = pass.getText().toString();

        mFirebaseAnalytics.logEvent("button_clicked", null);
        System.out.println("\n\nUsername: " + username + "  Password: " + password + "\n\n");

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignUp.this, "Please fill in both fields!", Toast.LENGTH_LONG).show();
            return;
        } else if (password.length() <= 6) {
            Toast.makeText(SignUp.this, "Password must have more than 6 characters.", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(username, password)
             .addOnCompleteListener(SignUp.this, task -> {
                 if (task.isSuccessful()) {
                     Toast.makeText(getApplicationContext(),"Registration successful!",Toast.LENGTH_LONG).show();
                     // User signed up successfully, so proceed to main screen
                     Intent intent = new Intent(SignUp.this, MainActivity.class);
                     startActivity(intent);
                 } else {
                     // Registration failed
                     Toast.makeText(getApplicationContext(),"Registration failed! Please try again.",Toast.LENGTH_LONG).show();
                 }
             });
    }

    public void buttonSignUpToLogIn(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}