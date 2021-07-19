package com.example.fasaltech.watermelon;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fasaltech.R;
import com.example.fasaltech.model.ChildOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder>{

    private List<ChildOptions> childOptions;
    WatermelonQuestionClickListener clickListener;
    private int selected_position = -1;
    public ChildAdapter(List<ChildOptions> childOptions, WatermelonQuestionClickListener clickListener) {
        this.childOptions = childOptions;
        this.clickListener = clickListener;
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
        //holder.tvOptions.setText(childOption.getChoice());
        if(childOption.getChoice()=="Haha"){
            holder.tvOptions.setVisibility(View.INVISIBLE);
            holder.watermelonquestionradiobutton.setVisibility(View.INVISIBLE);
            holder.choiceEditText.setVisibility(View.VISIBLE);
            //Log.i("Id in holder",childOption.getChoice());
        }else {
            holder.tvOptions.setText(childOption.getChoice());
            if (selected_position == position) {
                holder.watermelonquestionradiobutton.setChecked(true);

            }else {
                holder.watermelonquestionradiobutton.setChecked(false);
            }

            holder.watermelonquestionradiobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selected_position==position){
                        selected_position=-1;
                        notifyDataSetChanged();

                        return;
                    }
                    clickListener.onClick(childOption);
                    selected_position = position;
                    notifyDataSetChanged();
                }
            });
        }



    }
    @Override
    public int getItemCount() {
        return childOptions.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView tvOptions;
        RadioButton watermelonquestionradiobutton;
        EditText choiceEditText;
        public ChildViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvOptions = itemView.findViewById(R.id.tvChild);
            watermelonquestionradiobutton=itemView.findViewById(R.id.watermelonquestionradioButton);
            choiceEditText=itemView.findViewById(R.id.choiceEditText);
        }
    }
}