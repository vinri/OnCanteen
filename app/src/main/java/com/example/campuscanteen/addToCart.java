package com.example.campuscanteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class addToCart extends AppCompatActivity {

    private TextView menuName, menuPrice;
    private ImageView imageView;
    private ElegantNumberButton qty;
    private Button addToBtn;
    private String userId;

    private FirebaseFirestore fstore;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        menuName = findViewById(R.id.menuNameTextView);
        menuPrice = findViewById(R.id.priceTextView);
        qty = findViewById(R.id.elegantNumberButton);
        addToBtn = findViewById(R.id.addToCartBtn);
        imageView = findViewById(R.id.imageView3);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        String menuNm = intent.getStringExtra("menuName");
        String price = intent.getStringExtra("price");
        String menuUrl = intent.getStringExtra("menuUrl");
        String menuId = intent.getStringExtra("menuId");

        menuName.setText(menuNm);
        menuPrice.setText(price);
        Picasso.get().load(menuUrl).placeholder(R.drawable.ic_grouplogo).into(imageView);


        qty.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = qty.getNumber();
            }
        });

        addToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}