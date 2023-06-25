package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import okhttp3.internal.http2.Huffman;

public class Top_part_exercises_Activity extends AppCompatActivity {

    private ListView list;
    private CheckBox checkBoxBeginner,checkBoxIntermediate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_part_exercises);

        list = findViewById(R.id.top_listView);

        checkBoxBeginner=findViewById(R.id.checkBox_Beginner);
        checkBoxIntermediate=findViewById(R.id.checkBox_Intermidiate);

        ArrayList<String> itemList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        list.setAdapter(adapter);

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
                if (selectedItem.equals("Bicep Curls") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, BicepCurlsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if (selectedItem.equals("Hammer Curls") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, HammerCurlsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Push-Ups") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, Push_UpsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Tricep Dips") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, TricepDipsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Tricep Kickbacks") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, TricepKickbacksActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }

            }
        });

        checkBoxBeginner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The checkbox is checked, update the ListView with Beginner exercises
                    updateListViewWithBeginnerExercises();
                } else {
                    // The checkbox is unchecked, update the ListView with all exercises
                    updateListViewWithAllExercises();
                }
            }
        });

        checkBoxIntermediate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The checkbox is checked, update the ListView with Intermediate exercises
                    updateListViewWithIntermediateExercises();
                } else {
                    // The checkbox is unchecked, update the ListView with all exercises
                    updateListViewWithAllExercises();
                }
            }
        });

    }
    private void updateListViewWithBeginnerExercises() {
        ArrayList<String> itemList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        list.setAdapter(adapter);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Exercises");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                itemList.clear();

                for (DataSnapshot muscleGroupSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot exerciseSnapshot : muscleGroupSnapshot.getChildren()) {
                        String exerciseName = exerciseSnapshot.child("name").getValue(String.class);
                        String typeName = exerciseSnapshot.child("type").getValue(String.class);
                        String difficulty = exerciseSnapshot.child("difficulty").getValue(String.class);
                        if (typeName != null && typeName.equals("top") && exerciseName!=null && difficulty != null && difficulty.equals("Beginner")){
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
                if (selectedItem.equals("Bicep Curls") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, BicepCurlsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if (selectedItem.equals("Hammer Curls") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, HammerCurlsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Push-Ups") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, Push_UpsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Tricep Dips") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, TricepDipsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Tricep Kickbacks") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, TricepKickbacksActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }


            }
        });


    }

    private void updateListViewWithIntermediateExercises() {
        ArrayList<String> itemList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        list.setAdapter(adapter);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Exercises");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                itemList.clear();

                for (DataSnapshot muscleGroupSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot exerciseSnapshot : muscleGroupSnapshot.getChildren()) {
                        String exerciseName = exerciseSnapshot.child("name").getValue(String.class);
                        String typeName = exerciseSnapshot.child("type").getValue(String.class);
                        String difficulty = exerciseSnapshot.child("difficulty").getValue(String.class);
                        if (typeName != null && typeName.equals("top") && exerciseName!=null && difficulty != null && difficulty.equals("Intermediate")){
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
                if (selectedItem.equals("Bicep Curls") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, BicepCurlsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if (selectedItem.equals("Hammer Curls") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, HammerCurlsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Push-Ups") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, Push_UpsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Tricep Dips") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, TricepDipsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Tricep Kickbacks") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, TricepKickbacksActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }

            }
        });

    }

    private void updateListViewWithAllExercises() {

        ArrayList<String> itemList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        list.setAdapter(adapter);

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
                if (selectedItem.equals("Bicep Curls") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, BicepCurlsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if (selectedItem.equals("Hammer Curls") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, HammerCurlsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Push-Ups") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, Push_UpsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Tricep Dips") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, TricepDipsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Tricep Kickbacks") && selectedItem!=null){
                    Intent intent = new Intent(Top_part_exercises_Activity.this, TricepKickbacksActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }


            }
        });


    }
}