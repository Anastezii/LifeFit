package com.example.fitness;

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

    public NutritionData calculateNutritionData(double grams, double proteinPer100g, double carbohydratesPer100g, double caloriesPer100g, double fatsPer100g, String date) {
        double protein = proteinPer100g * (grams / 100);
        double carbohydrates = carbohydratesPer100g * (grams / 100);
        double calories = caloriesPer100g * (grams / 100);
        double fats = fatsPer100g * (grams / 100);

        return new NutritionData(date, protein, carbohydrates, calories, fats,grams);
    }


}
