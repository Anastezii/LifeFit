package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.fitness.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FoodActivity extends AppCompatActivity {

    private Button breakfast,second_breakfast,dinner,afternoon_snack,supper,snack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return;
                    case R.id.bottom_activity:
                        startActivity(new Intent(getApplicationContext(), SportActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return;
                    case R.id.bottom_eat:
                        return;
                    case R.id.bottom_calories:
                        startActivity(new Intent(getApplicationContext(), CaloriesActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return;
                    case R.id.bottom_exercises:
                        startActivity(new Intent(getApplicationContext(), ExercisesActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return;
                }
                return;
            }
        });

        getSupportActionBar().setTitle("Food");

        breakfast=findViewById(R.id.button_breakfast);
        second_breakfast=findViewById(R.id.button_second_breakfast);
        dinner=findViewById(R.id.button_dinner);
        afternoon_snack=findViewById(R.id.button_afternoon_snack);
        supper=findViewById(R.id.button_supper);
        snack=findViewById(R.id.button_snack);

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodActivity.this, BreakfastActivty.class);
                startActivity(intent);
            }
        });

        second_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodActivity.this,SecondBreakfastActivity.class);
                startActivity(intent);
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodActivity.this,DinnerActivity.class);
                startActivity(intent);
            }
        });

        afternoon_snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodActivity.this, AfternoonSnackActivity.class);
                startActivity(intent);
            }
        });

        supper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodActivity.this,SupperActivity.class);
                startActivity(intent);
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodActivity.this,SnackActivity.class);
                startActivity(intent);
            }
        });

    }
}