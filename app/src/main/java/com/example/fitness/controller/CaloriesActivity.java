package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.example.fitness.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class CaloriesActivity extends AppCompatActivity implements SensorEventListener {

    private Button food,sport,add_steps;
    private TextView txt_calories;
    DatabaseReference reference;
    DatabaseReference referenceSport;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private TextView progressText;

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        progressBar = findViewById(R.id.progressBar);
        progressText=findViewById(R.id.progress_text);
        add_steps=findViewById(R.id.buttonSteps);

        food=findViewById(R.id.buttonBuyFood);
        sport=findViewById(R.id.buttonBuySport);
        txt_calories=findViewById(R.id.show_calories);

        count = (TextView) findViewById(R.id.steps);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        reference= FirebaseDatabase.getInstance().getReference("Nutrition_data");
        referenceSport=FirebaseDatabase.getInstance().getReference("Sport_data");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Double calories_total = 0.0 ;

                for (DataSnapshot category: snapshot.getChildren()){
                    for( DataSnapshot entry : category.getChildren()){

                        String date = entry.child("date").getValue(String.class);

                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = dateFormat.format(currentDate);

                        if (dateString.equals(date)){

                            Double caloriesDouble = entry.child("calories").getValue(Double.class);
                            if (caloriesDouble != null) {
                                calories_total += caloriesDouble;
                            }

                        }

                    }
                }

                retrieveSportData(calories_total);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CaloriesActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

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
                        startActivity(new Intent(getApplicationContext(), FoodActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return;
                    case R.id.bottom_exercises:
                        startActivity(new Intent(getApplicationContext(), ExercisesActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    case R.id.bottom_calories:
                        return;
                }
                return;
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CaloriesActivity.this, BuyFoodActivity.class);
                startActivity(intent);
            }
        });

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CaloriesActivity.this, BuySportActivity.class);
                startActivity(intent);
            }
        });



    }

    private void retrieveSportData(final Double calories_total) {

        referenceSport.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double subtractedCalories = 0;

                for (DataSnapshot entrySnapshot : snapshot.getChildren()) {

                    String date = entrySnapshot.child("date").getValue(String.class);

                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String dateString = dateFormat.format(currentDate);

                    if (date.equals(dateString)){

                        Double caloriesDouble = entrySnapshot.child("calories").getValue(Double.class);
                        if (caloriesDouble != null) {
                            subtractedCalories += caloriesDouble;
                        }
                    }

                }

                double remainingCalories = calories_total - subtractedCalories;

                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                if (firebaseUser==null){
                    Toast.makeText(CaloriesActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
                }else {
                    DatabaseReference referenceUser= FirebaseDatabase.getInstance().getReference("Registered User");
                    referenceUser.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            HashMap<String, Object> food = (HashMap<String, Object>) snapshot.getValue();

                            Double total_calories = Double.parseDouble(food.get("calories").toString());

                            txt_calories.setText("Calories " + remainingCalories + "/" + total_calories);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int value = (int) Double.parseDouble(String.valueOf(remainingCalories));
                                            int value_2 = (int) Double.parseDouble(String.valueOf(total_calories));


                                            int progress = (value * 100) / value_2;
                                            progressText.setText("" + value);
                                            progressBar.setProgress(progress);
                                        }
                                    });

                                    try {
                                        Thread.sleep(100);
                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                }
                            }).start();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(CaloriesActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CaloriesActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    // Creating ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else {
            Toast.makeText(CaloriesActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
            count.setText("Your device does not support Sensor, so you can enter the data yourself. To do this, press the Write data button bellow");
            add_steps.setVisibility(View.VISIBLE);
            add_steps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showdialog();
                }
            });
        }
    }

    private void showdialog() {

        final Dialog dialog=new Dialog(CaloriesActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_steps);

        final EditText count_steps=dialog.findViewById(R.id.count_steps);
        Button writeButton=dialog.findViewById(R.id.write_count);

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String steps_number=count_steps.getText().toString();
                count.setText("Your count of steps is : "+steps_number + "/"+"10 000");
                dialog.dismiss();

            }
        });

        dialog.show();
    }



    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            count.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}