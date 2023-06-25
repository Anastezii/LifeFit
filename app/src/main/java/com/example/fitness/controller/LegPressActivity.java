package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness.R;
import com.example.fitness.service.ExerciseDataService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LegPressActivity extends AppCompatActivity {

    private TextView txt_instructions;
    private Button write_exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leg_press);


        txt_instructions=findViewById(R.id.description_LegPress);
        write_exercise=findViewById(R.id.button_LegPress);

        DatabaseReference exercisesRef = FirebaseDatabase.getInstance().getReference("Exercises/Abs/Leg Press");
        exercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String description = dataSnapshot.child("instructions").getValue(String.class);
                    String equipment = dataSnapshot.child("equipment").getValue(String.class);
                    txt_instructions.setText("Needed equipment :"+equipment+"  "+description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LegPressActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        write_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference exerciseRef = FirebaseDatabase.getInstance().getReference("Exercises/Abs/Leg Press");
                exerciseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Handle data changes here
                        if (snapshot.exists()) {
                            // Get the nutritional values for the selected food item
                            try {

                                int calories = snapshot.child("caloriesBurnedPerMinute").getValue(Integer.class);

                                int new_Calories=calories*10;

                                Date currentDate = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String dateString = dateFormat.format(currentDate);

                                ExerciseDataService exerciseData = new ExerciseDataService(dateString,  new_Calories);

                                writeNutritionDataToFirebase(exerciseData);

                            } catch (DatabaseException e ){
                                Toast.makeText(LegPressActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LegPressActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });




    }

    private void writeNutritionDataToFirebase(ExerciseDataService exerciseData) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Sport_data");
        String key = "LegPress";
        dbRef.child(key).setValue(exerciseData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(LegPressActivity.this,"Exercise is added successfully!",
                            Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(LegPressActivity.this,"Something went wrong!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}