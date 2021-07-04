package com.example.campuscanteen;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.campuscanteen.regSeller.TAG;

public class AdapterCanteen extends RecyclerView.Adapter<AdapterCanteen.ViewHolder> {

    private final List<ModelCanteen> list;

    public AdapterCanteen(List<ModelCanteen> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterCanteen.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_canteen, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelCanteen itemList = list.get(position);
        holder.textNameCanteen.setText(list.get(position).getCanteenName());
        Log.d(TAG, "#########canteenUri: " +itemList.getCanteenImgUrl());
        Log.d(TAG, "#########canteenName: " +itemList.getCanteenName());
        Log.d(TAG, "#########canteenId: " +itemList.getCanteenId());
        Picasso.get()
                .load(itemList
                .getCanteenImgUrl())
                .placeholder(R.drawable.ic_group)
                .into(holder.imageView);
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
        private final ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            textNameCanteen = v.findViewById(R.id.tvCanteenItem);
            imageView = v.findViewById(R.id.canteenImg);



        }
    }

}
