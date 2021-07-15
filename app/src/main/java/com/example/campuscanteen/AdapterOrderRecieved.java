package com.example.campuscanteen;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterOrderRecieved extends RecyclerView.Adapter<AdapterOrderRecieved.ViewHolder> {

    private final List<ModelTransaction> list;

    public AdapterOrderRecieved(List<ModelTransaction> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public AdapterOrderRecieved.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterOrderRecieved.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderRecieved.ViewHolder holder, int position) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        ModelTransaction modelTransaction = list.get(position);
        holder.transId.setText(modelTransaction.getUserId());
        holder.total.setText(modelTransaction.getTotalBayar());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference documentReference = fstore
                        .collection("transaction")
                        .document(modelTransaction.getTransactionId());
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Accept Order");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, Object> newStatus = new HashMap<>();
                        newStatus.put("status","Delivered");
                        documentReference.update(newStatus);
                        Intent intent = new Intent(v.getContext(), ProceedOrderSeller.class);
                        v.getContext().startActivity(intent);
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
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView transId, total;


        public ViewHolder(View v) {
            super(v);
            transId = v.findViewById(R.id.transId);
            total = v.findViewById(R.id.totalBayarTv);


        }
    }
}
