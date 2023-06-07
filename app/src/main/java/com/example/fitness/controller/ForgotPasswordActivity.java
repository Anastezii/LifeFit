package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button button_reset_password;
    private EditText reset_email;
    private FirebaseAuth auth;
    private final static String TAG="Forgot Password Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setTitle("Forgot Password");

        reset_email=findViewById(R.id.email_resety_password);
        button_reset_password=findViewById(R.id.button_reset_password);

        button_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_email=reset_email.getText().toString();

                if (TextUtils.isEmpty(txt_email)){
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter your registered email!",Toast.LENGTH_LONG).show();
                    reset_email.setError("Valid email is required");
                    reset_email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter valid email!",Toast.LENGTH_LONG).show();
                    reset_email.setError("Valid email is required");
                    reset_email.requestFocus();
                }else{
                    resetPassword(txt_email);
                }

            }
        });

    }

    private void resetPassword(String txt_email) {

        auth=FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(ForgotPasswordActivity.this,"Please check your inbox for password reset link",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ForgotPasswordActivity.this,MainActivity.class);

                    //clear stack to prevent user coming back to ForgotPasswordActivity on pressing back button after Logging Out
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // close UserProfileActivity
                }else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e ){
                        reset_email.setError("User does not exist or is no longer valid.Please register again.");
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(ForgotPasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }
}