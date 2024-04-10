package com.example.ironplan_fitness;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ironplan_fitness.Controller.UserSingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class chosePlan extends Fragment {

    private Button seePlanButton;

    private DatabaseReference mDatabase;

    private FirebaseDatabase database;

  //  private List<String> planIds = new ArrayList<>(); // List to store plan IDs

    private static int btn_counter = 0;

    public chosePlan() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chose_plan, container, false);

        // Find the placeholder layout
       LinearLayout placeholder = view.findViewById(R.id.choosePlan_mainLayout);

        // Set orientation of workoutPlansLayout to vertical
        placeholder.setOrientation(LinearLayout.VERTICAL);



        // Load public plans and populate the UI
        loadPublicPlans(placeholder, this.getContext() );

        return view;
    }

    public static LinearLayout createWorkoutPlanUI(Context context, String name, String type, String frequency, String level, String Pid) {
        // Create a LinearLayout to hold the workout plan details
        LinearLayout workoutPlanLayout = new LinearLayout(context);

        // Set layout parameters including margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, dpToPx(16, context)); // Add bottom margin
        workoutPlanLayout.setLayoutParams(layoutParams);

        workoutPlanLayout.setOrientation(LinearLayout.VERTICAL);
        workoutPlanLayout.setPadding(dpToPx(16, context), dpToPx(8, context), dpToPx(16, context), dpToPx(8, context));
        workoutPlanLayout.setBackgroundResource(R.drawable.plan_card); // Apply background with rounded corners


        // Create a TextView for the workout plan name
        TextView nameTextView = new TextView(context);
        nameTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        nameTextView.setText(name);
        nameTextView.setTextSize(20);
        nameTextView.setTextColor(Color.WHITE);
        nameTextView.setTypeface(nameTextView.getTypeface(), Typeface.BOLD); // Set name to bold
        nameTextView.setPadding(0, 0, 0, dpToPx(4, context)); // Add padding below name
        workoutPlanLayout.addView(nameTextView);

        // Create a TextView for the workout plan type
        TextView typeTextView = new TextView(context);
        typeTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        typeTextView.setText(type);
        typeTextView.setTextSize(16);
        typeTextView.setTextColor(Color.WHITE);
        workoutPlanLayout.addView(typeTextView);

        // Create a TextView for the workout plan frequency
        TextView frequencyTextView = new TextView(context);
        frequencyTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        frequencyTextView.setText("Frequency: " + frequency);
        frequencyTextView.setTextSize(16);
        frequencyTextView.setTextColor(Color.WHITE);
        workoutPlanLayout.addView(frequencyTextView);

        // Create a TextView for the workout plan level
        TextView levelTextView = new TextView(context);
        levelTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        levelTextView.setText("Level: " + level);
        levelTextView.setTextSize(16);
        levelTextView.setTextColor(Color.WHITE);
        workoutPlanLayout.addView(levelTextView);

        // Create a horizontal LinearLayout to hold the buttons
        LinearLayout buttonsLayout = new LinearLayout(context);
        buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setGravity(Gravity.CENTER);

        // Create the "See Plan" button
        Button seePlanButton = new Button(context);
        seePlanButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        seePlanButton.setTag(Pid); // Associate the button with the plan ID
        seePlanButton.setText("See Plan");
        seePlanButton.setTextSize(16);
        seePlanButton.setTextColor(Color.BLACK);
        seePlanButton.setBackgroundResource(R.drawable.button_background); // Apply green button background
        // Add click listener for the "See Plan" button
        seePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract the plan ID from the tag
                String planIdStr = (String) v.getTag();

                // Log the plan ID
                Log.d("PlanID", "Selected Plan ID: " + planIdStr);

                // Create an Intent to start the FullPlanActivity
                Intent intent = new Intent(context, FullPlan.class);

                // Pass the selected workout plan as an extra
                intent.putExtra("selected_plan", planIdStr); // Assuming 'name' uniquely identifies the workout plan

                // Start the activity
                context.startActivity(intent);
            }

        });
        buttonsLayout.addView(seePlanButton);

        // Create the "Add to My Plans" button
        Button addToMyPlansButton = new Button(context);
        addToMyPlansButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        addToMyPlansButton.setText("Add to My Plans");
        addToMyPlansButton.setTag(Pid); // Associate the button with the plan ID
        addToMyPlansButton.setTextSize(16);
        addToMyPlansButton.setTextColor(Color.BLACK);
        addToMyPlansButton.setBackgroundResource(R.drawable.button_background); // Apply green button background
        // Add click listener for the "Add to My Plans" button
        addToMyPlansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract the plan ID from the tag
                String planIdStr = (String) v.getTag();
                // Log the plan ID
                Log.d("choosePlanActivity", " Plan ID added to my plans " + planIdStr);

                // Get the user's UID
                String uid = UserSingleton.getInstance().getUid();

                // Reference to the user's plans in the database
                DatabaseReference userPlansRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("plans").child("public_plans");

                // Add the plan ID to the user's plans
                userPlansRef.child(planIdStr).setValue(true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Plan added successfully
                                Toast.makeText(context , "Plan added to My Plans", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to add the plan
                                Log.e("choosePlanActivity", "Failed to add plan to My Plans: " + e.getMessage());
                                Toast.makeText(context, "Failed to add plan to My Plans", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });
        buttonsLayout.addView(addToMyPlansButton);

        // Add the buttons layout to the workout plan layout
        workoutPlanLayout.addView(buttonsLayout);

        return workoutPlanLayout;
    }



    // Utility method to convert dp to pixels
    private static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    // Method to load public plans from the database and populate the UI
    private void loadPublicPlans(LinearLayout placeholder,Context context  ) {
        DatabaseReference PlansRef;


        // for public plans

             PlansRef = mDatabase.child("plans").child("public_plans");

        PlansRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot planSnapshot : dataSnapshot.getChildren()) {
                    // Get plan details
                    String name = planSnapshot.child("name").getValue(String.class);
                    String level = planSnapshot.child("level").getValue(String.class);
                    String type = planSnapshot.child("type").getValue(String.class);
                    long frequency = planSnapshot.child("wcounter").getValue(Long.class);
                    String planId = planSnapshot.getKey();


                    // Create UI components for the plan
                    LinearLayout planLayout =createWorkoutPlanUI (context, name, type, String.valueOf(frequency), level, planId);

                    // Add the UI components to the layout
                    placeholder.addView(planLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }



}



