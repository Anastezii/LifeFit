package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.fitness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText update_name,update_height,update_weight,update_age,update_goals,calories;
    private RadioGroup update_gender;
    private RadioButton update_gender_st;
    String txt_name,txt_gender,txt_goal;
    int txt_age,txt_height,txt_calories;
    Double txt_weight;
    private Button update_profile,upload_pic,update_email;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setTitle("Update Profile");

        update_name=findViewById(R.id.update_name);
        update_height=findViewById(R.id.update_height);
        update_weight=findViewById(R.id.update_weight);
        update_age=findViewById(R.id.update_age);
        update_goals=findViewById(R.id.update_goal);
        calories=findViewById(R.id.calories_update);

        update_gender=findViewById(R.id.update_gender);

        update_profile=findViewById(R.id.button_update_profile);
        upload_pic=findViewById(R.id.button_upload_pic);
        update_email=findViewById(R.id.button_update_email);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        //Show profile data
        showProfile(firebaseUser);

        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateProfileActivity.this,UploadProfilePicActivity.class);
                startActivity(intent);
                finish();
            }
        });

       update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateProfileActivity.this,UpdateEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileFunction(firebaseUser);
            }
        });

    }

    private void updateProfileFunction(FirebaseUser firebaseUser) {

        int selectedGenderId=update_gender.getCheckedRadioButtonId();
        update_gender_st=findViewById(selectedGenderId);

        if(TextUtils.isEmpty(txt_name)){
            Toast.makeText(UpdateProfileActivity.this,"Please enter your name!",Toast.LENGTH_LONG).show();
            update_name.setError("Full name is required!");
            update_name.requestFocus();
        }
        else if(update_gender.getCheckedRadioButtonId() == -1){
            Toast.makeText(UpdateProfileActivity.this,"Please input your gender!",Toast.LENGTH_LONG).show();
            update_gender_st.setError("Gender is required!");
            update_gender_st.requestFocus();
        }
        else if (TextUtils.isEmpty(Integer.toString(txt_height))){
            Toast.makeText(UpdateProfileActivity.this,"Please input your height!",Toast.LENGTH_LONG).show();
            update_height.setError("Height id required!");
            update_height.requestFocus();
        }
        else if (TextUtils.isEmpty(txt_goal)){
            Toast.makeText(UpdateProfileActivity.this,"Please input your goal!",Toast.LENGTH_LONG).show();
            update_goals.setError("Goal required!");
            update_goals.requestFocus();
        }
        else if(TextUtils.isEmpty(Double.toString(txt_weight))){
            Toast.makeText(UpdateProfileActivity.this,"Please input your weight!",Toast.LENGTH_LONG).show();
            update_weight.setError("Weight id required!");
            update_weight.requestFocus();
        }else{
            //obtain data entered by user
            txt_gender=update_gender_st.getText().toString();
            txt_name=update_name.getText().toString();
            txt_weight= Double.valueOf(update_weight.getText().toString());
            txt_age= Integer.parseInt(update_age.getText().toString());
            txt_height=Integer.parseInt(update_height.getText().toString());
            txt_goal=update_goals.getText().toString();
            txt_calories= Integer.parseInt(calories.getText().toString());

            //enter data into firebase db.set up dependencies
            ReadWriteUserDetails readWriteUserDetails=new ReadWriteUserDetails(txt_name,txt_gender,txt_age,txt_height,txt_weight,txt_goal,txt_calories);

            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Registered User");

            String userID=firebaseUser.getUid();

            reference.child(userID).setValue(readWriteUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        //setting new display name
                        UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().
                                setDisplayName(txt_name).build();
                        firebaseUser.updateProfile(profileChangeRequest);

                        Toast.makeText(UpdateProfileActivity.this,"Update Successful!",Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(UpdateProfileActivity.this,UserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(UpdateProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });

        }


    }

    //fetch data from firebase and display
    private void showProfile(FirebaseUser firebaseUser) {

        String userIdOfRegistered=firebaseUser.getUid();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Registered User");

        reference.child(userIdOfRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails !=null){
                    txt_name=firebaseUser.getDisplayName();
                    txt_weight=readWriteUserDetails.weight;
                    txt_height=readWriteUserDetails.height;
                    txt_age=readWriteUserDetails.age;
                    txt_gender=readWriteUserDetails.gender;
                    txt_goal=readWriteUserDetails.goals;
                    txt_calories=readWriteUserDetails.calories;

                    update_name.setText(txt_name);
                    update_age.setText(Integer.toString(txt_age));
                    update_height.setText(Integer.toString(txt_height));
                    update_weight.setText(Double.toString(txt_weight));
                    update_goals.setText(txt_goal);
                    calories.setText(Integer.toString(txt_calories));

                    if (txt_gender.equals("Male")){
                        update_gender_st=findViewById(R.id.update_male);
                    }else {
                        update_gender_st=findViewById(R.id.update_female);
                    }
                    update_gender_st.setChecked(true);
                }else {
                    Toast.makeText(UpdateProfileActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
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
            Intent intent=new Intent(UpdateProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_update_email){
            Intent intent=new Intent(UpdateProfileActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_change_password){
            Intent intent=new Intent(UpdateProfileActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_delete_profile){
            Intent intent=new Intent(UpdateProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_logout){
            firebaseAuth.signOut();
            Toast.makeText(UpdateProfileActivity.this,"Logged Out",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(UpdateProfileActivity.this,MainActivity.class);

            //clear stack to prevent user coming back to UserProfileActivity on pressing back button after Logging Out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // close UserProfileActivity
        }else {
            Toast.makeText(UpdateProfileActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

}