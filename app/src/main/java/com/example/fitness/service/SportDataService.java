package com.example.fitness.service;

import com.example.fitness.model.SportData;

public class SportDataService {

    private String date;
    private int calories;
    private int minutes;


    public SportDataService() {
    }

    public SportDataService(String date, int calories, int minutes) {
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

    public SportDataService calculateSportData(int minute, int caloriesNew, String date) {

        caloriesNew = caloriesNew * minute;

        return new SportDataService(date, caloriesNew, minute);
    }

}
