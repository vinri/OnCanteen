package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static com.example.campuscanteen.AddMenuSeller.TAG;

public class menuEdit extends AppCompatActivity {
    private ImageView menuImage;
    private TextView menuName, price;
    private FloatingActionButton changeMenuImageBtn;
    private Button editMenuBtn;
    private Button deleteButton;
    private Uri imageUri;

    private String userId, menuId, myUri;
    private String foodNm, priceTag;

    private FirebaseFirestore fstore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);


        menuImage = findViewById(R.id.menuImageView);
        menuName = findViewById(R.id.menuNameTV);
        price = findViewById(R.id.priceTV);
        changeMenuImageBtn = findViewById(R.id.changeImgBtn);
        editMenuBtn = findViewById(R.id.saveBtn);
        deleteButton = findViewById(R.id.deleteBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("menu/"+userId);

        Intent intent = getIntent();
        menuId = intent.getStringExtra("menuId");
        myUri = intent.getStringExtra("menuUrl");
        foodNm = intent.getStringExtra("foodName");
        priceTag = intent.getStringExtra("price");
        Log.d(TAG, "onCreate: "+menuId+"  "+myUri);



        DocumentReference menuRef = fstore
                .collection("canteen").document(userId)
                .collection("menu").document(menuId);
        menuRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                menuName.setText(value.getString("FoodName"));
                price.setText(value.getString("price"));
                Picasso.get().load(myUri).into(menuImage);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
        changeMenuImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(menuEdit.this);
            }
        });

        editMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editDataIntent = new Intent(getApplicationContext(), editDataMenu.class);
                editDataIntent.putExtra("foodName", foodNm);
                editDataIntent.putExtra("price", priceTag);
                editDataIntent.putExtra("menuId", menuId);
                startActivity(editDataIntent);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK
                && data != null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            uploadImage();

        }  else {
            Toast.makeText(this, "Error, Try again ", Toast.LENGTH_SHORT).show();
        }
    }



    private void uploadImage() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("set your profile");
        progressDialog.setMessage("Please Wait, While we are setting your data ");
        progressDialog.show();

        if (imageUri != null) {
            final DocumentReference reference = fstore
                    .collection("canteen").document(userId)
                    .collection("menu").document(menuId);
            final StorageReference fileRef = storageReference.child(menuId + ".jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(menuImage);
                            Toast.makeText(menuEdit.this, "menu image changed ", Toast.LENGTH_SHORT).show();
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
                        userMap.put("menuUrl",myUri);

                        reference.update(userMap);
                        progressDialog.dismiss();
                        finish();

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }

    }

    private void deleteData() {

        final DocumentReference menuRef = fstore
                .collection("canteen").document(userId)
                .collection("menu").document(menuId);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to delete ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                menuRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(menuEdit.this, "Menu successfully deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), manageMenu.class));
                        finish();
                    }
                });
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