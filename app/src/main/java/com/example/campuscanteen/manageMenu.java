package com.example.campuscanteen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class manageMenu extends AppCompatActivity {
    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private AdapterMenu adapterMenu;
    private String curentUser;
    private TextView headerName;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_seller);

        addButton = findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.menuCanteen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        curentUser = firebaseAuth.getCurrentUser().getUid();
        headerName = findViewById(R.id.tvCanteenName);

        DocumentReference reference = db.collection("canteen").document(curentUser);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                headerName.setText(value.getString("CanteenName"));
            }
        });

        db.collection("canteen").document(curentUser).collection("menu")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<ModelMenu> list = new ArrayList<>();
                if (value!=null){
                    for (QueryDocumentSnapshot item : value){
                        list.add(new ModelMenu(
                                item.getString("FoodName")
                                ,item.getString("menuId")
                                ,item.getString("price")
                                ,item.getString("menuUrl")
                        ));
                    }
                }
                adapterMenu = new AdapterMenu(list);
                recyclerView.setAdapter(adapterMenu);
            }
        });



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu();
            }
        });

    }

    private void addMenu() {
        startActivity(new Intent(getApplicationContext(), AddMenuSeller.class));
        finish();
    }
}