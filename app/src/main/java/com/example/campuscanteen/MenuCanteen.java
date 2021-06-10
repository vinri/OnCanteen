package com.example.campuscanteen;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuCanteen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterMenuCustomer adapter;
    private CanteenAdapter canteenAdapter;
    private TextView headerName;
    private String getId;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_canteen);


        recyclerView = findViewById(R.id.canteen_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        headerName = findViewById(R.id.dash);

        DocumentReference Canteenreference = db.collection("canteen").document();
        Canteenreference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                headerName.setText(value.getString("CanteenName"));
            }
        });

        db.collection("canteen").document().collection("menu")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<ModelMenu> list = new ArrayList<>();
                        if (value!=null){
                            for (QueryDocumentSnapshot item : value){
                                list.add(new ModelMenu(item.getString("FoodName"),item.getString("menuId"),item.getString("price")));
                            }
                        }
                        adapter = new AdapterMenuCustomer(list);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
}