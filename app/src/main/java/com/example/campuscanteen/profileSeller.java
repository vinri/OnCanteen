package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static com.example.campuscanteen.Register.TAG;

public class profileSeller extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView username,email,phone,canteenName;
    private ImageView profileImage, canteenImage;
    private FloatingActionButton editImageBtn,editCanteenBtn;
    private Button manageMenuBtn;
    private String userId, myUri="", canteenUri="";
    private Uri imageUri;
    private boolean statusCode;

    private DrawerLayout drawerLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;
    private StorageReference storageReference, canteenStorageRef;
    private StorageTask uploadTask;

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

        editImageBtn = findViewById(R.id.changeImageBtn);
        editCanteenBtn = findViewById(R.id.changeCanteenImage);
        manageMenuBtn = findViewById(R.id.manageMenu);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("user/profilePicture");
        canteenStorageRef = FirebaseStorage.getInstance().getReference().child("canteen/canteenImage");
        StorageReference profileImageRef = storageReference.child(userId + ".jpg");
        StorageReference canteenRef = canteenStorageRef.child(userId + ".jpg");
        //Navigation Drawer
        Toolbar toolbar = findViewById(R.id.toolbarMenu);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState==null){
            navigationView.setCheckedItem(R.id.navUserProfile);
        }

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textView8);
        TextView navEmail = headerView.findViewById(R.id.textView11);
        ImageView img = headerView.findViewById(R.id.navCircleImageView);

        DocumentReference headerRef = fstore.collection("users")
                .document(userId);
        headerRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                navUsername.setText(value.getString("fName"));
                navEmail.setText(value.getString("email"));
                Picasso.get().load(value.getString("imageUrl")).into(img);

                Log.d(TAG, "onCreate: Image :"+ value.getString("imageUrl"));
            }
        });

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

        manageMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), manageMenu.class));
            }
        });

    }



    private void orderRecieved() {

        fstore.collection("transaction").whereEqualTo("status", "proceed")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                startActivity(new Intent(getApplicationContext(), ProceedOrderSeller.class));
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
            final DocumentReference reference = fstore.collection("users").document(userId);
            final StorageReference fileRef = storageReference.child(userId + ".jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profileImage);
                            Toast.makeText(profileSeller.this, "profile image changed ", Toast.LENGTH_SHORT).show();
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

    private void uploadCanteenImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("set your profile");
        progressDialog.setMessage("Please Wait, While we are setting your data ");
        progressDialog.show();

        if (imageUri != null) {
            final DocumentReference canteenRef = fstore.collection("canteen").document(userId);
            final StorageReference canteenImgRef = canteenStorageRef.child(userId + ".jpg");
            canteenImgRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    canteenImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(canteenImage);
                            Toast.makeText(profileSeller.this, "Canteen image changed ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            uploadTask = canteenImgRef.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return canteenImgRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        canteenUri = downloadUri.toString();

                        HashMap<String, Object> canteenMap = new HashMap<>();
                        canteenMap.put("canteenImgUrl",canteenUri);

                        canteenRef.update(canteenMap);
                        progressDialog.dismiss();

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.navHome:
                startActivity(new Intent(getApplicationContext(), SellerMainActivity.class));
                finish();
                break;
            case R.id.navManageMenu:
                startActivity(new Intent(getApplicationContext(), manageMenu.class));
                finish();
                break;
            case R.id.navOrderRecieved:
                startActivity(new Intent(getApplicationContext(), ProceedOrderSeller.class));
                finish();
                break;
            case R.id.navUserProfile:
                startActivity(new Intent(getApplicationContext(), profileSeller.class));
                finish();
                break;
            case R.id.navLogout:
                logout();
                break;
        }

        return true;
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