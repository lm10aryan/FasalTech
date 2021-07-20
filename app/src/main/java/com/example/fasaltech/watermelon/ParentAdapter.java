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
    int preQuestionId = -1;
    int preChoiceId = -1;

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

   private boolean flag = false;

    @Override
    public void onBindViewHolder(@NonNull @NotNull ParentViewHolder parentViewHolder, int position) {
        ParentQuestion parentQuestion = parentList.get(position);
        parentViewHolder.parentQuestion.setText(parentQuestion.getQuestionText());
        ChildAdapter childAdapter = new ChildAdapter(parentQuestion.getChildOptions(), childOptions -> {
            if (preQuestionId == -1) {
                preQuestionId = parentQuestion.getQId();
                preChoiceId = childOptions.getChoiceId();
            }
            if (flag) {
                if (preQuestionId == parentQuestion.getQId() && preChoiceId == childOptions.getChoiceId() && !childOptions.isSelected()) {
                    for (int i = 0; i < subQuestionList.size(); i++) {
                        if (subQuestionList.get(i).getSubc_id() == childOptions.getChoiceId()) {
                            parentList.remove(subQuestionList.get(i));
                        }
                    }
                    notifyDataSetChanged();
                    return;
                }
                if (preQuestionId == parentQuestion.getQId() && preChoiceId != childOptions.getChoiceId()) {
                    for (int i = 0; i < subQuestionList.size(); i++) {
                        if (subQuestionList.get(i).getSubc_id() == preChoiceId) {
                            parentList.remove(subQuestionList.get(i));
                        }
                    }
                }
                preQuestionId = parentQuestion.getQId();
                preChoiceId = childOptions.getChoiceId();
            }
            flag = true;
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
