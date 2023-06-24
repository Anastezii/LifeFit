package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.fitness.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Top_part_exercises_Activity extends AppCompatActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_part_exercises);

        list = findViewById(R.id.top_listView);

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
                        if (typeName != null && typeName.equals("top") && exerciseName!=null){
                            itemList.add(exerciseName);
                        }
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Top_part_exercises_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String selectedItem = itemList.get(position);

                // Создайте Intent для открытия новой активности
                Intent intent = new Intent(Top_part_exercises_Activity.this, CaloriesActivity.class);
                intent.putExtra("selectedItem", selectedItem);
                startActivity(intent);
            }
        });


    }
}