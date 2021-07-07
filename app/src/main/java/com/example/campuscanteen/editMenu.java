package com.example.campuscanteen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class editMenu extends AppCompatActivity {

    private ImageView menuImage;
    private TextView menuName, price;
    private FloatingActionButton changeMenuImageBtn;
    private FloatingActionButton editMenuBtn;
    private Button deleteBtn;

    private String userId, menuId;

    private FirebaseFirestore fstore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        menuImage = findViewById(R.id.menuImageViewBtn);
        menuName = findViewById(R.id.menuNameTV);
        price = findViewById(R.id.priceTV);
        changeMenuImageBtn = findViewById(R.id.menuImageViewBtn);
        editMenuBtn = findViewById(R.id.editTextBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        menuId = intent.getStringExtra("menuId");

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });


    }

    private void deleteData() {

        final DocumentReference menuRef = fstore
                .collection("canteen").document(userId)
                .collection("menu").document(menuId);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to delete ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                menuRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editMenu.this, "Menu successfully deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), manageMenu.class));
                        finish();
                    }
                });
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
}