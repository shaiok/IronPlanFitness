package com.example.ironplan_fitness.Model;

import java.util.ArrayList;

public class Plan {
    private  String id;
    private String name ;

    private String category;

    private String level;

    private ArrayList<Workout> workouts ;

    public int getWcounter() {
        return wcounter;
    }

    public Plan setWcounter(int wcounter) {
        this.wcounter = wcounter;
        return this;
    }

    private int wcounter;


    public Plan(String name, String category, String level, int wcounter) {
        this.name = name;
        this.category = category;
        this.level = level;
        this.workouts = workouts;
        this.wcounter = wcounter;
    }
    public Plan(String name, String category, String level, ArrayList<Workout> workouts) {
        this.name = name;
        this.category = category;
        this.level = level;
        this.workouts = workouts;
    }
    public Plan(){
    this.workouts = new ArrayList<>();
    }

    public Plan(String name, String category, String level) {
        this.name = name;
        this.category = category;
        this.level = level;
        this.workouts = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    public Plan setName(String name) {
        this.name = name;
        return this;
    }

    public Plan setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
        return this;
    }

    public Plan setId(String id) {
        this.id = id;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public String getLevel() {
        return level;
    }

    public Plan setCategory(String category) {
        this.category = category;
        return this;
    }

    public Plan setLevel(String level) {
        this.level = level;
        return this;
    }


}
