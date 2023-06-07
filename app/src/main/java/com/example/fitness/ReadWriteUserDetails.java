package com.example.fitness;

public class ReadWriteUserDetails {

    public String name,goals,gender,email;
    public int age,height,calories;
    public Double weight;

    public ReadWriteUserDetails() {
    }

    public int getCalories() {
        return calories;
    }

    public ReadWriteUserDetails(String name, String gender, int age, int height, Double weight, String goals,int calories) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.goals=goals;
        this.calories=calories;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getGoals() {
        return goals;
    }

    public String getGender() {
        return gender;
    }

    public  int getHeight() {
        return height;
    }

    public int getAge() {
        return age;
    }

    public Double getWeight() {
        return weight;
    }

    public ReadWriteUserDetails(String txt_name,String txt_email,int txt_age, String txt_goals, int txt_height, String txt_gender,  double txt_weight,int calories) {

        this.name=txt_name;
        this.email=txt_email;
        this.age=txt_age;
        this.goals=txt_goals;
        this.height=txt_height;
        this.gender=txt_gender;
        this.weight=txt_weight;
        this.calories=calories;

    }
}
