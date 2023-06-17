package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness.R;
import com.example.fitness.model.ReadWriteUserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private TextView txt_welcome,txt_name,txt_email,txt_age,txt_goals,txt_height,txt_gender,txt_weight,txt_calories;
    private String name,email,goals,gender;
    private Integer age,height,calories;
    private double weight;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_home:
                         return;
                    case R.id.bottom_activity:
                        startActivity(new Intent(getApplicationContext(),SportActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                        return;
                    case R.id.bottom_eat:
                        startActivity(new Intent(getApplicationContext(),FoodActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                       return;
                    case R.id.bottom_calories:
                        startActivity(new Intent(getApplicationContext(),CaloriesActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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

        getSupportActionBar().setTitle("Home");

        txt_welcome=findViewById(R.id.textView_show_welcome);
        txt_name=findViewById(R.id.textView_show_full_name);
        txt_email=findViewById(R.id.textView_show_email);
        txt_age=findViewById(R.id.textView_show_age);
        txt_goals=findViewById(R.id.textView_show_goal);
        txt_height=findViewById(R.id.textView_show_height);
        txt_gender=findViewById(R.id.textView_show_gender);
        txt_weight=findViewById(R.id.textView_show_weight);
        txt_calories=findViewById(R.id.textViewCalories);

        //onclick on ImageView to open uploadProfilePicActivity
        imageView=findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfileActivity.this,UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if (firebaseUser==null){
            Toast.makeText(UserProfileActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
        }else {
            showUserProfile(firebaseUser);
        }

    }

    private void showUserProfile(FirebaseUser firebaseUser) {

        String userID=firebaseUser.getUid();

        //extracting user reference fro db for registered user
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Registered User");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails!=null){
                    name=firebaseUser.getDisplayName();
                    email=firebaseUser.getEmail();
                    age=readWriteUserDetails.age;
                    goals=readWriteUserDetails.goals;
                    height=readWriteUserDetails.height;
                    gender=readWriteUserDetails.gender;
                    weight=readWriteUserDetails.weight;
                    calories=readWriteUserDetails.calories;

                    txt_welcome.setText("Welcome, "+name+"!");
                    txt_name.setText(name);
                    txt_email.setText(email);
                    txt_age.setText(Integer.toString(age));
                    txt_goals.setText(goals);
                    txt_height.setText(Integer.toString(height));
                    txt_gender.setText(gender);
                    txt_weight.setText(Double.toString(weight));
                    txt_calories.setText(Integer.toString(calories));

                    //set user dp(after user has uploaded)
                    Uri uri=firebaseUser.getPhotoUrl();

                    //ImageViewer setImageURI() should not be used with regular URIs. So we are using Picasso
                    Picasso.with(UserProfileActivity.this).load(uri).into(imageView);



                }else{

                    Toast.makeText(UserProfileActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
    }


    // Creating ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id==R.id.menu_update_profile){
            Intent intent=new Intent(UserProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_update_email){
            Intent intent=new Intent(UserProfileActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_change_password){
            Intent intent=new Intent(UserProfileActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_delete_profile){
            Intent intent=new Intent(UserProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_logout){
            firebaseAuth.signOut();
            Toast.makeText(UserProfileActivity.this,"Logged Out",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(UserProfileActivity.this,MainActivity.class);

            //clear stack to prevent user coming back to UserProfileActivity on pressing back button after Logging Out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // close UserProfileActivity
        }else {
            Toast.makeText(UserProfileActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


}