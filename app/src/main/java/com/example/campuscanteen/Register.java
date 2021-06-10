package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText fullname,phonenum,emailaddress,userpassword,passwordConfirm;
    public String userId,profileImage=null, level ="customer";

    TextView login;

    Button register_button;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.name);
        emailaddress = findViewById(R.id.email);
        phonenum = findViewById(R.id.phone);
        userpassword = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.cnfrmPassword);

        register_button = findViewById(R.id.registerButton);
        login = findViewById(R.id.backToLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), profile.class));
            finish();
        }



        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDashboard();


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bacLogin();
            }
        });


    }

    private void bacLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to cencel registration ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to cencel registration ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

    private void openDashboard() {
        String fullNM = fullname.getText().toString().trim();
        String phoneNumber = phonenum.getText().toString().trim();
        String emailAdd = emailaddress.getText().toString().trim();
        String pw = userpassword.getText().toString().trim();
        String confirmPw = passwordConfirm.getText().toString().trim();

        if (fullNM.isEmpty()){
            fullname.setError("Required");
            return;
        }
        if (phoneNumber.isEmpty()){
            phonenum.setError("Required");
            return;
        }
        if (emailAdd.isEmpty()){
            emailaddress.setError("Required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()){
            emailaddress.setError("Please provide valid email");
            return;
        }
        if (pw.isEmpty()){
            userpassword.setError("Required");
            return;
        }
        if (confirmPw.isEmpty()){
            passwordConfirm.setError("Required");
            return;
        }
        if (!pw.equals(confirmPw)){
            passwordConfirm.setError("Password not match");
            return;
        }
            firebaseAuth.createUserWithEmailAndPassword(emailAdd, pw)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Register.this, "Account registered successfully", Toast.LENGTH_SHORT).show();
                                userId = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference reference = fstore.collection("users").document(userId);
                                Map<String,Object> user = new HashMap<>();
                                user.put("fName",fullNM);
                                user.put("phone",phoneNumber);
                                user.put("email",emailAdd);
                                user.put("level", level);
                                user.put("ImageUrl",profileImage);
                                reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Log.d(TAG, "onSuccess: user profile is created for " + userId);

                                    }
                                });
                            }
                            else {
                                Toast.makeText(Register.this, "Register Failed !", Toast.LENGTH_SHORT).show();
                            }
                            startActivity(new Intent(getApplicationContext(), profile.class));
                            finish();
                        }
                    });


    }

}