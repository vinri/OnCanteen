package com.example.campuscanteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConfirmPayment extends AppCompatActivity {

    private TextView totalBayar;
    private Button confirmBtn;
    private MaterialCardView cardView1, cardView2;
    private ProgressBar progressBar;

    private String canteenId, canteenName;
    private String transactionId, userId;
    private String total="";


    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        totalBayar = findViewById(R.id.totalBayarTextView);
        confirmBtn = findViewById(R.id.confirmPayBtn);
        cardView1 = findViewById(R.id.cardview1);
        cardView2 = findViewById(R.id.cardview2);
        progressBar = findViewById(R.id.progressBar3);


        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        transactionId = getRandomString(12);
        Intent intent = getIntent();
        canteenId = intent.getStringExtra("canteenId");
        canteenName = intent.getStringExtra("canteenName");
        total = Integer.toString(TotalBayar.getTotalBayar());
        totalBayar.setText(total);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cardView1.isChecked()){
                    cardView1.setChecked(true);
                    cardView2.setChecked(false);
                }else{
                    cardView1.setChecked(false);
                }
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cardView2.isChecked()){
                    cardView2.setChecked(true);
                    cardView1.setChecked(false);
                }else{
                    cardView2.setChecked(false);
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPayment();
            }
        });

    }

    private void confirmPayment() {
        if (!cardView1.isChecked() && !cardView2.isChecked()){
            Toast.makeText(this, "Please Select Your Payment Method", Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    transactionDb();
                    TotalBayar.setTotalBayar(0);
                    startActivity(new Intent(getApplicationContext(), dashboard.class));
                    Toast.makeText(ConfirmPayment.this, "Ordered Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            },2000);

        }
    }

    private void transactionDb() {
        DocumentReference transactionRef = fstore
                .collection("transaction").document(transactionId);
        Map<String,Object> transaksi = new HashMap<>();
        transaksi.put("userId", userId);
        transaksi.put("canteenId", canteenId);
        transaksi.put("total",total);
        transaksi.put("transactionId", transactionId);
        transaksi.put("status", "proceed");
        transactionRef.set(transaksi);

    }

    public static String getRandomString(int i){

        String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        while (i > 0){
            Random random = new Random();
            result.append(character.charAt(random.nextInt(character.length())));
            i--;
        }
        return result.toString();

    }
}