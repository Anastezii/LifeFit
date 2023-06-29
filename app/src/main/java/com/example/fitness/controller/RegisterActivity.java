package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fitness.R;
import com.example.fitness.model.ReadWriteUserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText name_registration;
    private Spinner age_registration;
    private RadioGroup radioGroup_registration;
    private RadioButton radioButton_female;
    private RadioButton radioButton_male;
    private EditText height_registration;
    private EditText weight_registration;
    private Spinner goals_registration;
    private EditText email_registration;
    private EditText password_registration;
    private Button registration_button;
    private int calories;
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        name_registration=findViewById(R.id.name_client_registration);
        age_registration=findViewById(R.id.age_client_registration);
        radioGroup_registration=findViewById(R.id.radioGroup_registration);
        radioButton_female=findViewById(R.id.radioButton_female);
        radioButton_male=findViewById(R.id.radioButton_male);
        height_registration=findViewById(R.id.height_registration);
        weight_registration=findViewById(R.id.weight_registration);
        goals_registration=findViewById(R.id.spinner_goals_registration);
        email_registration=findViewById(R.id.email_registration);
        password_registration=findViewById(R.id.password_registartion);
        registration_button=findViewById(R.id.registration_button);


        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name=name_registration.getText().toString();
                String txt_height=height_registration.getText().toString();
                String txt_weight=weight_registration.getText().toString();
                String txt_email=email_registration.getText().toString();
                String txt_password=password_registration.getText().toString();
                String txt_goals=goals_registration.getSelectedItem().toString();
                String txt_age=age_registration.getSelectedItem().toString();


                if(TextUtils.isEmpty(txt_name)){
                    Toast.makeText(RegisterActivity.this,"Please enter your name!",Toast.LENGTH_LONG).show();
                    name_registration.setError("Full name is required!");
                    name_registration.requestFocus();
                }
                else if(radioGroup_registration.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this,"Please input your gender!",Toast.LENGTH_LONG).show();
                    radioButton_female.setError("Gender is required!");
                    radioButton_female.requestFocus();
                }
                else if (TextUtils.isEmpty(txt_height)){
                    Toast.makeText(RegisterActivity.this,"Please input your height!",Toast.LENGTH_LONG).show();
                    height_registration.setError("Height id required!");
                    height_registration.requestFocus();
                }
                else if(TextUtils.isEmpty(txt_weight)){
                    Toast.makeText(RegisterActivity.this,"Please input your weight!",Toast.LENGTH_LONG).show();
                    weight_registration.setError("Weight id required!");
                    weight_registration.requestFocus();
                }
                else if((TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password))) {
                    Toast.makeText(RegisterActivity.this,"Empty credits!",Toast.LENGTH_LONG).show();
                    email_registration.setError("Please input your email!");
                    password_registration.setError("Please choose password!");
                    email_registration.requestFocus();
                    password_registration.requestFocus();
                }
                else if(txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this,"Password too short!",Toast.LENGTH_LONG).show();
                }
                else {
                    if (radioButton_female.isChecked()){
                        calories= (int) (447.6+(9.2*Double.parseDouble(txt_weight))+(3.1*Integer.parseInt(txt_height)-(4.3*Integer.parseInt(txt_age))));
                        String txt_female=radioButton_female.getText().toString();
                        registerUser(txt_name,txt_email,txt_female,txt_age,txt_goals,txt_height,txt_weight,txt_password);
                    }else if (radioButton_male.isChecked()){
                        calories= (int) (88.36+(13.4*Double.parseDouble(txt_weight))+(4.8*Integer.parseInt(txt_height)-(5.7*Integer.parseInt(txt_age))));
                        String txt_female=radioButton_male.getText().toString();
                        registerUser(txt_name,txt_email,txt_female,txt_age,txt_goals,txt_height,txt_weight,txt_password);
                    }
                }
            }
        });
    }

    public void registerUser(String txt_name,String txt_email, String txt_gender, String txt_age, String txt_goals, String txt_height, String txt_weight, String txt_password) {

        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser firebaseUser=auth.getCurrentUser();

                            registrationSuccessful = true;

                            // Update Display Name of User
                            UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(txt_name).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            //Enter User data into the Firebase Realtime Database.
                            ReadWriteUserDetails writeUserDetails =new ReadWriteUserDetails(txt_name,txt_email,Integer.parseInt(txt_age),txt_goals,Integer.parseInt(txt_height),txt_gender,Double.parseDouble(txt_weight),calories);

                            //Extracting user reference from Database for "Registered user"
                            DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered User");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(RegisterActivity.this,"User registered successfully!Please verify your email!",
                                            Toast.LENGTH_LONG).show();

                                    if (task.isSuccessful()){

                                        Toast.makeText(RegisterActivity.this,"User registered successfully!",
                                                Toast.LENGTH_LONG).show();

                                        //Send Verification Email to Client
                                        firebaseUser.sendEmailVerification();

                                        //Open User profile after successful registration
                                        Intent intent=new Intent(RegisterActivity.this,UserProfileActivity.class);
                                        //To prevent user from returning back to Register Activity on pressing back button after registration
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish(); // to close Register Activity

                                    }else{
                                        Toast.makeText(RegisterActivity.this,"User registration is failed!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else {

                            registrationSuccessful = false;

                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                Toast.makeText(RegisterActivity.this,"Your password is too weak!",Toast.LENGTH_LONG).show();
                            }
                            catch (FirebaseAuthInvalidCredentialsException e){
                                Toast.makeText(RegisterActivity.this,"Your email is invalid or already in use!",Toast.LENGTH_LONG).show();
                            }
                            catch (FirebaseAuthUserCollisionException e){
                                Toast.makeText(RegisterActivity.this,"User is already registered with this email!",Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e){
                                Log.e(TAG,e.getMessage());
                                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    public void setAuth(FirebaseAuth authMock) {
        this.auth=authMock;
    }

   private boolean registrationSuccessful = false;

    public boolean isRegistrationSuccessful() {
        return registrationSuccessful;
    }
}