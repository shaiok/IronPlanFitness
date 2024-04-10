package com.example.ironplan_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ironplan_fitness.Controller.FirebaseDatabaseHelper;
import com.example.ironplan_fitness.Controller.UserSingleton;
import com.example.ironplan_fitness.Model.Exercise;
import com.example.ironplan_fitness.Model.Plan;
import com.example.ironplan_fitness.Model.Workout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class FullPlan extends AppCompatActivity {

    private int lastFragmentNumber;
    private LinearLayout parentLayout;

    private DatabaseReference mDatabase;

    private FirebaseDatabase database;
    private FirebaseDatabaseHelper databaseHelper;

    private Plan plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_plan);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();


        parentLayout = findViewById(R.id.fullplan_layout); // Assuming you have a LinearLayout with id parentLayout in your XML
        String Pid = getIntent().getStringExtra("selected_plan");

        Log.d("FullPlanActivity", "starting activity this is pid "+Pid);
        // load a plan from the data base  based on the plan id

       loadPlan(Pid);


        // Get the last fragment number from the intent
        this.lastFragmentNumber = getIntent().getIntExtra("lastFragment", -1);


    }

    private void addPlanDetailsToParentLayout(Plan plan) {
        // Create a card view for the plan
        CardView planCard = createPlanCardView(this, plan.getName(), plan.getCategory(), plan.getLevel(), plan.getWorkouts().size());

        // Add the plan card to the parent layout
        parentLayout.addView(planCard);

        // Iterate over each workout in the plan
        for (Workout workout : plan.getWorkouts()) {
            // Create a card view for the workout and add exercises
            CardView workoutCard = createWorkoutCardView(this, workout.getName(), workout.getExercises());

            // Add the workout card to the parent layout
            parentLayout.addView(workoutCard);
        }

        // Add buttons
        addAddToMyPlansButton();
        addReturnToChoosePlanButton();
    }
    private CardView createPlanCardView(Context context, String name, String type, String level, int numWorkouts) {
        CardView cardView = new CardView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dpToPx(8), 0, 0);
        cardView.setLayoutParams(params);
        cardView.setBackgroundResource(R.drawable.edittext_background);
        cardView.setRadius(dpToPx(8));
        cardView.setCardElevation(dpToPx(4));

        LinearLayout planDetailsLayout = new LinearLayout(context);
        planDetailsLayout.setOrientation(LinearLayout.VERTICAL);
        planDetailsLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        TextView nameTextView = createTextView(name, 20, true);
        TextView typeTextView = createTextView("Type: " + type, 16, false);
        TextView levelTextView = createTextView("Level: " + level, 16, false);
        TextView numWorkoutsTextView = createTextView("Number of Workouts: " + numWorkouts, 16, false);

        planDetailsLayout.addView(nameTextView);
        planDetailsLayout.addView(typeTextView);
        planDetailsLayout.addView(levelTextView);
        planDetailsLayout.addView(numWorkoutsTextView);

        cardView.addView(planDetailsLayout);

        return cardView;
    }
    private CardView createWorkoutCardView(Context context, String name, ArrayList<Exercise> exercises) {
        CardView cardView = new CardView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dpToPx(8), 0, 0);
        cardView.setLayoutParams(params);
        cardView.setRadius(dpToPx(8));
        cardView.setBackgroundResource(R.drawable.edittext_background);
        cardView.setCardElevation(dpToPx(2));
        cardView.setCardBackgroundColor(Color.WHITE); // Set white background color

        // Create a linear layout for the workout details
        LinearLayout workoutDetailsLayout = new LinearLayout(context);
        workoutDetailsLayout.setOrientation(LinearLayout.VERTICAL);
        workoutDetailsLayout.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));

        // Create a text view for the workout name
        TextView nameTextView = createTextView( name, 20, true); // Increase font size
        nameTextView.setTextColor(Color.BLACK); // Set text color to black
        nameTextView.setShadowLayer(4, 0, 2, Color.LTGRAY); // Add shadow effect
        workoutDetailsLayout.addView(nameTextView);

        // Iterate over each exercise in the workout
        for (Exercise exercise : exercises) {
            // Create a card view for each exercise
            CardView exerciseCardView = createExerciseCardView(context, exercise.getName(), exercise.getMuscle(), exercise.getSets(), exercise.getReps(), exercise.getRest_time());

            // Add the exercise card to the workout details layout
            workoutDetailsLayout.addView(exerciseCardView);
        }

        // Add the linear layout with workout details to the card view
        cardView.addView(workoutDetailsLayout);

        return cardView;
    }

    private CardView createExerciseCardView(Context context, String name, String muscle, int sets, int reps, int restTime) {
        CardView cardView = new CardView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dpToPx(8), 0, 0);
        cardView.setBackgroundResource(R.drawable.plan_card);
        cardView.setLayoutParams(params);
        cardView.setRadius(dpToPx(8));
        cardView.setCardElevation(dpToPx(2));
        cardView.setCardBackgroundColor(Color.LTGRAY); // Set light gray background color

        // Create a linear layout for the exercise details
        LinearLayout exerciseDetailsLayout = new LinearLayout(context);
        exerciseDetailsLayout.setOrientation(LinearLayout.VERTICAL);
        exerciseDetailsLayout.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));

        // Create text views for exercise details
        TextView nameTextView = createTextView( name, 18, true);
        TextView muscleTextView = createTextView("Muscle: " + muscle, 16, false);
        TextView setsRepsTextView = createTextView("Sets: " + sets + " | Reps: " + reps, 16, false);
        TextView restTimeTextView = createTextView("Rest Time: " + restTime + " seconds", 16, false);

        // Add exercise details text views to the linear layout
        exerciseDetailsLayout.addView(nameTextView);
        exerciseDetailsLayout.addView(muscleTextView);
        exerciseDetailsLayout.addView(setsRepsTextView);
        exerciseDetailsLayout.addView(restTimeTextView);

        // Add the linear layout with exercise details to the card view
        cardView.addView(exerciseDetailsLayout);

        return cardView;
    }




    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    private void addAddToMyPlansButton() {
        Button addToMyPlansButton = new Button(this);
        addToMyPlansButton.setText("Add to My Plans");
        addToMyPlansButton.setTextSize(14);
        addToMyPlansButton.setTextColor(Color.WHITE);
        addToMyPlansButton.setBackgroundResource(R.drawable.button_background); // Use green rounded button drawable

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_top_bottom);

        addToMyPlansButton.setLayoutParams(params);
        addToMyPlansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the plan to "My Plans"
                // You can implement this functionality based on your requirements
            }
        });

        parentLayout.addView(addToMyPlansButton);
    }

    private void addReturnToChoosePlanButton() {
        Button returnToChoosePlanButton = new Button(this);
        returnToChoosePlanButton.setText("Return");
        returnToChoosePlanButton.setTextSize(14);
        returnToChoosePlanButton.setTextColor(Color.WHITE);
        returnToChoosePlanButton.setBackgroundResource(R.drawable.button_background); // Use green rounded button drawable

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_top_bottom);

        returnToChoosePlanButton.setLayoutParams(params);
        returnToChoosePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the Choose Plan fragment
                Intent intent = new Intent(FullPlan.this, Home.class);
                if (lastFragmentNumber ==3)
                    intent.putExtra("lastFragment", 3);
                else if (lastFragmentNumber==2)
                    intent.putExtra("lastFragment", 2);
                startActivity(intent);
                finish(); // Finish the Full Plan activity
            }
        });

        parentLayout.addView(returnToChoosePlanButton);
    }

    private TextView createTextView(String text, int textSize, boolean isBold) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setTextColor(Color.WHITE);
        if (isBold) {
            textView.setTypeface(null, Typeface.BOLD);
        }
        return textView;
    }
    private  void loadPlan(String planId) {

        if (mDatabase.child("plans").child("public_plans").child(planId)!=null)
        {
            DatabaseReference publicPlanRef = mDatabase.child("plans").child("public_plans").child(planId);
            publicPlanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        Log.d("FullPlanActivity", "is exsist");
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

                        addPlanDetailsToParentLayout(plan);


                    } else {

                        DatabaseReference privatePlanRef = mDatabase.child("users").child(UserSingleton.getInstance().getUid())
                                .child("plans").child("private_plans").child(planId);
                        privatePlanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    Log.d("FullPlanActivity", "private plan is exists");
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

                                    addPlanDetailsToParentLayout(plan);


                                } else {

                                    Log.d("FullPlanActivity", "data not exsist ");
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
