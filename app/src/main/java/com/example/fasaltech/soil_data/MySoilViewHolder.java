package com.example.fasaltech.soil_data;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fasaltech.R;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class MySoilViewHolder extends RecyclerView.ViewHolder {
    ImageView img;
    TextView t1,t2;
    MaterialCardView materialCardView;

    public MySoilViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.img1);
        t1=itemView.findViewById(R.id.text1);
        t2=itemView.findViewById(R.id.text2);
        materialCardView=itemView.findViewById(R.id.cardView);
    }
}
