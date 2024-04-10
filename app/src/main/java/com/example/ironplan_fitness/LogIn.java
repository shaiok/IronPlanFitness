package com.example.ironplan_fitness;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Import Log class
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ironplan_fitness.Controller.UserSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LogIn extends AppCompatActivity {

    private static final String TAG = "LogInActivity"; // Define log tag

    private EditText email;
    private EditText password;
    private Button signIn;
    private Button signUp;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize EditTexts, Button, and ProgressBar
        email = findViewById(R.id.LogIn_email_EditText);
        password = findViewById(R.id.LogIn_editTextPassword);
        signIn = findViewById(R.id.LogIn_login_Button);
        signUp = findViewById(R.id.LogIn_signUp_Button);
        progressBar = findViewById(R.id.progress_bar_LogIn);

        // Set click listener for sign in button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        // Set click listener for sign up button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });
    }

    private void logIn() {
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        // Check if email or password are empty
        if (TextUtils.isEmpty(emailStr)) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar while signing in
        progressBar.setVisibility(View.VISIBLE);

        // Sign in user with Firebase
        mAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE); // Hide progress bar
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserSingleton.getInstance().setUser(user);

                            // Redirect to the home activity
                            startActivity(new Intent(LogIn.this, Home.class));
                            finish(); // Finish the login activity
                        } else {
                            // If sign in fails, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                // If the user doesn't exist
                                Toast.makeText(LogIn.this, "User not found.", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "User not found: " + task.getException().getMessage()); // Log error
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // If the password is incorrect
                                Toast.makeText(LogIn.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Incorrect password: " + task.getException().getMessage()); // Log error
                            } else {
                                // For other errors
                                Toast.makeText(LogIn.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Authentication failed: " + task.getException().getMessage()); // Log error
                            }
                        }
                    }
                });
    }
}
