package com.example.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText email_login;
    private EditText password_login;
    private Button login_button;
    private Button forgot_password;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        email_login=findViewById(R.id.email_login);
        password_login=findViewById(R.id.password_login);
        login_button=findViewById(R.id.login_button);
        forgot_password=findViewById(R.id.button_forgot_password);

        firebaseAuth =FirebaseAuth.getInstance();

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"You can reset your password now!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email_login.getText().toString();
                String txt_password=password_login.getText().toString();

                if (TextUtils.isEmpty(txt_email)){
                    Toast.makeText(LoginActivity.this,"Please input your email!",Toast.LENGTH_LONG).show();
                    email_login.setError("Please input your email!");
                    email_login.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
                    Toast.makeText(LoginActivity.this,"Please re-enter your email!",Toast.LENGTH_LONG).show();
                    email_login.setError("Valid email is required!");
                    email_login.requestFocus();
                }else if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this,"Please input your password!",Toast.LENGTH_LONG).show();
                    password_login.setError("Please input your password!");
                    password_login.requestFocus();
                }
                else{
                    loginUser(txt_email,txt_password);
                }

            }
        });
    }

    private void loginUser(String txt_email, String txt_password) {

        firebaseAuth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"User is logged successfully!",Toast.LENGTH_LONG).show();

                    // Get instance of the current User
                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                    //Check if email is verified before user can access their profile
                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this,"You are logged in now!",Toast.LENGTH_SHORT).show();

                        //Open User profile after successful registration
                        Intent intent=new Intent(LoginActivity.this,UserProfileActivity.class);
                        //To prevent user from returning back to Register Activity on pressing back button after registration
                        startActivity(intent);
                        finish(); // to close Login Activity

                    }else {

                        firebaseUser.sendEmailVerification();
                        firebaseAuth.signOut(); //Sign out user
                        showAlertDialog();

                    }

                }
                else{
                    Toast.makeText(LoginActivity.this,"User`s logging is failed!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void showAlertDialog() {

        //Setup the Alert Builder
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification!");

        //Open email apps if User clicks/taps continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // to email app in new window and not within our app
                startActivity(intent);

            }
        });

        // Create alert dialog
        AlertDialog alertDialog=builder.create();

        //show alert dialog
        alertDialog.show();

    }

    // check if user is already logged in. in such case --> user`s profile
    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            Toast.makeText(LoginActivity.this,"You are already logged in!",Toast.LENGTH_SHORT).show();

            //start the userProfileActivity
            startActivity(new Intent(LoginActivity.this,UserProfileActivity.class));
            finish();
        }else{
            Toast.makeText(LoginActivity.this,"You can logged in now!",Toast.LENGTH_SHORT).show();
        }
    }
}