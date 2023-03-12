package com.example.bleapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UIAdapter extends RecyclerView.Adapter<UIViewHolder> {
    private List<BluetoothDevice> deviceList;
    private Context context;

    public UIAdapter(Context context, List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;
        this.context = context;
    }

    @NonNull
    @Override
    public UIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_lay, parent, false);
        UIViewHolder viewHolder = new UIViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.deviceList.size();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull UIViewHolder holder, int position) {
        BluetoothDevice device = deviceList.get(position);
        holder.tvName.setText(device.getName());
        holder.tvAddr.setText(device.getAddress());
        holder.tvLink.setOnClickListener(v->{
            if (itemClickListener != null) {
                itemClickListener.itemClick(device);
            }
        });
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    interface ItemClickListener {
        void itemClick(BluetoothDevice device);
    }
}

class UIViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
    public TextView tvAddr;
    public TextView tvLink;

    public UIViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        tvAddr = itemView.findViewById(R.id.tvAddr);
        tvLink = itemView.findViewById(R.id.tvLink);
    }
}
