package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class regSeller extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText fullname,phonenum,emailaddress,userpassword,passwordConfirm,canteenNM;
    public String userId,canteenId,imageUrl=null,canteenImgUrl=null, level ="seller";
    private Uri uri;

    TextView login;

    Button register_button;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_seller);


        fullname = findViewById(R.id.name);
        emailaddress = findViewById(R.id.email);
        phonenum = findViewById(R.id.phone);
        userpassword = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.cnfrmPassword);
        canteenNM = findViewById(R.id.canteenName);
        uri = Uri.parse("drawable/ic_grouplogo.xml");

        register_button = findViewById(R.id.registerButton);
        login = findViewById(R.id.backToLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();



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
        String canteenName = canteenNM.getText().toString().trim();


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
                        userId = firebaseAuth.getCurrentUser().getUid();
                        if (task.isSuccessful()){

                            DocumentReference reference = fstore.collection("users").document(userId);
                            canteenId = userId;
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullNM);
                            user.put("phone",phoneNumber);
                            user.put("email",emailAdd);
                            user.put("level", level);
                            user.put("imageUrl",imageUrl);
                            DocumentReference canteenReff = fstore.collection("canteen").document(userId);
                            Map<String,Object> canteen = new HashMap<>();
                            canteen.put("CanteenName",canteenName);
                            canteen.put("canteenId",canteenId);
                            canteen.put("canteenImgUrl", canteenImgUrl);
                            canteenReff.set(canteen).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: canteen is created for "+userId);
                                }
                            });
                            reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "onSuccess: user profile is created for " + userId);

                                }
                            });
                        }
                        else {

                        }
                        Toast.makeText(regSeller.this, "Acoount successfully registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), profileSeller.class));
                    }
                });


    }

    




}