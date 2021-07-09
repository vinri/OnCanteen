package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class profile extends AppCompatActivity {
    private TextView username,fullname,email,phone;
    private ImageView profileImage;
    private Button logout, manageMenuBtn, editUserbtn;
    private FloatingActionButton editImageBtn;
    private CircleImageView circleImageView;
    private String userId, myUri="";
    private Uri imageUri;

    private ChipNavigationBar navigationBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullname = findViewById(R.id.profileFullName);
        username = findViewById(R.id.profileUsername);
        email = findViewById(R.id.profileEmail);
        phone = findViewById(R.id.profilePhone);
        logout = findViewById(R.id.logout);
        profileImage = findViewById(R.id.menuImage);
        navigationBar = findViewById(R.id.navBar);
        editImageBtn = findViewById(R.id.changeImageBtn);
        editUserbtn = findViewById(R.id.editUserBtn);

        firebaseAuth= FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("user/profilePicture");
        StorageReference storageRef = storageReference.child(userId+".jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

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
                        finish();
                        break;
                    case R.id.CanteenSelected:
                        navigationBar.setItemSelected(R.id.CanteenSelected,true);
                        startActivity(new Intent(getApplicationContext(), dashboard.class));
                        finish();
                        break;
                    case R.id.aboutSelected:
                        navigationBar.setItemSelected(R.id.aboutSelected,true);
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        finish();
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
        editUserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editUser = new Intent(getApplicationContext(), editUser.class);
                startActivity(editUser);
            }
        });

        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(profile.this);
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

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            //profileImage.setImageURI(imageUri);
            uploadImage();

        } else {
            Toast.makeText(this, "Error, Try again ", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("set your profile");
        progressDialog.setMessage("Please Wait, While we are setting your data ");
        progressDialog.show();

        if (imageUri != null){

            final DocumentReference reference = fstore.collection("users").document(userId);
            final StorageReference fileRef = storageReference.child(userId+".jpg");

            uploadTask = fileRef.putFile(imageUri);

            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.get().load(uri).into(profileImage);

                            Toast.makeText(profile.this, "profile image changed ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            uploadTask = fileRef.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("imageUrl",myUri);

                        reference.update(userMap);
                        progressDialog.dismiss();

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }

    }

    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to Logout ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}