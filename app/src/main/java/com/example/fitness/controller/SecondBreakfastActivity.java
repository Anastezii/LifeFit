package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness.R;
import com.example.fitness.model.NutritionData;
import com.example.fitness.service.NutritionDataService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class SecondBreakfastActivity extends AppCompatActivity {


    private EditText grams;
    private Spinner foodSpinner;
    private Button add;
    private Double proteins,carbohydrates,calories,fats;
    private TextView txt_calories;

    DatabaseReference reference;
    //List<Food> foodListArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_breakfast);

        grams=findViewById(R.id.gramms_dinner);
        foodSpinner=findViewById(R.id.spinner_dinner);
        add=findViewById(R.id.add_dinner);
        txt_calories=findViewById(R.id.show_calories_second_breakfast);

        reference= FirebaseDatabase.getInstance().getReference("Food");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> foodList=new ArrayList<>();
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot foodSnapShot:snapshot.getChildren()){
                        try{
                            HashMap<String, Object> data = (HashMap<String, Object>) foodSnapShot.getValue();
                            for( String item : data.keySet()){
                                foodList.add(item);
                            }

                        }catch (DatabaseException e ){
                            Toast.makeText(SecondBreakfastActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, foodList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                foodSpinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SecondBreakfastActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFood = foodSpinner.getSelectedItem().toString();
                DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("Food").child(selectedFood);
                foodRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Handle data changes here
                        if (snapshot.exists()) {
                            // Get the nutritional values for the selected food item
                            try {
                                HashMap<String, Object> foodNutrition = (HashMap<String, Object>) snapshot.child(selectedFood).getValue();
                                proteins = Double.parseDouble(foodNutrition.get("proteins").toString());
                                carbohydrates = Double.parseDouble(foodNutrition.get("carbohydrates").toString());
                                calories = Double.parseDouble(foodNutrition.get("Calories").toString());
                                fats = Double.parseDouble(foodNutrition.get("Fats").toString());

                                double txt_grams=Double.parseDouble(grams.getText().toString());

                                Date currentDate = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String dateString = dateFormat.format(currentDate);

                                NutritionData nutritionData = new NutritionData(dateString, proteins, carbohydrates, calories, fats, txt_grams);
                                NutritionDataService nutritionDataService=new NutritionDataService(dateString, proteins, carbohydrates, calories, fats, txt_grams);
                                nutritionDataService.calculateNutritionData(txt_grams,proteins,carbohydrates,calories,fats,dateString);

                                writeNutritionDataToFirebase(nutritionDataService);

                                txt_calories.setText("Calories "+ String.valueOf(nutritionData.getCalories()));


                            } catch (DatabaseException e ){
                                Toast.makeText(SecondBreakfastActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SecondBreakfastActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }

    private void writeNutritionDataToFirebase(NutritionDataService nutritionData) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Nutrition_data");
        DatabaseReference nutritionDataRef = dbRef.child("Second_breakfast");
        String key = nutritionDataRef.push().getKey();
        nutritionDataRef.child(key).setValue(nutritionData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(SecondBreakfastActivity.this,"Second breakfast is added successfully!",
                            Toast.LENGTH_LONG).show();

                   /* Intent intent=new Intent(SecondBreakfastActivity.this,FoodActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // to close Activity*/


                }else{
                    Toast.makeText(SecondBreakfastActivity.this,"Something went wrong!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}