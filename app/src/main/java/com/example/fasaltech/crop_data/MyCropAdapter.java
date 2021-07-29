package com.example.fasaltech.crop_data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fasaltech.R;
import com.example.fasaltech.constant.Api;
import com.example.fasaltech.model.SoilDataModel;
import com.example.fasaltech.soil_data.ClickListener;
import com.example.fasaltech.soil_data.MySoilViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyCropAdapter extends RecyclerView.Adapter<MyCropViewHolder> {

    ArrayList<CropDataModel> data;
    CropClickListener cropClickListener;
    private int selected_position = -1;

    public MyCropAdapter(ArrayList<CropDataModel> data, CropClickListener clickListener) {
        this.data = data;
        this.cropClickListener = (CropClickListener) clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyCropViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.single_soil_row,parent,false);
        return new MyCropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyCropViewHolder holder, int position) {
        final CropDataModel temp=data.get(position);
        holder.t1.setText(data.get(position).getHeader());
        holder.t2.setText(data.get(position).getDesc());


        Glide.with(holder.img.getContext()).load(Api.cropDataUrl+temp.getId()+"/").into(holder.img);
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
                cropClickListener.onClick(temp);
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
