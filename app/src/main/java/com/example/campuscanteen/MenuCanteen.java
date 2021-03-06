package com.example.campuscanteen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
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

    private static final String TAG = "TAG";
    private RecyclerView recyclerView;
    private AdapterMenuCustomer adapter;
    private AdapterCanteen adapterCanteen;
    private TextView headerName;
    private Button placeOrderButton;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_canteen);


        recyclerView = findViewById(R.id.canteen_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        headerName = findViewById(R.id.dash);
        placeOrderButton = findViewById(R.id.placeOrderBtn);

        Intent data = getIntent();
        String name = data.getStringExtra("CanteenName");
        String id = data.getStringExtra("canteenId");
        headerName.setText(name);

        db.collection("canteen").document(id).collection("menu")
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
                        adapter = new AdapterMenuCustomer(list);
                        recyclerView.setAdapter(adapter);
                    }
                });
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "TOTAL BAYAR: "+Integer.toString(TotalBayar.getTotalBayar()));
                if (TotalBayar.getTotalBayar() == 0){
                    Toast.makeText(MenuCanteen.this, "add your order please", Toast.LENGTH_SHORT).show();
                }else if(TotalBayar.getTotalBayar() > 0){
//                    startActivity(new Intent(getApplicationContext(), ConfirmPayment.class));
                    Intent intentPayment = new Intent(getApplicationContext(), ConfirmPayment.class);
                    intentPayment.putExtra("canteenId", id);
                    Log.d(TAG, "onClick: "+FoodMenuModel.getFoodMenuModel());
                    startActivity(intentPayment);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TotalBayar.setTotalBayar(0);
    }
}