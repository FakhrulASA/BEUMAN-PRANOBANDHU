package com.example.beuman;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView name,address,needs,age,reason,time,type;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.nameza);
        address=itemView.findViewById(R.id.addressza);
        needs=itemView.findViewById(R.id.needsza);
        age=itemView.findViewById(R.id.ageza);
        reason=itemView.findViewById(R.id.reasonza);
        time=itemView.findViewById(R.id.timeza);
        type=itemView.findViewById(R.id.ageza);
    }

}
