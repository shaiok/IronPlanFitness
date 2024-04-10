package com.example.ironplan_fitness.Controller;

import com.example.ironplan_fitness.Model.Exercise;
import com.example.ironplan_fitness.Model.Plan;
import com.example.ironplan_fitness.Model.Workout;

import java.util.ArrayList;
import java.util.List;

public class hardCodedPlans {

    public static Plan plan1(){
        Plan atHomeWorkout = new Plan();
        atHomeWorkout.setName("At-Home Workout Routine for Men");
        atHomeWorkout.setLevel("Beginner");
        atHomeWorkout.setCategory("Home");





// Day 1: Legs, shoulders, and abs
        Workout day1 = new Workout();
        day1.setName("Day 1: Legs, Shoulders, and Abs");

        List<Exercise> day1Exercises = new ArrayList<>();
        day1.getExercises().add(new Exercise("Legs", "Dumbbell Squats", 3, 6, 80));
        day1.getExercises().add(new Exercise("Shoulders", "Standing Shoulder Press", 3, 6, 80));
        day1.getExercises().add(new Exercise("Shoulders", "Dumbbell Upright Rows", 2, 8, 100));
        day1.getExercises().add(new Exercise("Legs", "Dumbbell Lunge", 2, 8, 100));
        day1.getExercises().add(new Exercise("Hamstrings", "Romanian Dumbbell Deadlift", 2, 6, 80));
        day1.getExercises().add(new Exercise("Shoulders", "Lateral Raises", 3, 8, 100));
        day1.getExercises().add(new Exercise("Calves", "Seated Calf Raises", 4, 10, 120));
        day1.getExercises().add(new Exercise("Abs", "Crunches with Legs Elevated", 3, 10, 120));

        atHomeWorkout.getWorkouts().add(day1);

// Day 2: Chest and back
        Workout day2 = new Workout();
        day2.setName("Day 2: Chest and Back");


        day2.getExercises().add(new Exercise("Chest", "Dumbbell Bench Press or Floor Press", 3, 6, 8));
        day2.getExercises() .add(new Exercise("Back", "Dumbbell Bent-Over Rows", 3, 6, 8));
        day2.getExercises() .add(new Exercise("Chest", "Dumbbell Fly", 3, 8, 10));
        day2.getExercises() .add(new Exercise("Back", "One-Arm Dumbbell Rows", 3, 6, 8));
        day2.getExercises() .add(new Exercise("Chest", "Pushups", 3, 10, 12));
        day2.getExercises().add(new Exercise("Back/Chest", "Dumbbell Pullovers", 3, 10, 12));

        atHomeWorkout.getWorkouts().add(day2);

// Day 3: Arms and abs
        Workout day3 = new Workout();
        day3.setName("Day 3: Arms and Abs");

        day3.getExercises().add(new Exercise("Biceps", "Alternating Biceps Curls", 3, 8, 10));
        day3.getExercises().add(new Exercise("Triceps", "Overhead Triceps Extensions", 3, 8, 10));
        day3.getExercises().add(new Exercise("Biceps", "Seated Dumbbell Curls", 2, 10, 12));
        day3.getExercises() .add(new Exercise("Triceps", "Bench Dips", 2, 10, 12));
        day3.getExercises() .add(new Exercise("Biceps", "Concentration Curls", 3, 10, 12));
        day3.getExercises() .add(new Exercise("Triceps", "Dumbbell Kickbacks", 3, 8, 10));
        day3.getExercises().add(new Exercise("Abs", "Planks", 3, 30, 30)); // Assuming 30-second holds


        atHomeWorkout.getWorkouts().add(day3);

        return atHomeWorkout;
    }

    public static Plan plan2() {
        Plan beginnersWorkout = new Plan();
        beginnersWorkout.setName("Beginner’s Workout Routine for Men");
        beginnersWorkout.setLevel("Beginner");
        beginnersWorkout.setCategory("Gym");

        // Day 1: Full body
        Workout day1 = new Workout();
        day1.setName("Day 1: Full body");

        day1.getExercises().add(new Exercise("Legs", "Barbell Back Squats", 3, 5, 80));
        day1.getExercises().add(new Exercise("Chest", "Flat Barbell Bench Press", 3, 5, 80));
        day1.getExercises().add(new Exercise("Back", "Seated Cable Rows", 3, 6, 8));
        day1.getExercises().add(new Exercise("Shoulders", "Seated Dumbbell Shoulder Press", 3, 6, 8));
        day1.getExercises().add(new Exercise("Triceps", "Cable Rope Triceps Pushdowns", 3, 8, 10));
        day1.getExercises().add(new Exercise("Shoulders", "Lateral Raises", 3, 10, 12));
        day1.getExercises().add(new Exercise("Calves", "Seated Calf Raises", 3, 10, 12));
        day1.getExercises().add(new Exercise("Abs", "Planks", 3, 30, 30));

        beginnersWorkout.getWorkouts().add(day1);

        // Day 2: Full body
        Workout day2 = new Workout();
        day2.setName("Day 2: Full body");

        day2.getExercises().add(new Exercise("Back/Hamstrings", "Barbell or Trap Bar Deadlifts", 3, 5, 80));
        day2.getExercises().add(new Exercise("Back", "Pullups or Lat Pulldowns", 3, 6, 8));
        day2.getExercises().add(new Exercise("Chest", "Barbell or Dumbbell Incline Press", 3, 6, 8));
        day2.getExercises().add(new Exercise("Shoulders", "Machine Shoulder Press", 3, 6, 8));
        day2.getExercises().add(new Exercise("Biceps", "Barbell or Dumbbell Biceps Curls", 3, 8, 10));
        day2.getExercises().add(new Exercise("Shoulders", "Reverse Machine Fly", 3, 10, 12));
        day2.getExercises().add(new Exercise("Calves", "Standing Calf Raises", 3, 10, 12));

        beginnersWorkout.getWorkouts().add(day2);

        // Day 3: Full body
        Workout day3 = new Workout();
        day3.setName("Day 3: Full body");

        day3.getExercises().add(new Exercise("Legs", "Leg Press", 3, 5, 80));
        day3.getExercises().add(new Exercise("Back", "T-bar Rows", 3, 6, 8));
        day3.getExercises().add(new Exercise("Chest", "Machine or Dumbbell Chest Fly", 3, 6, 8));
        day3.getExercises().add(new Exercise("Shoulders", "One-arm Dumbbell Shoulder Press", 3, 6, 8));
        day3.getExercises().add(new Exercise("Triceps", "Dumbbell or Machine Triceps Extensions", 3, 8, 10));
        day3.getExercises().add(new Exercise("Shoulders", "Cable or Dumbbell Front Raises", 3, 10, 12));
        day3.getExercises().add(new Exercise("Calves", "Seated Calf Raises", 3, 10, 12));
        day3.getExercises().add(new Exercise("Abs", "Decline Crunches", 3, 10, 12));

        beginnersWorkout.getWorkouts().add(day3);

        return beginnersWorkout;
    }

}
