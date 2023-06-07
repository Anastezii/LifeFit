package com.example.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText old_password,new_password,confirmed_password;
    private Button authenticate,change_pwd;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String user_pwd_cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Change Password");

        old_password=findViewById(R.id.update_old_password);
        new_password=findViewById(R.id.new_password);
        confirmed_password=findViewById(R.id.confirm_new_password);

        authenticate=findViewById(R.id.button_authenticate_password);
        change_pwd=findViewById(R.id.button_change_pwd);

        new_password.setEnabled(false);
        confirmed_password.setEnabled(false);
        change_pwd.setEnabled(false);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(ChangePasswordActivity.this,"Something went wrong.User`s details not corrected!",Toast.LENGTH_LONG).show();
        }else{
            reAuthenticate(firebaseUser);
        }

    }

    private void reAuthenticate(FirebaseUser firebaseUser) {

        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_pwd_cur=old_password.getText().toString();

                if (TextUtils.isEmpty(user_pwd_cur))
                {
                    Toast.makeText(ChangePasswordActivity.this,"Password is needed to continue!",Toast.LENGTH_LONG).show();
                    old_password.setError("Please enter yor password for authentication!");
                    old_password.requestFocus();
                }else {
                    AuthCredential credential= EmailAuthProvider.getCredential(firebaseUser.getEmail(),user_pwd_cur);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                old_password.setEnabled(false);
                                authenticate.setEnabled(false);

                                new_password.setEnabled(true);
                                confirmed_password.setEnabled(true);
                                change_pwd.setEnabled(true);

                                Toast.makeText(ChangePasswordActivity.this,"Password was verified"+"Now you can change your password",Toast.LENGTH_LONG).show();

                                change_pwd.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this,R.color.teal_200));

                                change_pwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    private void changePwd(FirebaseUser firebaseUser) {

        String new_pwd=new_password.getText().toString();
        String confirm_pwd=confirmed_password.getText().toString();

        if (TextUtils.isEmpty(new_pwd)){
            Toast.makeText(ChangePasswordActivity.this,"Please enter your new password",Toast.LENGTH_LONG).show();
            new_password.setError("Please enter your new password!");
            new_password.requestFocus();
        }else if (TextUtils.isEmpty(confirm_pwd)){
            Toast.makeText(ChangePasswordActivity.this,"Please confirm your new password",Toast.LENGTH_LONG).show();
            confirmed_password.setError("Please confirm your new password!");
            confirmed_password.requestFocus();
        }else if (new_pwd.matches(confirm_pwd)){
            Toast.makeText(ChangePasswordActivity.this,"Password did not match",Toast.LENGTH_LONG).show();
            confirmed_password.setError("Please re-enter same password!");
            confirmed_password.requestFocus();
        }else if (!user_pwd_cur.matches(new_pwd)){
            Toast.makeText(ChangePasswordActivity.this,"Please enter your NEW password , different from old",Toast.LENGTH_LONG).show();
            new_password.setError("Please enter your NEW password!");
            new_password.requestFocus();
        }else{

            firebaseUser.updatePassword(new_pwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChangePasswordActivity.this,"Password has been changed ",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
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
            Intent intent=new Intent(ChangePasswordActivity
                    .this,UpdateProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_update_email){
            Intent intent=new Intent(ChangePasswordActivity
                    .this,ChangePasswordActivity
                    .class);
            startActivity(intent);
        }else if (id==R.id.menu_change_password){
            Intent intent=new Intent(ChangePasswordActivity
                    .this,ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_delete_profile){
            Intent intent=new Intent(ChangePasswordActivity
            .this,DeleteProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_logout){
            firebaseAuth.signOut();
            Toast.makeText(ChangePasswordActivity
                    .this,"Logged Out",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ChangePasswordActivity
                    .this,MainActivity.class);

            //clear stack to prevent user coming back to UserProfileActivity on pressing back button after Logging Out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // close UserProfileActivity
        }else {
            Toast.makeText(ChangePasswordActivity
                    .this,"Something went wrong",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}