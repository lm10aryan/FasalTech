package com.example.fasaltech.watermelon;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fasaltech.R;
import com.example.fasaltech.model.ChildOptions;
import com.example.fasaltech.model.ParentQuestion;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    private final List<ParentQuestion> parentList;
    WatermelonMainClickListener watermelonMainClickListener;
    private final List<ParentQuestion> subQuestionList;

    public ParentAdapter(List<ParentQuestion> parentList, List<ParentQuestion> subQuestionList, WatermelonMainClickListener watermelonMainClickListener) {
        this.parentList = parentList;
        this.watermelonMainClickListener = watermelonMainClickListener;
        this.subQuestionList = subQuestionList;
    }

    @NonNull
    @NotNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_item, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ParentViewHolder parentViewHolder, int position) {
        ParentQuestion parentQuestion = parentList.get(position);
        parentViewHolder.parentQuestion.setText(parentQuestion.getQuestionText());
        ChildAdapter childAdapter = new ChildAdapter(parentQuestion.getChildOptions(), (childOptions, listChildOptions) -> {
            boolean flg = false;
            for (int i = 0; i < listChildOptions.size(); i++) {
                if (!listChildOptions.get(i).isSelected()) {
                    for (int j = 0; j < parentList.size(); j++) {
                        if (listChildOptions.get(i).getChoiceId() == parentList.get(j).getSubc_id()) {
                            parentList.remove(parentList.get(j));
                            j--;
                            flg = true;
                        }
                    }
                }
            }
            if (flg){
                notifyDataSetChanged();
                return;
            }

            for (int i = 0; i < subQuestionList.size(); i++) {
                if (subQuestionList.get(i).getSubc_id() == childOptions.getChoiceId()) {
                    parentList.add(subQuestionList.get(i));
                }
            }
            notifyDataSetChanged();
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(parentViewHolder.rvChild.getContext());
        layoutManager.setInitialPrefetchItemCount(parentQuestion.getChildOptions().size());
        parentViewHolder.rvChild.setLayoutManager(layoutManager);
        parentViewHolder.rvChild.setAdapter(childAdapter);
        parentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (watermelonMainClickListener != null) {
                    watermelonMainClickListener.onClick(parentQuestion);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }


    public static class ParentViewHolder extends RecyclerView.ViewHolder {

        private final TextView parentQuestion;
        private final RecyclerView rvChild;

        public ParentViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            parentQuestion = itemView.findViewById(R.id.tvQuestion);
            rvChild = itemView.findViewById(R.id.rvChild);
        }
    }
}
