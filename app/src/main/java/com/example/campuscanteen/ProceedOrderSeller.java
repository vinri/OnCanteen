package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.campuscanteen.Register.TAG;

public class ProceedOrderSeller extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String userId;
    private DrawerLayout drawerLayout;

    private TextView userName;
    private RecyclerView recyclerView;
    private AdapterAllOrder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_order_seller);


        userId = firebaseAuth.getCurrentUser().getUid();
        //nav drawer
        Toolbar toolbar = findViewById(R.id.toolbarMenu);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState==null){

            navigationView.setCheckedItem(R.id.navOrderRecieved);
        }
        recyclerView = findViewById(R.id.recycleViewAllOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textView8);
        TextView navEmail = headerView.findViewById(R.id.textView11);
        ImageView img = headerView.findViewById(R.id.navCircleImageView);

        DocumentReference headerRef = db.collection("users")
                .document(userId);
        headerRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                navUsername.setText(value.getString("fName"));
                navEmail.setText(value.getString("email"));
                Picasso.get().load(value.getString("imageUrl")).into(img);

                Log.d(TAG, "onCreate: Image :"+ value.getString("imageUrl"));
            }
        });



        db.collection("transaction")
                .whereEqualTo("canteenId", userId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                snapshot();

            }
        });
    }

    private void snapshot() {
        db.collection("transaction").whereEqualTo("canteenId", userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<ModelTransaction> list = new ArrayList<>();
                if(value != null) {
                    for(QueryDocumentSnapshot item : value) {
                        list.add(new ModelTransaction(
                                item.getString("canteenId"),
                                item.getString("userId"),
                                item.getString("total"),
                                item.getString("transactionId"),
                                item.getString("status")
                        ));
                    }
                }

                adapter = new AdapterAllOrder(list);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.navHome:
                startActivity(new Intent(getApplicationContext(), SellerMainActivity.class));
                finish();
                break;
            case R.id.navManageMenu:
                startActivity(new Intent(getApplicationContext(), manageMenu.class));
                finish();
                break;
            case R.id.navOrderRecieved:
                startActivity(new Intent(getApplicationContext(), ProceedOrderSeller.class));
                finish();
                break;
            case R.id.navUserProfile:
                startActivity(new Intent(getApplicationContext(), profileSeller.class));
                finish();
                break;
            case R.id.navLogout:
                logout();
                break;
        }

        return true;
    }

    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to Logout ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
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