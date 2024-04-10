package com.example.ironplan_fitness.Controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ironplan_fitness.Model.Exercise;
import com.example.ironplan_fitness.Model.Plan;
import com.example.ironplan_fitness.Model.Workout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseHelper {

    private static FirebaseDatabaseHelper instance;
    private DatabaseReference mDatabase;

    private FirebaseDatabase database;

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    private FirebaseDatabaseHelper() {
        // Initialize Firebase Realtime Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void addPlan(Plan plan, int flag) {
        String planId;
        if (flag != 0) {
            planId = mDatabase.child("plans").child("public_plans").push().getKey(); // Generate a unique key for the plan
        } else {
            planId = mDatabase.child("users").child(UserSingleton.getInstance().getUid()).child("plans").child("private_plans").push().getKey();
        }

        plan.setId(planId);

        // Create a map to hold plan details
        Map<String, Object> planMap = new HashMap<>();
        planMap.put("name", plan.getName());
        planMap.put("level", plan.getLevel());
        planMap.put("type", plan.getCategory());
        planMap.put("wcounter", plan.getWorkouts().size());

        // Create a map to hold workouts
        Map<String, Object> workoutsMap = new HashMap<>();

        for (int i = 0; i < plan.getWorkouts().size(); i++) {
            Workout workout = plan.getWorkouts().get(i);
            Map<String, Object> workoutMap = new HashMap<>();
            workoutMap.put("name", workout.getName());

            // Create a map to hold exercises
            Map<String, Object> exercisesMap = new HashMap<>();
            for (int j = 0; j < workout.getExercises().size(); j++) {
                Exercise exercise = workout.getExercises().get(j);
                Map<String, Object> exerciseMap = new HashMap<>();
                exerciseMap.put("name", exercise.getName());
                exerciseMap.put("muscle", exercise.getMuscle());
                exerciseMap.put("sets", exercise.getSets());
                exerciseMap.put("reps", exercise.getReps());
                exerciseMap.put("rest", exercise.getRest_time());
                exercisesMap.put("exercise_" + (j + 1), exerciseMap);
            }
            workoutMap.put("exercises", exercisesMap);

            workoutsMap.put("workout_" + (i + 1), workoutMap);
        }

        planMap.put("workouts", workoutsMap);

        if (flag != 0) {
            mDatabase.child("plans").child("public_plans").child(planId).setValue(planMap); // Set the plan object under "plans" node with the generated key
        } else {
            mDatabase.child("users").child(UserSingleton.getInstance().getUid()).child("plans").child("private_plans").child(planId).setValue(planMap); // Set the plan object under "users" under private plans node with the generated key
        }
    }


    // Singleton instance method
    public static synchronized FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
        return instance;
    }

    // Method to write data to the database
    public void writeData(String key, Object data) {
        mDatabase.child(key).setValue(data);
    }


    // Define interface for data callback
    public interface DataCallback {
        void onSuccess(Object data);

        void onError(String errorMessage);
    }

}
