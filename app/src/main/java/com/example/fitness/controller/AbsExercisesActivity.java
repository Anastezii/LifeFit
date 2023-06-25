package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fitness.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AbsExercisesActivity extends AppCompatActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abs_exercises);


        list = findViewById(R.id.ab_listView);

        ArrayList<String> itemList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        list.setAdapter(adapter);

// Получите ссылку на нужную коллекцию или узел в Firebase
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Exercises");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                itemList.clear();

                for (DataSnapshot muscleGroupSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot exerciseSnapshot : muscleGroupSnapshot.getChildren()) {
                        String exerciseName = exerciseSnapshot.child("name").getValue(String.class);
                        String typeName = exerciseSnapshot.child("type").getValue(String.class);
                        if (typeName != null && typeName.equals("ab") && exerciseName!=null){
                            itemList.add(exerciseName);
                        }
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AbsExercisesActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);

                // Создайте Intent для открытия новой активности
                if (selectedItem.equals("Bicycle Crunches") && selectedItem!=null){
                    Intent intent = new Intent(AbsExercisesActivity.this, BicycleCrunchesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if (selectedItem.equals("Crunches") && selectedItem!=null){
                    Intent intent = new Intent(AbsExercisesActivity.this, CrunchesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Leg Raises") && selectedItem!=null){
                    Intent intent = new Intent(AbsExercisesActivity.this, LegRaisesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Plank") && selectedItem!=null){
                    Intent intent = new Intent(AbsExercisesActivity.this, PlankActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Russian Twists") && selectedItem!=null){
                    Intent intent = new Intent(AbsExercisesActivity.this, RussianTwistsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }

            }
        });

    }
}