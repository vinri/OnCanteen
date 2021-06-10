package com.example.campuscanteen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class registerAs extends AppCompatActivity {
    Button customerBtn, sellerBtn;

    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as);

        customerBtn = findViewById(R.id.customer);
        sellerBtn =  findViewById(R.id.seller);

        login =  findViewById(R.id.backToLogin);

        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });
        sellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), regSeller.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });



    }
    public void onBackPressed(){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
    }

}