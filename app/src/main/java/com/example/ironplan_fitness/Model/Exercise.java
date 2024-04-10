package com.example.ironplan_fitness.Model;

public class Exercise {
    private String id ;
    private String name ;
    private String muscle;



    private int reps ;
    private String duration ;
    private int sets;

    private int rest_time;

    private String tutorial_link;

    public Exercise(String name, String muscle, int sets,  int reps, int rest_time) {
        this.name = name;
        this.muscle = muscle;
        this.sets = sets;
        this.reps = reps;


        this.rest_time = rest_time;

    }

    public Exercise( String name, String muscle, int reps, String duration, int sets, int rest_time, String tutorial_link) {
        this.name = name;
        this.muscle = muscle;
        this.reps = reps;
        this.duration = duration;
        this.sets = sets;
        this.rest_time = rest_time;
        this.tutorial_link = tutorial_link;
    }

    public Exercise() {

    }

    public Exercise setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMuscle() {
        return muscle;
    }

    public int getReps() {
        return reps;
    }

    public String getDuration() {
        return duration;
    }

    public int getSets() {
        return sets;
    }

    public int getRest_time() {
        return rest_time;
    }

    public String getTutorial_link() {
        return tutorial_link;
    }

    public Exercise setName(String name) {
        this.name = name;
        return this;
    }

    public Exercise setMuscle(String muscle) {
        this.muscle = muscle;
        return this;
    }

    public Exercise setReps(int reps) {
        this.reps = reps;
        return this;
    }

    public Exercise setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public Exercise setSets(int sets) {
        this.sets = sets;
        return this;
    }

    public Exercise setRest_time(int rest_time) {
        this.rest_time = rest_time;
        return this;
    }

    public Exercise setTutorial_link(String tutorial_link) {
        this.tutorial_link = tutorial_link;
        return this;
    }
}
