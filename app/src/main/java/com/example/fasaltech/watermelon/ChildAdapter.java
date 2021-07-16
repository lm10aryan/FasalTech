package com.example.fasaltech.watermelon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fasaltech.R;
import com.example.fasaltech.model.ChildOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    private List<ChildOptions> childOptions;

    public ChildAdapter(List<ChildOptions> childOptions) {
        this.childOptions = childOptions;
    }

    @NonNull
    @NotNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChildViewHolder holder, int position) {
        ChildOptions childOption = childOptions.get(position);
        holder.tvOptions.setText(childOption.getChoice());
    }

    @Override
    public int getItemCount() {
        return childOptions.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView tvOptions;

        public ChildViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvOptions = itemView.findViewById(R.id.tvChild);
        }
    }
}
