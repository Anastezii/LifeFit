package com.example.fitness;

import com.example.fitness.model.NutritionData;

import junit.framework.TestCase;

import org.junit.Test;

public class NutritionDataTest extends TestCase {

    @Test
    public void testGetGrams() {
        NutritionData nutritionData=new NutritionData("12/12/2023", 2.32, 12.36, 125.3, 1.3,25.3);
        assertEquals(25.3,nutritionData.getGrams());
    }


}