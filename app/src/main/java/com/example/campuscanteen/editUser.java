package com.example.campuscanteen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class editUser extends AppCompatActivity {

    private EditText userName, phoneNum;
    private Button save;
    private String userId, fName, phone;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        userName = findViewById(R.id.nameEditText);
        phoneNum = findViewById(R.id.phoneEditText);
        save = findViewById(R.id.saveBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();

        DocumentReference reference = fstore.collection("users").document(userId);
        reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName.setText(value.getString("fName"));
                phoneNum.setText(value.getString("phone"));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton();
            }
        });


    }

    private void saveButton() {

        fName = userName.getText().toString();
        phone = phoneNum.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("set your profile");
        progressDialog.setMessage("Please Wait, While we are setting your data ");
        progressDialog.show();
        DocumentReference fileRef = fstore.collection("users").document(userId);

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("fName",fName);
        userMap.put("phone", phone);

        fileRef.update(userMap);
        startActivity(new Intent(getApplicationContext(), profile.class));
        progressDialog.dismiss();
        finish();

    }
}