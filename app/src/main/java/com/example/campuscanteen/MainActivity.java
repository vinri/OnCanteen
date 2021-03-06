package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    TextView createAccountButton;
    EditText loginEmail,loginPassword;
    String uId;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);
        createAccountButton = findViewById(R.id.register);
        loginBtn = findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            login();
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginEmail.getText().toString().isEmpty()){
                        loginEmail.setError("Email is missing");
                        return;
                }
                if (loginPassword.getText().toString().isEmpty()){
                        loginPassword.setError("Password is missing");
                        return;
                }

                firebaseAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                login();
                                finish();
                            }
                        },2000);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), registerAs.class));
                finish();
            }
        });


    }

    public void login(){
        uId = firebaseAuth.getCurrentUser().getUid();
        db.collection("users").document(uId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()){
                    String level = value.getString("level");

                    switch (level){
                        case "customer":
                            startActivity(new Intent(getApplicationContext(), dashboard.class));
                            finish();

                            break;

                        case "seller":
//                            FirebaseAuth.getInstance().signOut();
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            startActivity(new Intent(getApplicationContext(), SellerMainActivity.class));
                            finish();

                            break;

                    }
                }
            }
        });


    }

    private void progressDialog() {


        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please Wait, While we are setting your data ");
        progressDialog.show();
    }

}