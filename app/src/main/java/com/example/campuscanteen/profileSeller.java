package com.example.campuscanteen;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class profileSeller extends AppCompatActivity {
    private TextView username,email,phone,canteenName;
    private ImageView profileImage, canteenImage;
    private FloatingActionButton editImageBtn,editCanteenBtn;
    private Button logout;
//    private CircleImageView circleImageView,circleCanteenImage;
    private String userId, myUri="";
    private Uri imageUri;
    private boolean statusCode;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;
    private StorageReference storageReference, canteenStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_seller);

        username = findViewById(R.id.usernameTV);
        email = findViewById(R.id.userEmailTV);
        phone = findViewById(R.id.phoneNumTV);
        canteenName = findViewById(R.id.canteenNameTV);

        profileImage = findViewById(R.id.circleImageView);
        canteenImage = findViewById(R.id.circleCanteenImageView);

        editImageBtn = findViewById(R.id.changeImage);
        editCanteenBtn = findViewById(R.id.changeCanteenImage);
        logout = findViewById(R.id.logout);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("user/profilePicture");
        canteenStorageRef = FirebaseStorage.getInstance().getReference().child("canteen/canteenImage");
        StorageReference profileImageRef = storageReference.child(userId + ".jpg");
        StorageReference canteenRef = canteenStorageRef.child(userId + ".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        canteenRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(canteenImage);

            }
        });

        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                username.setText(value.getString("fName"));
                email.setText(value.getString("email"));
                phone.setText(value.getString("phone"));
            }
        });

        DocumentReference reference = fstore.collection("canteen").document(userId);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                canteenName.setText(value.getString("CanteenName"));
            }
        });

        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusCode = true;
                CropImage.activity().setAspectRatio(1,1).start(profileSeller.this);
            }
        });

        editCanteenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusCode = false;
                CropImage.activity().setAspectRatio(1,1).start(profileSeller.this);
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

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && statusCode == true){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            uploadImage();

        } if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && statusCode == false){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            uploadCanteenImage();

        } else {
            Toast.makeText(this, "Error, Try again ", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("set your profile");
        progressDialog.setMessage("Please Wait, While we are setting your data ");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageReference.child(userId + ".jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profileImage);
                            Toast.makeText(profileSeller.this, "profile image changed ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    private void uploadCanteenImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("set your profile");
        progressDialog.setMessage("Please Wait, While we are setting your data ");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = canteenStorageRef.child(userId + ".jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profileImage);
                            Toast.makeText(profileSeller.this, "Canteen image changed ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
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