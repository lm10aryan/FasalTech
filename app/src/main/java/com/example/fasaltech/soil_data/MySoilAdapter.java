package com.example.fasaltech.soil_data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fasaltech.constant.Api;
import com.example.fasaltech.constant.Constant;
import com.example.fasaltech.R;
import com.example.fasaltech.model.SoilDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MySoilAdapter extends RecyclerView.Adapter<MySoilViewHolder> {

    ArrayList<SoilDataModel> data;
    ClickListener clickListener;
    private int selected_position = -1;

    public MySoilAdapter(ArrayList<SoilDataModel> data,ClickListener clickListener) {
        this.data = data;
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public MySoilViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.single_soil_row,parent,false);
        return new MySoilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MySoilViewHolder holder, int position) {
        final SoilDataModel temp=data.get(position);
        holder.t1.setText(data.get(position).getHeader());
        holder.t2.setText(data.get(position).getDesc());
        Glide.with(holder.img.getContext()).load(Api.soilDataUrl+temp.getId()+"/").into(holder.img);
        // Glide.with(holder.img.getContext()).load("http://ec2-52-66-244-191.ap-south-1.compute.amazonaws.com:8000/get-intro-photo/1/2/").into(holder.img);
        if (selected_position == position) {
            holder.materialCardView.setChecked(true);
        } else {
            holder.materialCardView.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_position == position) {
                    selected_position = -1;
                    notifyDataSetChanged();
                    return;
                }
                clickListener.onClick(temp);
                selected_position = position;
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
