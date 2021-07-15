package com.example.campuscanteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class manageMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private AdapterMenu adapterMenu;
    private String curentUser;
    private DrawerLayout drawerLayout;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

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

            navigationView.setCheckedItem(R.id.navManageMenu);
        }


        addButton = findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.menuCanteen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        curentUser = firebaseAuth.getCurrentUser().getUid();
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textView8);
        TextView navEmail = headerView.findViewById(R.id.textView11);
        ImageView img = headerView.findViewById(R.id.navCircleImageView);

        DocumentReference headerRef = db.collection("users")
                .document(curentUser);
        headerRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                navUsername.setText(value.getString("fName"));
                navEmail.setText(value.getString("email"));
                Picasso.get().load(value.getString("imageUrl")).into(img);

                Log.d(TAG, "onCreate: Image :"+ value.getString("imageUrl"));
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