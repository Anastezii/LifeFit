package com.example.fitness.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fitness.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadProfilePicActivity extends AppCompatActivity {

    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private Button upload;
    private Button update;
    private StorageReference storageReference;
    private FirebaseUser  firebaseUser;
    private static final int PICK_IMAGE_REQUEST=1;
    private  Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);

        getSupportActionBar().setTitle("Upload Profile Picture");

        upload=findViewById(R.id.button_add_pic);
        update=findViewById(R.id.button_upload);
        imageView=findViewById(R.id.imageView2);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        storageReference= FirebaseStorage.getInstance().getReference("DisplayPics");

        Uri uri =firebaseUser.getPhotoUrl();

        //set user`s current dp in imageview(if uploaded already. We will picasso imageViewer setImage
        //regular URIs
        Picasso.with(UploadProfilePicActivity.this).load(uri).into(imageView);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadPic();

            }
        });

    }

    private void UploadPic() {

        if (uriImage!=null){

            //save the image with uid of the currently logged user
            StorageReference fileReference=storageReference.child(firebaseAuth.getCurrentUser().getUid()+"."
            + getFileExtension(uriImage));

            //upload image to storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Uri downloadUri=uri;
                            firebaseUser=firebaseAuth.getCurrentUser();

                            //set the display image of the user after upload
                            UserProfileChangeRequest profileChangeRequest =new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadProfilePicActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(UploadProfilePicActivity.this,"Upload Successful!",Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(UploadProfilePicActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }

    // obtain file extension of the image
    private String getFileExtension(Uri uriImage) {

        ContentResolver cR =getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uriImage));
    }

    private void openFileChooser() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            uriImage=data.getData();
            imageView.setImageURI(uriImage);

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
            Intent intent=new Intent(UploadProfilePicActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_update_email){
            Intent intent=new Intent(UploadProfilePicActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_change_password){
            Intent intent=new Intent(UploadProfilePicActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_delete_profile){
            Intent intent=new Intent(UploadProfilePicActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
        }else if (id==R.id.menu_logout){
            firebaseAuth.signOut();
            Toast.makeText(UploadProfilePicActivity.this,"Logged Out",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(UploadProfilePicActivity.this,MainActivity.class);

            //clear stack to prevent user coming back to UserProfileActivity on pressing back button after Logging Out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // close UserProfileActivity
        }else {
            Toast.makeText(UploadProfilePicActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

}