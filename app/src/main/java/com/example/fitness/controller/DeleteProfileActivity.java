package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteProfileActivity extends AppCompatActivity {

    private EditText password;
    private Button delete_authentication,delete_profile;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private String user_pwd;
    private static final String TAG="Delete ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        getSupportActionBar().setTitle("Delete Profile");

        password=findViewById(R.id.delete_password);
        delete_authentication=findViewById(R.id.button_delete_authentication);
        delete_profile=findViewById(R.id.button_delete_profile);

        delete_profile.setEnabled(false);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(DeleteProfileActivity.this,"Something went wrong.User`s details not corrected!",Toast.LENGTH_LONG).show();
        }else{
            reAuthenticate(firebaseUser);
        }

    }

    private void reAuthenticate(FirebaseUser firebaseUser) {

        delete_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_pwd=password.getText().toString();

                if (TextUtils.isEmpty(user_pwd))
                {
                    Toast.makeText(DeleteProfileActivity.this,"Password is needed to continue!",Toast.LENGTH_LONG).show();
                    password.setError("Please enter yor password for authentication!");
                    password.requestFocus();
                }else {
                    AuthCredential credential= EmailAuthProvider.getCredential(firebaseUser.getEmail(),user_pwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                password.setEnabled(false);
                                delete_authentication.setEnabled(false);

                                delete_profile.setEnabled(true);

                                Toast.makeText(DeleteProfileActivity.this,"Profile was verified"+"Now you can delete your profile",Toast.LENGTH_LONG).show();

                                delete_profile.setBackgroundTintList(ContextCompat.getColorStateList(DeleteProfileActivity.this,R.color.teal_200));

                                delete_profile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        deleteProfile(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    private void deleteProfile(FirebaseUser firebaseUser) {

        AlertDialog.Builder builder=new AlertDialog.Builder(DeleteProfileActivity.this);
        builder.setTitle("Delete User and Related Data ?");
        builder.setMessage("Do you really want to delete your profile and related data? This action is irreversible!");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(firebaseUser);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(DeleteProfileActivity.this,UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    private void delete(FirebaseUser firebaseUser) {

        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    deleteUserData();
                    firebaseAuth.signOut();
                    Toast.makeText(DeleteProfileActivity.this,"User has been deleted!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(DeleteProfileActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    private void deleteUserData() {

        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReference(firebaseUser.getPhotoUrl().toString());
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"OnSuccess:Photo Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.getMessage());
                Toast.makeText(DeleteProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        //Delete data from realtime db
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Registered User");
        databaseReference.child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"OnSuccess:Data was Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.getMessage());
                Toast.makeText(DeleteProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
            Intent intent=new Intent(DeleteProfileActivity
                    .this,UpdateProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_update_email){
            Intent intent=new Intent(DeleteProfileActivity
                    .this,UpdateEmailActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_change_password){
            Intent intent=new Intent(DeleteProfileActivity
                    .this,ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_delete_profile){
            Intent intent=new Intent(DeleteProfileActivity
                    .this,DeleteProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_logout){
            firebaseAuth.signOut();
            Toast.makeText(DeleteProfileActivity
                    .this,"Logged Out",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(DeleteProfileActivity
                    .this,MainActivity.class);

            //clear stack to prevent user coming back to DeleteProfileActivity
            // on pressing back button after Logging Out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // close DeleteProfileActivity

        }else {
            Toast.makeText(DeleteProfileActivity
                    .this,"Something went wrong",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


}