package com.example.fitness.model;

public class Food {

    private String name;
    private int calories;
    private Double fats,carbohydrates,proteins;

    public Food() {
    }

    public Food(String name, int calories, Double fats, Double carbohydrates, Double proteins) {
        this.name = name;
        this.calories = calories;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public Double getFats() {
        return fats;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public Double getProteins() {
        return proteins;
    }
}
