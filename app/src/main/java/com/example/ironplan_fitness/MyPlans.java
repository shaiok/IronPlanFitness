package com.example.ironplan_fitness;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ironplan_fitness.Controller.FirebaseDatabaseHelper;
import com.example.ironplan_fitness.Controller.UserSingleton;
import com.example.ironplan_fitness.Model.Plan;
import com.example.ironplan_fitness.Model.start_workout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPlans extends Fragment {

    private DatabaseReference mDatabase;

    private FirebaseDatabase database;
    private FirebaseDatabaseHelper databaseHelper;

    private View rootView;

    private ArrayList<Plan> plans; // Placeholder for plans loaded from the database

    public MyPlans() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_my_plans, container, false);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize Firebase database instance
        database = FirebaseDatabase.getInstance();

        // Fetch plans from the database (this is just a placeholder)
        // Replace this with your actual database query to fetch plans
        this.plans = new ArrayList<>();
        loadPlansFromDatabase(UserSingleton.getInstance().getUid());

        return rootView;
    }
    private void displayPlans(View rootView, Plan plan) {

        LinearLayout myPlansLayout = rootView.findViewById(R.id.myPlan_Layout);
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        if (plan != null) {
            // Create a LinearLayout to hold plan details
            LinearLayout planLayout = new LinearLayout(getContext());
            planLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, dpToPx(16, getContext())); // Add bottom margin
            planLayout.setLayoutParams(params);
            planLayout.setBackgroundResource(R.drawable.plan_card); // Apply background with rounded corners

            // Set plan details
            TextView nameTextView = new TextView(getContext());
            nameTextView.setText(plan.getName());
            nameTextView.setTextSize(20);
            nameTextView.setTextColor(Color.WHITE);
            nameTextView.setTypeface(nameTextView.getTypeface(), Typeface.BOLD); // Set name to bold
            nameTextView.setPadding(0, 0, 0, dpToPx(4, getContext())); // Add padding below name
            planLayout.addView(nameTextView);

            TextView typeTextView = new TextView(getContext());
            typeTextView.setText("Category: " + plan.getCategory());
            typeTextView.setTextSize(16);
            typeTextView.setTextColor(Color.WHITE);
            planLayout.addView(typeTextView);

            TextView frequencyTextView = new TextView(getContext());
            frequencyTextView.setText("Workouts per week: " + plan.getWcounter());
            frequencyTextView.setTextSize(16);
            frequencyTextView.setTextColor(Color.WHITE);
            planLayout.addView(frequencyTextView);

            TextView levelTextView = new TextView(getContext());
            levelTextView.setText("Level: " + plan.getLevel());
            levelTextView.setTextSize(16);
            levelTextView.setTextColor(Color.WHITE);
            planLayout.addView(levelTextView);

            // Create a LinearLayout to hold the buttons horizontally
            LinearLayout buttonLayout = new LinearLayout(getContext());
            buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.CENTER_VERTICAL); // Adjust gravity to center buttons vertically

            // Create layout parameters for buttons
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            buttonParams.setMargins(0, dpToPx(8, getContext()), dpToPx(8, getContext()), 0); // Add top margin

            // Button to start workout
            Button startWorkoutButton = new Button(getContext());
            startWorkoutButton.setText("Start Workout");
            startWorkoutButton.setTextColor(Color.BLACK);
            startWorkoutButton.setBackgroundResource(R.drawable.button_background);
            startWorkoutButton.setLayoutParams(buttonParams);
            startWorkoutButton.setTextSize(14); // Adjust text size
            startWorkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fullPlanIntent = new Intent(getActivity(), start_workout.class);
                    fullPlanIntent.putExtra("selected_plan", plan.getId());
                    startActivity(fullPlanIntent);
                }
            });



            // Button to see full plan
            Button seeFullPlanButton = new Button(getContext());
            seeFullPlanButton.setTag(plan.getId());
            seeFullPlanButton.setText("Full Plan");
            seeFullPlanButton.setTextColor(Color.BLACK);
            seeFullPlanButton.setBackgroundResource(R.drawable.button_background);
            seeFullPlanButton.setLayoutParams(buttonParams); // Apply the same layout parameters as startWorkoutButton
            seeFullPlanButton.setPadding(dpToPx(8, getContext()), dpToPx(8, getContext()), dpToPx(8, getContext()), dpToPx(8, getContext())); // Set padding
            seeFullPlanButton.setTextSize(14); // Adjust text size
            seeFullPlanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the FullPlanActivity
                    Intent fullPlanIntent = new Intent(getActivity(), FullPlan.class);
                    fullPlanIntent.putExtra("selected_plan", plan.getId());
                    fullPlanIntent.putExtra("lastFragment", 3); // Replace lastFragmentNumber with the actual last fragment number
                    startActivity(fullPlanIntent);
                }
            });

            // Button to remove workout
            Button removeWorkoutButton = new Button(getContext());
            removeWorkoutButton.setText("Remove");
            removeWorkoutButton.setTextColor(Color.BLACK);
            removeWorkoutButton.setBackgroundResource(R.drawable.button_background);
            removeWorkoutButton.setLayoutParams(buttonParams); // Apply the same layout parameters as startWorkoutButton
            removeWorkoutButton.setPadding(dpToPx(8, getContext()), dpToPx(8, getContext()), dpToPx(8, getContext()), dpToPx(8, getContext())); // Set padding
            removeWorkoutButton.setTextSize(14); // Adjust text size
            // Implement onClickListener for removing workout
            removeWorkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeplan(plan.getId());
                }
            });


            // Add buttons to the button layout
            buttonLayout.addView(startWorkoutButton);
            buttonLayout.addView(seeFullPlanButton);
            buttonLayout.addView(removeWorkoutButton);

            // Add button layout to the plan layout
            planLayout.addView(buttonLayout);

            // Add plan layout to the parent layout
            myPlansLayout.addView(planLayout);
        }
    }

    private void removeplan(String id) {
        DatabaseReference privatePlansRef = mDatabase.child("users").child(UserSingleton.getInstance().getUid())
                .child("plans").child("private_plans").child(id);





        // Load references to public plans
        DatabaseReference publicPlansRef =  mDatabase.child("plans").child("public_plans").child(id);
        publicPlansRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                          @Override
                                                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                              // Check if there are any public plans available
                                                              if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                                                  mDatabase.child("users").child(UserSingleton.getInstance().getUid())
                                                                                  .child("plans").child("public_plans").child(id).removeValue();
                                                                  Log.d("MyPlanActivity", "removed public plan ref");
                                                                  Toast.makeText(getContext(),"removed plan from my plans",Toast.LENGTH_SHORT).show();

                                                              } else {
                                                                  // Handle the case where there are no public plans available
                                                                  Log.d("MyPlanActivity", "dident remove public plan ref");
                                                              }
                                                          }

                                                          @Override
                                                          public void onCancelled(@NonNull DatabaseError databaseError) {
                                                              Log.e("MyPlanActivity", "Failed reomve public plan: ");

                                                          }
        });


        privatePlansRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if there are any public plans available
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    mDatabase.child("users").child(UserSingleton.getInstance().getUid())
                            .child("plans").child("private_plans").child(id).removeValue();
                    Toast.makeText(getContext(),"removed plan from my plans",Toast.LENGTH_SHORT).show();




                    Log.d("MyPlanActivity", "removed private plan ref");
                } else {
                    // Handle the case where there are no public plans available
                    Log.d("MyPlanActivity", "dident remove private plan ref");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MyPlanActivity", "Failed reomve private plan: ");

            }
        });
    }

    // Utility method to convert dp to pixels
    private static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);


    }
    private void loadPlansFromDatabase(String userUid) {


        DatabaseReference userPlansRef = mDatabase.child("users").child(userUid).child("plans");
        DatabaseReference privatePlansRef = mDatabase.child("users").child(userUid).child("plans").child("private_plans");

        // Load references to public plans
        DatabaseReference publicPlansRef = userPlansRef.child("public_plans");
        publicPlansRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if there are any public plans available
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (DataSnapshot planSnapshot : dataSnapshot.getChildren()) {
                        if (planSnapshot.getKey() != null)
                        if (mDatabase.child("plans").child("public_plans").child(planSnapshot.getKey()) != null ) {
                            String planId = planSnapshot.getKey();
                            // Since this is a public plan, pass the correct reference to loadPlanDetails
                            loadPlanDetails(planId, mDatabase.child("plans").child("public_plans"));
                            Log.d("MyPlanActivity", " match between public plan and user ref");
                        }

                        Log.d("MyPlanActivity", "No match between plan and user ref");
                    }
                } else {
                    // Handle the case where there are no public plans available
                    Log.d("MyPlanActivity", "No public plans found");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MyPlanActivity", "Failed to load public plans: " + databaseError.getMessage());
            }
        });

        privatePlansRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if there are any public plans available
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (DataSnapshot planSnapshot : dataSnapshot.getChildren()) {
                        if (mDatabase.child("users").child(userUid).child("plans").child("private_plans")
                                .child(planSnapshot.getKey()) != null )
                        {
                            String planId = planSnapshot.getKey();
                            // Since this is a public plan, pass the correct reference to loadPlanDetails
                            loadPlanDetails(planId, mDatabase.child("users").child(userUid).child("plans").child("private_plans"));
                            Log.d("MyPlanActivity", " match between public plan and user ref");
                        }

                        Log.d("MyPlanActivity", "No match between plan and user ref");
                    }
                } else {
                    // Handle the case where there are no public plans available
                    Log.d("MyPlanActivity", "No public plans found");
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MyPlanActivity", "Failed to load public plans: " + databaseError.getMessage());
            }
        });



    }

    public void loadPlanDetails(String planId, DatabaseReference plansRef ) {
        DatabaseReference userPlansRef = plansRef.child(planId); // Use the provided plansRef directly


        userPlansRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Extract plan details
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String level = dataSnapshot.child("level").getValue(String.class);
                    String type = dataSnapshot.child("type").getValue(String.class);
                    int frequency = dataSnapshot.child("wcounter").getValue(int.class);

                    Plan plan = new Plan(name, level, type,frequency);
                    plan.setId(planId);
                    Log.d("MyPlanActivity", "data snap "+dataSnapshot.getKey()+" Adding plan: " + dataSnapshot.child("name") + " Level: " + dataSnapshot.child("level").getKey() + " Type: " + type);

                    // Pass the plan to displayPlans method for rendering
                    displayPlans(rootView, plan);
                } else {
                    Log.d("MyPlanActivity", "Plan does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyPlanActivity", "Failed to load plan details: " + error.getMessage());
            }
        });
    }


}



