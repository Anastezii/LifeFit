package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fitness.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Bottom_part_exercises_Activity extends AppCompatActivity {

    private ListView list;
    private CheckBox checkBoxBeginner,checkBoxIntermediate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_part_exercises);

        list = findViewById(R.id.bottom_listView);
        checkBoxBeginner=findViewById(R.id.checkBox_Beginner_bottom);
        checkBoxIntermediate=findViewById(R.id.checkBox_Intermidiate_bottom);

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
                        if (typeName != null && typeName.equals("bottom") && exerciseName!=null){
                            itemList.add(exerciseName);
                        }
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Bottom_part_exercises_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);

                // Создайте Intent для открытия новой активности
                if (selectedItem.equals("Calf Raises") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, CalfRaisesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if (selectedItem.equals("Deadlifts") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, DeadliftsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Leg Press") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, LegPressActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Lunges") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, LungesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Squats") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, SquatsActivity.class);
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
                        if (typeName != null && typeName.equals("bottom") && exerciseName!=null && difficulty != null && difficulty.equals("Beginner")){
                            itemList.add(exerciseName);
                        }
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Bottom_part_exercises_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);

                if (selectedItem.equals("Calf Raises") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, CalfRaisesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if (selectedItem.equals("Deadlifts") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, DeadliftsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Leg Press") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, LegPressActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Lunges") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, LungesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Squats") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, SquatsActivity.class);
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
                        if (typeName != null && typeName.equals("bottom") && exerciseName!=null && difficulty != null && difficulty.equals("Intermediate")){
                            itemList.add(exerciseName);
                        }
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Bottom_part_exercises_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);

                if (selectedItem.equals("Calf Raises") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, CalfRaisesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if (selectedItem.equals("Deadlifts") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, DeadliftsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Leg Press") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, LegPressActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Lunges") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, LungesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Squats") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, SquatsActivity.class);
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
                        if (typeName != null && typeName.equals("bottom") && exerciseName!=null){
                            itemList.add(exerciseName);
                        }
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Bottom_part_exercises_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);

                if (selectedItem.equals("Calf Raises") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, CalfRaisesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if (selectedItem.equals("Deadlifts") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, DeadliftsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Leg Press") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, LegPressActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Lunges") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, LungesActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }else if(selectedItem.equals("Squats") && selectedItem!=null){
                    Intent intent = new Intent(Bottom_part_exercises_Activity.this, SquatsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);
                    startActivity(intent);
                }
            }
        });


    }
}