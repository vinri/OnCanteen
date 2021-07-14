package com.example.campuscanteen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMenuCustomer extends RecyclerView.Adapter<AdapterMenuCustomer.ViewHolder> {
    private final List<ModelMenu> list;
    private static final String TAG = "AdapterMenuCustomer";

    public AdapterMenuCustomer(List<ModelMenu> list) {
        this.list = list;
    }



    @NonNull
    @Override
    public AdapterMenuCustomer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterMenuCustomer.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMenuCustomer.ViewHolder holder, int position) {
        ModelMenu menu = list.get(position);
        holder.textNameMenu.setText(list.get(position).getFoodName());
        holder.textPrice.setText(list.get(position).getPrice());
        Picasso.get().load(menu.getMenuUrl()).placeholder(R.drawable.ic_grouplogo).into(holder.imageView);
        int price = Integer.parseInt(menu.getPrice());
        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Log.d(TAG, String.format("oldValue: %d   newValue: %d", oldValue, newValue));
                if (oldValue < newValue){
                    TotalBayar.setTotalBayar(TotalBayar.getTotalBayar()+price);

                }else {
                    TotalBayar.setTotalBayar(TotalBayar.getTotalBayar()-price);

                }
                Log.d(TAG, "onValueChange: "+ TotalBayar.getTotalBayar());
            }
        });

//        holder.elegantNumberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String num = holder.elegantNumberButton.getNumber();
//                Log.d(TAG, "onClick: "+num);
//                To
//            }
//        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), addToCart.class);
//                intent.putExtra("menuName", menu.getFoodName());
//                intent.putExtra("price", menu.getPrice());
//                intent.putExtra("menuUrl", menu.getMenuUrl());
//                intent.putExtra("menuId", menu.getMenuId());
//                v.getContext().startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView textNameMenu, textPrice;
        private final ImageView imageView;
        private final ElegantNumberButton elegantNumberButton;


        public ViewHolder(View v) {
            super(v);
            textNameMenu = v.findViewById(R.id.tvMenuSeller);
            textPrice = v.findViewById(R.id.tvPrice);
            imageView = v.findViewById(R.id.menuImgView);
            elegantNumberButton = v.findViewById(R.id.elegantNumberButton);


        }
    }
}
