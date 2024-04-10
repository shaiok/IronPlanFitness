package com.example.ironplan_fitness.Model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ironplan_fitness.Controller.UserSingleton;
import com.example.ironplan_fitness.Home;
import com.example.ironplan_fitness.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class start_workout extends AppCompatActivity {

    private LinearLayout parentLayout;

    private DatabaseReference mDatabase;

    private FirebaseDatabase database;
    private Plan plan ;

    private Workout current_workout;

    private int current_exercise;

    private DatabaseReference ref;

    private  int setsRemain;

    private int  minutes;
    private int  seconds  ;
    private CountDownTimer restTimer;


    // Declare your views here
    private Spinner workoutSpinner;
    private Button startWorkoutButton;
    private Button finishSetButton;
    private Button skipRestButton;
    private Button returnButton;
    private TextView workoutNameTextView;
    private TextView exerciseNameTextView;
    private TextView setsRemainingTextView;
    private TextView repsTextView;
    private TextView timerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);


        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        String Pid = getIntent().getStringExtra("selected_plan");
        // setting all the views from my xml
        getViews();

        // load the number of workout in the current plan to the workout spinner .
        // load this plan in this activity
        loadPlan(Pid);







    }

    private void updateUI() {
        // Get a reference to the spinner
        Spinner workoutSpinner = findViewById(R.id.workoutSpinner);

        // Retrieve the list of workout names from your data source
        List<String> workoutNames = getWorkoutNames(); // Replace this with your actual method to fetch workout names

        // Create an adapter to populate the spinner with workout names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workoutNames);

        // Set the dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        workoutSpinner.setAdapter(adapter);
    }

    private List<String> getWorkoutNames() {

        List<String> workoutNames = new ArrayList<>();
        for (int i = 0; i < plan.getWorkouts().size(); i++) {
            workoutNames.add(plan.getWorkouts().get(i).getName());
        }
        // Add more workout names as needed
        return workoutNames;
    }


    private void getViews() {
            workoutSpinner = findViewById(R.id.workoutSpinner);
            startWorkoutButton = findViewById(R.id.startWorkoutButton);
            finishSetButton = findViewById(R.id.finishSetButton);
            skipRestButton = findViewById(R.id.skipRestButton);
            returnButton = findViewById(R.id.returnButton);
            workoutNameTextView = findViewById(R.id.workoutNameTextView);
            exerciseNameTextView = findViewById(R.id.exerciseNameTextView);
            setsRemainingTextView = findViewById(R.id.setsRemainingTextView);
            repsTextView = findViewById(R.id.repsTextView);
            timerTextView = findViewById(R.id.timerTextView);

            // Set onClick listeners
            startWorkoutButton.setOnClickListener(v -> startWorkout());
            finishSetButton.setOnClickListener(v -> finishSet());
            skipRestButton.setOnClickListener(v -> skipRest());
            returnButton.setOnClickListener(v -> returnBtn());
        }

    private void startWorkout() {
        // Hide the spinner and start workout button
        Spinner workoutSpinner = findViewById(R.id.workoutSpinner);
        Button startButton = findViewById(R.id.startWorkoutButton);

        Log.d("StartWorkout", "Spinner and start button hidden");

        String workoutName = workoutSpinner.getSelectedItem().toString();
        for (int i=0 ; i< plan.getWorkouts().size();i++) {
            if (workoutName.equals(plan.getWorkouts().get(i).getName())){
                current_workout = plan.getWorkouts().get(i);
            }
        }

        workoutSpinner.setVisibility(View.GONE);
        startButton.setVisibility(View.GONE);

        // Log visibility changes
        Log.d("StartWorkout", "Spinner and start button visibility set to GONE");

        // Show the layout containing exercise details
        LinearLayout exerciseDetailsLayout = findViewById(R.id.workoutDetailsLayout);
        exerciseDetailsLayout.setVisibility(View.VISIBLE);

        // Log visibility change
        Log.d("StartWorkout", "Exercise details layout visibility set to VISIBLE");

        // Show buttons layout
        LinearLayout buttonsLayout = findViewById(R.id.buttons_layout1);
        buttonsLayout.setVisibility(View.VISIBLE);

        // Log visibility change
        Log.d("StartWorkout", "Buttons layout visibility set to VISIBLE");

        // Fetch the details of the first exercise from your data source
        workoutNameTextView.setText(current_workout.getName());
        updateExerciseUI();
    }

    private void finishSet() {
        // Decrease the number of remaining sets
        setsRemain--;

        // Check if there are more sets to complete for the current exercise
        if (setsRemain > 0) {
            // Update the sets remaining TextView
            setsRemainingTextView.setText("Sets remaining: " + setsRemain);
            // Start the rest timer
            startRestTimer();
        } else {
            // Move to the next exercise or finish the workout if it was the last exercise
            moveToNextExercise();
        }
    }

    private void moveToNextExercise() {
        // Get the index of the current exercise
        current_exercise++;
        // Check if there is a next exercise
        if (current_exercise < current_workout.getExercises().size() - 1) {
            // There is a next exercise, update the current exercise
            // Update the UI with the new exercise details
            updateExerciseUI();
        } else {
            // This was the last exercise, finish the workout
            finishWorkout();
        }
    }

    private void updateExerciseUI() {
        // Update the text views with the details of the new exercise
        Exercise exercise = current_workout.getExercises().get(current_exercise);
        setsRemain = exercise.getSets();

        Log.d("UpdateExerciseUI", "Exercise details updated");

        exerciseNameTextView.setText("Exercise Name: " + exercise.getMuscle());
        setsRemainingTextView.setText("Sets remaining: " + setsRemain);
        repsTextView.setText("Reps: " + exercise.getReps());

        Log.d("UpdateExerciseUI", "Exercise name, sets remaining, and reps updated");

        // Reset the timer
        minutes = exercise.getRest_time() / 60;
        seconds = exercise.getRest_time() % 60;
        timerTextView.setText(minutes + ":" + seconds);

        Log.d("UpdateExerciseUI", "Timer reset");
    }


    private void finishWorkout() {
        // Logic to conclude the workout
        // For example, show a message that the workout is complete
        Toast.makeText(this, "Workout Complete!", Toast.LENGTH_LONG).show();
        // Navigate the user to a different screen or activity
    }



    private void startRestTimer() {
        // Convert minutes and seconds to milliseconds
        long restTimeInMillis = (minutes * 60 + seconds) * 1000;

        restTimer = new CountDownTimer(restTimeInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update the timer TextView every second
                long mins = millisUntilFinished / 60000;
                long secs = (millisUntilFinished % 60000) / 1000;
                timerTextView.setText(String.format("%02d:%02d", mins, secs));
                // Enable the skip rest button
                skipRestButton.setEnabled(true);
            }

            public void onFinish() {
                // Disable the skip rest button when the timer finishes
                skipRestButton.setEnabled(false);
                // Update the timer TextView
                timerTextView.setText("00:00");

            }
        }.start();
    }

    private void skipRest() {
        // Cancel the timer if it's running
        if (restTimer != null) {
            restTimer.cancel();
        }
        // Disable the skip rest button
        skipRestButton.setEnabled(false);
        // Update the timer TextView
        timerTextView.setText("00:00");
        // Move to the next set or exercise


    }


    private void returnBtn() {
        // Create an Intent to navigate back to the Home activity
        Intent intent = new Intent(start_workout.this, Home.class);
        // Add extra data to indicate the last fragment
        intent.putExtra("lastFragment", 3);
        // Start the activity
        startActivity(intent);
        // Finish the current activity
        finish();
    }



    private  void loadPlan(String planId) {

        if (mDatabase.child("plans").child("public_plans").child(planId)!=null)
        {
            DatabaseReference publicPlanRef = mDatabase.child("plans").child("public_plans").child(planId);
            publicPlanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        Log.d("start_workout", "is exsist");
                        // Extract plan details
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String level = dataSnapshot.child("level").getValue(String.class);
                        String type = dataSnapshot.child("type").getValue(String.class);
                        //   long frequency = dataSnapshot.child("wcounter").getValue(Long.class);

                        // Create a new Plan object
                        plan = new Plan();
                        plan.setId(planId);
                        plan.setName(name);
                        plan.setLevel(level);
                        plan.setCategory(type);

                        // Load workouts associated with the plan
                        for (DataSnapshot workoutSnapshot : dataSnapshot.child("workouts").getChildren()) {
                            Workout workout = new Workout();
                            workout.setName(workoutSnapshot.child("name").getValue(String.class));

                            // Load exercises associated with the workout

                            for (DataSnapshot exerciseSnapshot : workoutSnapshot.child("exercises").getChildren()) {
                                Exercise exercise = new Exercise();
                                exercise.setName(exerciseSnapshot.child("name").getValue(String.class));
                                exercise.setMuscle(exerciseSnapshot.child("muscle").getValue(String.class));
                                exercise.setSets(exerciseSnapshot.child("sets").getValue(Integer.class));
                                exercise.setReps(exerciseSnapshot.child("reps").getValue(Integer.class));
                                exercise.setRest_time(exerciseSnapshot.child("rest").getValue(Integer.class));
                                workout.getExercises().add(exercise);
                            }

                            plan.getWorkouts().add(workout); // Directly add the workout to the plan


                        }

                        updateUI();

                    } else {

                        DatabaseReference privatePlanRef = mDatabase.child("users").child(UserSingleton.getInstance().getUid())
                                .child("plans").child("private_plans").child(planId);
                        privatePlanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    Log.d("start_workout", "private plan is exists");
                                    // Extract plan details
                                    String name = dataSnapshot.child("name").getValue(String.class);
                                    String level = dataSnapshot.child("level").getValue(String.class);
                                    String type = dataSnapshot.child("type").getValue(String.class);
                                    //   long frequency = dataSnapshot.child("wcounter").getValue(Long.class);

                                    // Create a new Plan object
                                    plan = new Plan();
                                    plan.setId(planId);
                                    plan.setName(name);
                                    plan.setLevel(level);
                                    plan.setCategory(type);

                                    // Load workouts associated with the plan
                                    for (DataSnapshot workoutSnapshot : dataSnapshot.child("workouts").getChildren()) {
                                        Workout workout = new Workout();
                                        workout.setName(workoutSnapshot.child("name").getValue(String.class));

                                        // Load exercises associated with the workout

                                        for (DataSnapshot exerciseSnapshot : workoutSnapshot.child("exercises").getChildren()) {
                                            Exercise exercise = new Exercise();
                                            exercise.setName(exerciseSnapshot.child("name").getValue(String.class));
                                            exercise.setMuscle(exerciseSnapshot.child("muscle").getValue(String.class));
                                            exercise.setSets(exerciseSnapshot.child("sets").getValue(Integer.class));
                                            exercise.setReps(exerciseSnapshot.child("reps").getValue(Integer.class));
                                            exercise.setRest_time(exerciseSnapshot.child("rest").getValue(Integer.class));
                                            workout.getExercises().add(exercise);
                                        }

                                        plan.getWorkouts().add(workout); // Directly add the workout to the plan



                                    }

                                    updateUI();



                                } else {

                                    Log.d("start_workout", "data not exsist ");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle errors
                            }
                        });


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }

    }






}