package com.example.ironplan_fitness;

import static com.example.ironplan_fitness.Controller.hardCodedPlans.plan1;
import static com.example.ironplan_fitness.Controller.hardCodedPlans.plan2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ironplan_fitness.Controller.FirebaseDatabaseHelper;
import com.example.ironplan_fitness.Controller.UserSingleton;
import com.example.ironplan_fitness.Model.Plan;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    TextView choosePlan;
    TextView myPlan;
    TextView makePlan;

    private FirebaseDatabaseHelper databaseHelper;
    private int currentFragment = 2; // Default to Choose Plan fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = FirebaseDatabaseHelper.getInstance();

        // for adding a hard coded plan
     //  databaseHelper.addPlan(plan2());
     //   databaseHelper.addPlan(plan1());

        findViews();

        TextView logoutTextView = findViewById(R.id.option4);
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the User Singleton object to null
                UserSingleton.getInstance().setUser(null);

                // Navigate back to the Login activity
                Intent intent = new Intent(Home.this, LogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to it from the LoginActivity
            }
        });



        int lastFragmentNumber = getIntent().getIntExtra("lastFragment", 2); // Default to Choose Plan fragment if not provided
        loadFragment(lastFragmentNumber);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentFragment", currentFragment);
    }

    private void findViews() {
        makePlan = findViewById(R.id.option1);
        choosePlan = findViewById(R.id.option2);
        myPlan = findViewById(R.id.option3);

        makePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(1); // Pass a unique identifier for each fragment
            }
        });

        choosePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(2); // Pass a unique identifier for each fragment
            }
        });

        myPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(3); // Pass a unique identifier for each fragment
            }
        });
    }

    private void loadFragment(int fragmentNumber) {
        Fragment fragment;
        switch (fragmentNumber) {
            case 1:
                fragment = new MakePlan();
                break;
            case 2:
                fragment = new chosePlan();
                break;
            case 3:
                fragment = new MyPlans();
                break;
            default:
                fragment = null;
        }

        if (fragment != null) {
            currentFragment = fragmentNumber; // Update the current fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }


    }

