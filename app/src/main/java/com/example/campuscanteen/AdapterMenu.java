package com.example.campuscanteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.ViewHolder> {
    private final List<ModelMenu> list;

    public AdapterMenu(List<ModelMenu> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterMenu.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_menu, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelMenu menu = list.get(position);

        holder.textNameMenu.setText(list.get(position).getFoodName());
        holder.textPrice.setText(list.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), profileSeller.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key", menu);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView textNameMenu, textPrice;

        public ViewHolder(View v) {
            super(v);
            textNameMenu = v.findViewById(R.id.tvMenuSeller);
            textPrice = v.findViewById(R.id.tvPrice);


        }
    }
}
