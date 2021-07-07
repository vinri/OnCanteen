package com.example.campuscanteen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static com.example.campuscanteen.AddMenuSeller.TAG;

public class editDataMenu extends AppCompatActivity {

    private EditText foodName, price;
    private Button save;
    private String userId, menuId, foodNm, priceTag;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_menu);

        foodName = findViewById(R.id.foodNameEditText);
        price = findViewById(R.id.priceEditText);
        save = findViewById(R.id.saveBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        menuId = intent.getStringExtra("menuId");
        foodNm = intent.getStringExtra("foodName");
        priceTag = intent.getStringExtra("price");

        Log.d(TAG, "onCreate: "+menuId+"  "+foodNm+"  "+priceTag );

        foodName.setText(foodNm);
        price.setText(priceTag);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSave();
            }
        });

    }

    private void editSave() {
        String theFoodName = foodName.getText().toString();
        String thePrice = price.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("set your profile");
        progressDialog.setMessage("Please Wait, While we are setting your data ");
        progressDialog.show();
        DocumentReference fileRef = fstore
                .collection("canteen").document(userId)
                .collection("menu").document(menuId);

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("FoodName",theFoodName);
        userMap.put("price", thePrice);

        fileRef.update(userMap);
        startActivity(new Intent(getApplicationContext(), manageMenu.class));
        progressDialog.dismiss();
        finish();

    }
}