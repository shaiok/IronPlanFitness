package com.example.ironplan_fitness;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ironplan_fitness.Controller.FirebaseDatabaseHelper;
import com.example.ironplan_fitness.Model.Exercise;
import com.example.ironplan_fitness.Model.Plan;
import com.example.ironplan_fitness.Model.Workout;

import java.util.ArrayList;

public class MakePlan extends Fragment {

    private LinearLayout planLayout;

    private  Plan plan ;

    private FirebaseDatabaseHelper databaseHelper;

    private int workoutCount = 0;

    private ArrayList<LinearLayout> workoutLayouts = new ArrayList<>();

    public MakePlan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_make_plan, container, false);

        // Initialize the workout layout
        planLayout = rootView.findViewById(R.id.makePlan_mainLayout);

        databaseHelper = FirebaseDatabaseHelper.getInstance();

        // Set up the button click listeners
        setUpAddWorkoutButton(rootView);
        setUpSubmitPlanButton(rootView);

        return rootView;
    }

    private void setUpAddWorkoutButton(View rootView) {
        Button addWorkoutButton = rootView.findViewById(R.id.makePlan_addWorkoutButton);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkoutFields(rootView);
            }
        });
    }

    private void setUpSubmitPlanButton(View rootView) {
        Button submitPlanButton = rootView.findViewById(R.id.makePlan_submitPlanButton);
        submitPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputFields(rootView)) {
                    Plan plan = createPlanFromUI(rootView);
                    databaseHelper.addPlan(plan, 0);
                    showToast("plan successfully added to your plans");
                }
            }
        });
    }

    private void addWorkoutFields(@NonNull View rootView) {
        workoutCount++;

        // Create a new LinearLayout for the workout
        LinearLayout workoutContainer = createLinearLayout(LinearLayout.VERTICAL);
        addViewToContainer(workoutContainer, planLayout);
        workoutLayouts.add(workoutContainer);

        // Add "Workout X" text
        TextView workoutTextView = createTextView("Workout " + workoutCount + ":", getResources().getColor(R.color.black));
        addViewToContainer(workoutTextView, workoutContainer);

        // Add EditText for workout name
        EditText workoutNameEditText = createEditText("Enter workout name", getResources().getColor(R.color.black));
        workoutNameEditText.setBackgroundResource(R.drawable.edittext_background);
        addViewToContainer(workoutNameEditText, workoutContainer);

        // Add button for adding exercises
        Button addExerciseButton = createButton("Add Exercise", getResources().getDrawable(R.drawable.button_background));
        addExerciseButton.setBackgroundResource(R.drawable.button_background);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseFields(workoutContainer);
            }
        });
        addViewToContainer(addExerciseButton, workoutContainer);

        // Add collapse button
        Button collapseButton = createButton("Hide", null);
        collapseButton.setBackgroundResource(R.drawable.edittext_background);
        collapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWorkoutVisibility(workoutContainer);
            }
        });
        collapseButton.setVisibility(View.GONE);
        addViewToContainer(collapseButton, workoutContainer);

        // Add reverse button
        Button reverseButton = createButton("Show", null);
        reverseButton.setBackgroundResource(R.drawable.button_background);
        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWorkoutVisibility(workoutContainer);
            }
        });
        addViewToContainer(reverseButton, workoutContainer);
    }

    private EditText createEditText(String hint, int textColor) {
        EditText editText = new EditText(getContext());
        editText.setBackgroundResource(R.drawable.edittext_background);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        editText.setHint(hint);
        editText.setHintTextColor(textColor);
        editText.setTextColor(textColor);
        return editText;
    }

    private Button createButton(String text, Drawable icon) {
        Button button = new Button(getContext());
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        button.setText(text);
        button.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        return button;
    }

    private TextView createTextView(String text, int textColor) {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(text);
        textView.setTextColor(textColor);
        return textView;
    }

    private LinearLayout createLinearLayout(int orientation) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(orientation);
        linearLayout.setPadding(0, 16, 0, 16);
        return linearLayout;
    }

    private void addExerciseFields(LinearLayout workoutContainer) {
        // Create a new LinearLayout for exercise details
        LinearLayout exerciseContainer = createLinearLayout(LinearLayout.VERTICAL);
        addViewToContainer(exerciseContainer, workoutContainer);

        // Add EditText for exercise name
        EditText exerciseNameEditText = createEditText("Enter exercise name", getResources().getColor(R.color.black));
        addViewToContainer(exerciseNameEditText, exerciseContainer);

        // Add Spinner for muscle
        Spinner muscleSpinner = createMuscleSpinner();
        addViewToContainer(muscleSpinner, exerciseContainer);

        // Add EditTexts for reps, sets, and rest time
        addViewToContainer(createEditText("Enter reps", getResources().getColor(R.color.black)), exerciseContainer);
        addViewToContainer(createEditText("Enter sets", getResources().getColor(R.color.black)), exerciseContainer);
        addViewToContainer(createEditText("Enter rest time", getResources().getColor(R.color.black)), exerciseContainer);
    }
    private Spinner createMuscleSpinner() {
        Spinner muscleSpinner = new Spinner(getContext());
        muscleSpinner.setBackgroundResource(R.drawable.edittext_background); // Set background resource
        muscleSpinner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ArrayAdapter<CharSequence> muscleAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.muscles, android.R.layout.simple_spinner_item);
        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        muscleSpinner.setAdapter(muscleAdapter);
        return muscleSpinner;
    }


    private void toggleWorkoutVisibility(LinearLayout workoutContainer) {
        // Toggle visibility of exercise details
        for (int i = 2; i < workoutContainer.getChildCount(); i++) { // Start from index 2 to skip workout name and add exercise button
            View child = workoutContainer.getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                child.setVisibility(View.GONE);
            } else {
                child.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addViewToContainer(View view, ViewGroup container) {
        container.addView(view);
    }

    private Plan createPlanFromUI(View rootView) {
        // Get plan details from UI
        EditText planName = rootView.findViewById(R.id.makePlan_PlanNameEditText);
        EditText planCategory =  rootView.findViewById(R.id.makePlan_categoryEditText);
        Spinner planLevel =  rootView.findViewById(R.id.makePlan_levelSpinner);

         plan = new Plan();
        plan.setName(planName.getText().toString());
        plan.setCategory(planCategory.getText().toString());
        plan.setLevel(planLevel.getSelectedItem().toString());


        Log.d("MakePlanActivity", "plan name  "+plan.getName() +"category: "+plan.getCategory() +"plan level:"+plan.getLevel());

        // Loop through workouts
        for (int i = 0; i < workoutLayouts.size(); i++) {
            View workoutView = workoutLayouts.get(i);

            if (workoutView instanceof LinearLayout) {
                LinearLayout workoutContainer = (LinearLayout) workoutView;

                Workout workout = new Workout();
                // Get workout name
                EditText workoutNameEditText = (EditText) workoutContainer.getChildAt(1);
                String workoutName = workoutNameEditText.getText().toString();

                workout.setName(workoutName);

                // Loop through exercises
                for (int j = 2; j < workoutContainer.getChildCount(); j++) {
                    View exerciseView = workoutContainer.getChildAt(j);

                    if (exerciseView instanceof LinearLayout) {
                        LinearLayout exerciseContainer = (LinearLayout) exerciseView;

                        // Get exercise details
                        EditText exerciseNameEditText = (EditText) exerciseContainer.getChildAt(0);
                        Spinner muscleSpinner = (Spinner) exerciseContainer.getChildAt(1);
                        EditText repsEditText = (EditText) exerciseContainer.getChildAt(2);
                        EditText setsEditText = (EditText) exerciseContainer.getChildAt(3);
                        EditText restEditText = (EditText) exerciseContainer.getChildAt(4);

                        String exerciseName = exerciseNameEditText.getText().toString();
                        String muscleName = muscleSpinner.getSelectedItem().toString();
                        int reps = Integer.parseInt(repsEditText.getText().toString());
                        int sets = Integer.parseInt(setsEditText.getText().toString());
                        int rest = Integer.parseInt(restEditText.getText().toString());

                        Exercise exercise = new Exercise(exerciseName, muscleName, reps, sets, rest);
                        workout.getExercises().add(exercise);
                    }
                }


                plan.getWorkouts().add(workout);
            }
        }

        return plan;
    }
    private boolean checkInputFields(View rootView) {
        // Check plan details
        EditText planName = rootView.findViewById(R.id.makePlan_PlanNameEditText);
        EditText planCategory =  rootView.findViewById(R.id.makePlan_categoryEditText);
        Spinner planLevel =  rootView.findViewById(R.id.makePlan_levelSpinner);

        if (planName.getText().toString().isEmpty()) {
            showToast("Please enter plan name");
            return false;
        }

        if (planCategory.getText().toString().isEmpty()) {
            showToast("Please enter plan category");
            return false;
        }

        // Check workout details
        for (int i = 0; i < workoutLayouts.size(); i++) {
            View workoutView = workoutLayouts.get(i);

            if (workoutView instanceof LinearLayout) {
                LinearLayout workoutContainer = (LinearLayout) workoutView;

                // Get workout name
                EditText workoutNameEditText = (EditText) workoutContainer.getChildAt(1);
                String workoutName = workoutNameEditText.getText().toString();

                if (workoutName.isEmpty()) {
                    showToast("Please enter workout name for Workout " + (i + 1));
                    return false;
                }

                // Check exercise details
                for (int j = 2; j < workoutContainer.getChildCount(); j++) {
                    View exerciseView = workoutContainer.getChildAt(j);

                    if (exerciseView instanceof LinearLayout) {
                        LinearLayout exerciseContainer = (LinearLayout) exerciseView;

                        // Get exercise details
                        EditText exerciseNameEditText = (EditText) exerciseContainer.getChildAt(0);
                        EditText repsEditText = (EditText) exerciseContainer.getChildAt(2);
                        EditText setsEditText = (EditText) exerciseContainer.getChildAt(3);
                        EditText restEditText = (EditText) exerciseContainer.getChildAt(4);

                        if (exerciseNameEditText.getText().toString().isEmpty()) {
                            showToast("Please enter exercise name for Workout " + (i + 1));
                            return false;
                        }

                        if (repsEditText.getText().toString().isEmpty()) {
                            showToast("Please enter reps for Workout " + (i + 1));
                            return false;
                        }

                        if (setsEditText.getText().toString().isEmpty()) {
                            showToast("Please enter sets for Workout " + (i + 1));
                            return false;
                        }

                        if (restEditText.getText().toString().isEmpty()) {
                            showToast("Please enter rest time for Workout " + (i + 1));
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}
