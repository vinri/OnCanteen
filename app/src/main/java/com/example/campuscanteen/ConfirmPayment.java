package com.example.campuscanteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.util.Random;

public class ConfirmPayment extends AppCompatActivity {

    private TextView totalBayar;
    private Button confirmBtn;
    private MaterialCardView cardView1, cardView2;

    private String canteenId, canteenName;
    private String paymentId;
    private String total="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);


        totalBayar = findViewById(R.id.totalBayarTextView);
        confirmBtn = findViewById(R.id.confirmPayBtn);
        cardView1 = findViewById(R.id.cardview1);
        cardView2 = findViewById(R.id.cardview2);

        paymentId = getRandomString(12);
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
        }else if (cardView1.isChecked()){
            Toast.makeText(this, "BCA selected", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "BNI selected", Toast.LENGTH_SHORT).show();
        }
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