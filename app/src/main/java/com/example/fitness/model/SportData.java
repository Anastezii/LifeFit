package com.example.fitness.model;

public class SportData {

    private String date;
    private int calories;
    private int minutes;


    public SportData() {
    }

    public SportData(String date, int calories, int minutes) {
        this.date = date;
        this.calories = calories;
        this.minutes = minutes;
    }

    public String getDate() {
        return date;
    }

    public int getCalories() {
        return calories;
    }

    public int getMinutes() {
        return minutes;
    }


}
