package com.example.campuscanteen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {
    private static final String TAG = "CanteenActivity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String user;

    private TextView userName;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterCanteen adapter;

    Button button;
    ChipNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        recyclerView = findViewById(R.id.canteen_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        navigationBar = findViewById(R.id.navBar);
        button = findViewById(R.id.toProfile);
        userName = findViewById(R.id.tvUsername);
        user = firebaseAuth.getCurrentUser().getUid();

        if (savedInstanceState==null){
            navigationBar.setItemSelected(R.id.CanteenSelected,true);
        }

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.homeSelected:
                        navigationBar.setItemSelected(R.id.homeSelected,true);
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        finish();
                        break;
                    case R.id.CanteenSelected:
                        navigationBar.setItemSelected(R.id.CanteenSelected,true);
                        startActivity(new Intent(getApplicationContext(), dashboard.class));
                        finish();
                        break;
                    case R.id.aboutSelected:
                        navigationBar.setItemSelected(R.id.aboutSelected,true);
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        finish();
                        break;

                }
            }
        });

        DocumentReference reference = db.collection("users").document(user);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName.setText(value.getString("fName"));
            }
        });

        db.collection("canteen").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d(TAG, "onEvent: " );
                List<ModelCanteen> list = new ArrayList<>();
                if(value != null) {
                    for(QueryDocumentSnapshot item : value) {
                        list.add(new ModelCanteen(item.getString("CanteenId"), item.getString("CanteenName")));
                    }
                }

                adapter = new AdapterCanteen(list);
                recyclerView.setAdapter(adapter);
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),profile.class));
//                finish();
//            }
//        });
    }


}