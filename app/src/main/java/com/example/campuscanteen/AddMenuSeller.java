package com.example.campuscanteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddMenuSeller extends AppCompatActivity {
    public static final String TAG = "TAG";

    EditText foodName, priceTag, qty;
    String userId, menuId;

    Button addButton;


    FirebaseFirestore fstore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_seller);

        foodName = findViewById(R.id.FoodName);
        priceTag = findViewById(R.id.price);
        addButton = findViewById(R.id.skip);

        fstore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });


    }

    private void addItem() {
        String name = foodName.getText().toString().trim();
        String price = priceTag.getText().toString().trim();
        Log.d(TAG, "#####onCreate: " + price);
        menuId = "menu"+getRandomString(4);

        if (name.isEmpty()){
            foodName.setError("Required");
            return;
        }

        userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference reference = fstore.collection("canteen").document(userId)
                .collection("menu").document(menuId);
        Map<String, Object> menu = new HashMap<>();
        menu.put("menuId", menuId);
        menu.put("FoodName", name);
        menu.put("price", price);
        menu.put("menuUrl", null);
//        menu.put("qty", quantity);

        reference.set(menu).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Menu is created for "+userId);
            }
        });
        Toast.makeText(AddMenuSeller.this, "Menu successfully Added", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(getApplicationContext(), addMenuImage.class));
        Intent intent = new Intent(getApplicationContext(), addMenuImage.class);
        intent.putExtra("menuId", menuId);
        startActivity(intent);
        finish();

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