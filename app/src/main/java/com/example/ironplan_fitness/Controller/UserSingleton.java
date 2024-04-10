package com.example.ironplan_fitness.Controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSingleton {
    private static UserSingleton instance;
    private String fullName;
    private String email;
    private String uid;

    // Private constructor to prevent instantiation from outside
    private UserSingleton() {
    }

    // Method to get the singleton instance
    public static synchronized UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    // Method to set user details
    public void setUser(FirebaseUser user) {
        if (user != null) {
            uid = user.getUid();
            email = user.getEmail();
            retrieveUserNameFromDatabase(uid);
        }
    }


    // Method to retrieve user name from database
    private void retrieveUserNameFromDatabase(String userUid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userUid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("UserSingleton", "Snapshot value: " + snapshot.getValue());

                if (snapshot.exists()) {
                    // User data exists, retrieve full name
                    fullName = snapshot.child("fullName").getValue(String.class);
                    // Log retrieved full name for debugging
                    Log.d("UserSingleton", "Retrieved full name from database: " + fullName);
                } else {
                    // Handle the case where user data doesn't exist
                    Log.d("UserSingleton", "User data doesn't exist in the database for UID: " + userUid);
                }

           }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Log.e("UserSingleton", "Error retrieving user data: " + error.getMessage());
            }
        });
    }


    // Getters and setters for user information
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

