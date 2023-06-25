package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness.R;
import com.example.fitness.model.SportData;
import com.example.fitness.service.SportDataService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SportActivity extends AppCompatActivity {

    private EditText minutes;
    private Spinner sportSpinner;
    private Button add;
    private int calories;
    private TextView txt_calories;
    private SearchView searchView;

    DatabaseReference reference;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        minutes=findViewById(R.id.minutes);
        sportSpinner=findViewById(R.id.spinner_sport);
        add=findViewById(R.id.add_sport);
        txt_calories=findViewById(R.id.show_calories_sport);
        searchView = findViewById(R.id.searchView);

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
                        return;
                    case R.id.bottom_eat:
                        startActivity(new Intent(getApplicationContext(), FoodActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
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

        getSupportActionBar().setTitle("Sport");

        reference= FirebaseDatabase.getInstance().getReference("Activity");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the spinner items based on the search query
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> sportList=new ArrayList<>();
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot foodSnapShot:snapshot.getChildren()){
                        try{
                            HashMap<String, Object> data = (HashMap<String, Object>) foodSnapShot.getValue();
                            for( String item : data.keySet()){
                                sportList.add(item);
                            }

                        }catch (DatabaseException e ){
                            Toast.makeText(SportActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                }
                adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, sportList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sportSpinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SportActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSport = sportSpinner.getSelectedItem().toString();
                DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("Activity").child(selectedSport);
                foodRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Handle data changes here
                        if (snapshot.exists()) {
                            // Get the nutritional values for the selected food item
                            try {
                                HashMap<String, Object> foodNutrition = (HashMap<String, Object>) snapshot.child(selectedSport).getValue();

                                calories = Integer.parseInt(foodNutrition.get("calories").toString());


                                int txt_minutes=Integer.parseInt(minutes.getText().toString());

                                int new_Calories=calories*txt_minutes;

                                Date currentDate = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String dateString = dateFormat.format(currentDate);

                                SportData sportData = new SportData(dateString,  new_Calories, txt_minutes);
                                SportDataService sportDataService=new SportDataService(dateString,  new_Calories, txt_minutes);
                                sportDataService.calculateSportDataService(txt_minutes,new_Calories,dateString);

                                writeNutritionDataToFirebase(sportDataService);

                                txt_calories.setText("Calories "+ String.valueOf(sportData.getCalories()));



                            } catch (DatabaseException e ){
                                Toast.makeText(SportActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SportActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }

    private void writeNutritionDataToFirebase(SportDataService sportData) {
        String selectedSport = sportSpinner.getSelectedItem().toString();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Sport_data");
        String key =selectedSport;
        dbRef.child(key).setValue(sportData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(SportActivity.this,"Sport is added successfully!",
                            Toast.LENGTH_LONG).show();

                    /*Intent intent=new Intent(SportActivity.this,SportActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // to close Activity*/


                }else{
                    Toast.makeText(SportActivity.this,"Something went wrong!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}