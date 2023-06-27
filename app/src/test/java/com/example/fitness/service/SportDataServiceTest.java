package com.example.fitness.service;

import static org.junit.Assert.*;

import com.example.fitness.model.NutritionData;

import org.junit.Test;

public class SportDataServiceTest {

    @Test
    public void getDate() {
        SportDataService sportDataService=new SportDataService("12/12/2023", 125,25);
        assertEquals("12/12/2023",sportDataService.getDate());
    }

    @Test
    public void getCalories() {
        SportDataService sportDataService=new SportDataService("12/12/2023", 125,25);
        assertEquals(125,sportDataService.getCalories());
    }

    @Test
    public void getMinutes() {
        SportDataService sportDataService=new SportDataService("12/12/2023", 125,25);
        assertEquals(25,sportDataService.getMinutes());

    }

    @Test
    public void testCalculateSportDataServiceMethod() {
        // Arrange
        int minute = 30;
        int caloriesNew = 10;
        String date = "2023-06-27";
        int expectedCalories = caloriesNew * minute;

        SportDataService result = calculateSportDataService(minute, caloriesNew, date);

        assertEquals(date, result.getDate());
        assertEquals(expectedCalories, result.getCalories());
        assertEquals(minute, result.getMinutes());
    }

    public SportDataService calculateSportDataService(int minute, int caloriesNew, String date) {
        caloriesNew = caloriesNew * minute;
        return new SportDataService(date, caloriesNew, minute);
    }
}