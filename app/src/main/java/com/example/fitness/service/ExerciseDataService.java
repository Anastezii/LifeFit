package com.example.fitness.service;

public class ExerciseDataService {

    private String date;
    private int calories;

    public ExerciseDataService() {
    }

    public ExerciseDataService(String date, int calories) {
        this.date = date;
        this.calories = calories;
    }

    public String getDate() {
        return date;
    }

    public int getCalories() {
        return calories;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
