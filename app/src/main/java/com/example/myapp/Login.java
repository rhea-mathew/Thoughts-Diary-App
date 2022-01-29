package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void buttonLogInToSignUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }

    public void buttonLogin(View view) {
        EditText user = findViewById(R.id.username_login);
        EditText pass = findViewById(R.id.password_login);
        username = user.getText().toString();
        password = pass.getText().toString();
        System.out.println("\n\nUsername: " + username + "  Password: " + password + "\n\n");
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Please fill in both fields!", Toast.LENGTH_LONG).show();
            return;
        }

        // sign in existing user
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Login successful!", Toast.LENGTH_LONG).show();

                            // User logged in successfully, so proceed to main screen
                            Intent intent= new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // sign-in failed
                            Toast.makeText(getApplicationContext(),"Login failed.", Toast.LENGTH_LONG).show();
                        }
                    }
            });
    }

}