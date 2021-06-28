package com.example.campuscanteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.ViewHolder> {

    private final List<CanteenModel> list;

    public CanteenAdapter(List<CanteenModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CanteenAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_canteen, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CanteenModel itemList = list.get(position);
        holder.textNameCanteen.setText(list.get(position).getCanteenName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MenuCanteen.class);
                intent.putExtra("canteenId",itemList.getCanteenId());
                intent.putExtra("CanteenName",itemList.getCanteenName());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView textNameCanteen;

        public ViewHolder(View v) {
            super(v);
            textNameCanteen = v.findViewById(R.id.tvCanteenItem);



        }
    }

}
