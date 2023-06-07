package com.example.fitness.model;

public class NutritionData {

    private String date;
    private double protein;
    private double carbohydrates;
    private double calories;
    private double fats;
    private double grams;

    public NutritionData() {
    }

    public NutritionData(String date, double protein, double carbohydrates, double calories, double fats,double grams) {
        this.date = date;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
        this.fats = fats;
        this.grams=grams;
    }

    public String getDate() {
        return date;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public double getCalories() {
        return calories;
    }

    public double getFats() {
        return fats;
    }

    public double getGrams() {
        return grams;
    }



}
