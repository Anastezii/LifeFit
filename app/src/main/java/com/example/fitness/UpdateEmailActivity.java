package com.example.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmailActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView txt_auth;
    private String userOldEmail,userNewEmail,userPwd;
    private Button button_update,button_authenticate;
    private EditText email,pwd,new_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        getSupportActionBar().setTitle("Update Email");

        email=findViewById(R.id.update_email_address);
        pwd=findViewById(R.id.update_email_password);
        txt_auth=findViewById(R.id.textView22);
        new_email=findViewById(R.id.update_new_email);

        button_update=findViewById(R.id.button_new_email);

        button_update.setEnabled(false); //make button disabled in the beginning until the user is authenticated
        new_email.setEnabled(false);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();;

        //set old email ID on TextView
        userOldEmail=firebaseUser.getEmail();
        TextView txt_old_email=findViewById(R.id.textView20);
        txt_old_email.setText(userOldEmail);

        if (firebaseUser.equals("")){
            Toast.makeText(UpdateEmailActivity.this,"Something went wrong.User`s details nit corrected!",Toast.LENGTH_LONG).show();
        }else{
            reAuthenticate(firebaseUser);
        }

    }

    private void reAuthenticate(FirebaseUser firebaseUser) {

        button_authenticate=findViewById(R.id.button_authenticate);
        button_authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtain password for authentication
                userPwd=pwd.getText().toString();

                if (TextUtils.isEmpty(userPwd)){
                    Toast.makeText(UpdateEmailActivity.this,"Password is needed to continue!",Toast.LENGTH_LONG).show();
                    pwd.setError("Please enter yor password for authentication!");
                    pwd.requestFocus();
                }else{
                    AuthCredential credential= EmailAuthProvider.getCredential(userOldEmail,userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdateEmailActivity.this, "Password has been verified." + "You can update yor email now.", Toast.LENGTH_LONG).show();

                                new_email.setEnabled(true);
                                button_update.setEnabled(true);
                                button_authenticate.setEnabled(false);
                                email.setEnabled(false);
                                pwd.setEnabled(false);

                                button_update.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this, R.color.teal_200));

                                button_update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userNewEmail = new_email.getText().toString();
                                        if (TextUtils.isEmpty(userNewEmail)) {
                                            Toast.makeText(UpdateEmailActivity.this, "New email is needed to continue!", Toast.LENGTH_LONG).show();
                                            new_email.setError("Please enter your new email !");
                                            new_email.requestFocus();
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()) {
                                            Toast.makeText(UpdateEmailActivity.this, "New email is needed to be different from old!", Toast.LENGTH_LONG).show();
                                            new_email.setError("Please enter your NEW email !");
                                            new_email.requestFocus();
                                        } else {
                                            updateEmail(firebaseUser);
                                        }
                                    }
                                });
                            }else{
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    private void updateEmail(FirebaseUser firebaseUser) {

        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){


                    //verify email
                    firebaseUser.sendEmailVerification();

                    Toast.makeText(UpdateEmailActivity.this,"Email has been updated.Please verify your email.", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(UpdateEmailActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
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
            Intent intent=new Intent(UpdateEmailActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_update_email){
            Intent intent=new Intent(UpdateEmailActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_change_password){
            Intent intent=new Intent(UpdateEmailActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_delete_profile){
            Intent intent=new Intent(UpdateEmailActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_logout){
            firebaseAuth.signOut();
            Toast.makeText(UpdateEmailActivity.this,"Logged Out",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(UpdateEmailActivity.this,MainActivity.class);

            //clear stack to prevent user coming back to UserProfileActivity on pressing back button after Logging Out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // close UserProfileActivity
        }else {
            Toast.makeText(UpdateEmailActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}