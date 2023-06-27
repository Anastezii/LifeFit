package com.example.fitness.service;

import static org.junit.Assert.*;

import org.junit.Test;

public class NutritionDataServiceTest {

    @Test
    public void getDate() {
        NutritionDataService nutritionDataService=new NutritionDataService("12/12/2023", 2.32, 12.36, 125.3, 1.3,25.3);
        assertEquals("12/12/2023",nutritionDataService.getDate());
    }

    @Test
    public void getProtein() {
        NutritionDataService nutritionDataService=new NutritionDataService("12/12/2023", 2.32, 12.36, 125.3, 1.3,25.3);
        assertEquals(2.32,nutritionDataService.getProtein());
    }

    @Test
    public void getCarbohydrates() {
        NutritionDataService nutritionDataService=new NutritionDataService("12/12/2023", 2.32, 12.36, 125.3, 1.3,25.3);
        assertEquals(12.36,nutritionDataService.getCarbohydrates());
    }

    @Test
    public void getCalories() {
        NutritionDataService nutritionDataService=new NutritionDataService("12/12/2023", 2.32, 12.36, 125.3, 1.3,25.3);
        assertEquals(125.3,nutritionDataService.getCalories());
    }

    @Test
    public void getFats() {
        NutritionDataService nutritionDataService=new NutritionDataService("12/12/2023", 2.32, 12.36, 125.3, 1.3,25.3);
        assertEquals(1.3,nutritionDataService.getFats());
    }

    @Test
    public void getGrams() {
        NutritionDataService nutritionDataService=new NutritionDataService("12/12/2023", 2.32, 12.36, 125.3, 1.3,25.3);
        assertEquals(25.3,nutritionDataService.getGrams());
    }

    @Test
    public void testCalculateNutritionDataMethod() {
        // Arrange
        double grams = 200.0;
        double proteinPer100g = 5.0;
        double carbohydratesPer100g = 10.0;
        double caloriesPer100g = 100.0;
        double fatsPer100g = 8.0;
        String date = "2023-06-27";

        // Calculate expected values
        double expectedProtein = proteinPer100g * (grams / 100);
        double expectedCarbohydrates = carbohydratesPer100g * (grams / 100);
        double expectedCalories = caloriesPer100g * (grams / 100);
        double expectedFats = fatsPer100g * (grams / 100);

        // Act
        NutritionDataService result = calculateNutritionData(grams, proteinPer100g, carbohydratesPer100g,
                caloriesPer100g, fatsPer100g, date);

        // Assert
        assertEquals(date, result.getDate());
        assertEquals(expectedProtein, result.getProtein(), 0.01); // Using delta for double values
        assertEquals(expectedCarbohydrates, result.getCarbohydrates(), 0.01);
        assertEquals(expectedCalories, result.getCalories(), 0.01);
        assertEquals(expectedFats, result.getFats(), 0.01);
        assertEquals(grams, result.getGrams(), 0.01);
    }

    public NutritionDataService calculateNutritionData(double grams, double proteinPer100g, double carbohydratesPer100g,
                                                       double caloriesPer100g, double fatsPer100g, String date) {
        double protein = proteinPer100g * (grams / 100);
        double carbohydrates = carbohydratesPer100g * (grams / 100);
        double calories = caloriesPer100g * (grams / 100);
        double fats = fatsPer100g * (grams / 100);

        return new NutritionDataService(date, protein, carbohydrates, calories, fats, grams);
    }
}