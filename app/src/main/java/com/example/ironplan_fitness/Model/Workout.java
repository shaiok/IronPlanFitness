package com.example.ironplan_fitness.Model;

import java.util.ArrayList;

public class Workout {



    private String id ;

    private String name ;
    private int workout_num;
    private ArrayList<Exercise> exercises;

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public Workout() {
        this.exercises= new ArrayList<>();
    }

    public Workout(int workout_num, ArrayList<Exercise> exercises) {
        this.workout_num = workout_num;
        this.exercises = exercises;

    }



    public String getName() {
        return name;
    }

    public Workout setName(String name) {
        this.name = name;
        return this;
    }

    public Workout setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public int getWorkout_num() {
        return workout_num;
    }


    public Workout setWorkout_num(int workout_num) {
        this.workout_num = workout_num;
        return this;
    }

    public Workout setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
        return this;
    }
}
