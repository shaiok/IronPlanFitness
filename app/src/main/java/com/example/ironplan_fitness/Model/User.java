package com.example.ironplan_fitness.Model;

import java.util.HashMap;
import java.util.Map;


public class User {

    private String fullName;

    private Map<String, Integer> plans; // Map of plan IDs

    // Default constructor required for Firebase
    public User() {
    }

    public User(String fullName) {
        this.fullName = fullName;
        this.plans = new HashMap<>();
    }

    public String getFullName() {
        return fullName;
    }

    public Map<String, Integer> getPlans() {
        return plans;
    }
}