package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class profile extends AppCompatActivity {
    TextView username,fullname,email,phone;
    ImageView profileImage;
    Button logout;
    String userId;

    ChipNavigationBar navigationBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullname = findViewById(R.id.profileFullName);
        username = findViewById(R.id.profileUsername);
        email = findViewById(R.id.profileEmail);
        phone = findViewById(R.id.profilePhone);
        logout = findViewById(R.id.logout);
        profileImage = findViewById(R.id.imgUser);
        navigationBar = findViewById(R.id.navBar);

        firebaseAuth= FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = firebaseAuth.getCurrentUser().getUid();
        if (savedInstanceState==null){
            navigationBar.setItemSelected(R.id.homeSelected,true);
        }

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.homeSelected:
                        navigationBar.setItemSelected(R.id.homeSelected,true);
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        break;
                    case R.id.CanteenSelected:
                        navigationBar.setItemSelected(R.id.CanteenSelected,true);
                        startActivity(new Intent(getApplicationContext(), dashboard.class));
                        break;
                    case R.id.aboutSelected:
                        navigationBar.setItemSelected(R.id.aboutSelected,true);
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        break;

                }
            }
        });

        DocumentReference reference = fstore.collection("users").document(userId);
        reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                fullname.setText(value.getString("fName"));
                username.setText(value.getString("fName"));
                email.setText(value.getString("email"));
                phone.setText(value.getString("phone"));
            }

        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);

                profileImageUpload(imageUri);

            }
        }

    }
    private void profileImageUpload(Uri imageUri) {

        StorageReference fileRef = storageReference.child("profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(profile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this, "Upload Failed !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


}