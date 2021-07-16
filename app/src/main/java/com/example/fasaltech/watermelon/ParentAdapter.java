package com.example.fasaltech.watermelon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fasaltech.R;
import com.example.fasaltech.model.ParentQuestion;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private final List<ParentQuestion> parentList;

    public ParentAdapter(List<ParentQuestion> parentList) {
        this.parentList = parentList;
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(parentViewHolder
                .rvChild
                .getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        layoutManager.setInitialPrefetchItemCount(parentQuestion.getChildOptions().size());
        ChildAdapter childAdapter = new ChildAdapter(parentQuestion.getChildOptions());
        parentViewHolder.rvChild.setLayoutManager(layoutManager);
        parentViewHolder.rvChild.setAdapter(childAdapter);
        parentViewHolder.rvChild.setRecycledViewPool(viewPool);
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
