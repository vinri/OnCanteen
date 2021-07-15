package com.example.campuscanteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAllOrder extends RecyclerView.Adapter<AdapterAllOrder.ViewHolder> {

    private final List<ModelTransaction> list;

    public AdapterAllOrder(List<ModelTransaction> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterAllOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterAllOrder.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_transaction, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAllOrder.ViewHolder holder, int position) {
        ModelTransaction modelTransaction = list.get(position);
        holder.transId.setText(modelTransaction.getUserId());
        holder.total.setText(modelTransaction.getTotalBayar());
        holder.status.setText(modelTransaction.getStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView transId, total, status;


        public ViewHolder(View v) {
            super(v);
            this.transId = v.findViewById(R.id.transId2);
            this.total = v.findViewById(R.id.totalBayarTv2);
            this.status = v.findViewById(R.id.statusTV);


        }
    }
}
