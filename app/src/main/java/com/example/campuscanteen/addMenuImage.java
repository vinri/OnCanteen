package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static com.example.campuscanteen.AddMenuSeller.TAG;

public class addMenuImage extends AppCompatActivity {

    private FloatingActionButton editImageBtn;
    private Uri imageUri;
    private ImageView imageView;
    private String userId, menuId, myUri="";


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_image);

        editImageBtn = findViewById(R.id.changeImageBtn);
        imageView = findViewById(R.id.menuImage);

        firebaseAuth= FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("menu/"+userId);

        Intent intent = getIntent();
        menuId = intent.getStringExtra("menuId");
        StorageReference storageRef = storageReference.child(menuId+".jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });

        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(addMenuImage.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            uploadImage();

        } else {
            Toast.makeText(this, "Error, Try again ", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("set your menu");
        progressDialog.setMessage("Please Wait, While we are setting your data ");
        progressDialog.show();

        if (imageUri != null){

            final DocumentReference reference = fstore
                    .collection("canteen").document(userId)
                    .collection("menu").document(menuId);
            final StorageReference fileRef = storageReference.child(menuId+".jpg");
            Log.d(TAG, "##########uploadImage: "+menuId);

            uploadTask = fileRef.putFile(imageUri);

            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.get().load(uri).into(imageView);

                            Toast.makeText(addMenuImage.this, "profile image changed ", Toast.LENGTH_SHORT).show();
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
                        startActivity(new Intent(getApplicationContext(), manageMenu.class));
                        finish();

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }
}