package com.example.fasaltech.seed_data;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fasaltech.R;
import com.example.fasaltech.constant.Api;
import com.example.fasaltech.crop_data.CropDataModel;
import com.example.fasaltech.crop_data.MyCropViewHolder;
import com.example.fasaltech.model.SoilDataModel;
import com.example.fasaltech.soil_data.ClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MySeedAdapter extends RecyclerView.Adapter<MySeedViewHolder> {
    ArrayList<SeedDataModel> data;
    SeedClickListener seedClickListener;
    int number=0;
    int getPosition=-1;
    private int selected_position = -1;

    public MySeedAdapter(ArrayList<SeedDataModel> data, SeedClickListener seedClickListener) {
        this.data = data;
        this.seedClickListener = seedClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public MySeedViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.single_soil_row,parent,false);
        return new MySeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MySeedViewHolder holder, int position) {
        final SeedDataModel temp=data.get(position);
        holder.t1.setText(data.get(position).getHeader());
        holder.t2.setText(data.get(position).getDesc());
        Glide.with(holder.img.getContext()).load(Api.seedDataUrl+temp.getId()+"/").into(holder.img);

        if (selected_position == position) {
            holder.materialCardView.setChecked(true);
        }else {
            holder.materialCardView.setChecked(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selected_position==position){
                    selected_position=-1;
                    notifyDataSetChanged();
                    return;
                }
                seedClickListener.onClick(temp);
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
