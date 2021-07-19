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

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> implements WatermelonQuestionClickListener {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private final List<ParentQuestion> parentList;
    WatermelonMainClickListener watermelonMainClickListener;
    ChildAdapter childAdapter;

    ArrayList<Integer> childArrayList = new ArrayList<>();
    LinearLayoutManager layoutManager;


    public ParentAdapter(List<ParentQuestion> parentList, WatermelonMainClickListener watermelonMainClickListener) {
        this.parentList = parentList;
        this.watermelonMainClickListener = watermelonMainClickListener;
    }
    /*ChildAdapter childAdapter;
    ArrayList<Integer>childArrayList=new ArrayList<>();
    LinearLayoutManager layoutManager;

    public ParentAdapter(List<ParentQuestion> parentList) {
        this.parentList = parentList;
    }*/

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
        if (parentQuestion.isShow()) {
            parentViewHolder.parentQuestion.setText(parentQuestion.getQuestionText());
            childAdapter = new ChildAdapter(parentQuestion.getChildOptions(), this);
            layoutManager = new LinearLayoutManager(parentViewHolder
                    .rvChild
                    .getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);
            layoutManager.setInitialPrefetchItemCount(parentQuestion.getChildOptions().size());
            parentViewHolder.rvChild.setLayoutManager(layoutManager);
            parentViewHolder.rvChild.setAdapter(childAdapter);
            parentViewHolder.rvChild.setRecycledViewPool(viewPool);
        }

       /* if (!childArrayList.isEmpty()) {
            Log.i("being", "gone");
            for (int i = 0; i < childArrayList.size(); i++) {
                if (parentQuestion.getSubc_id() == childArrayList.get(i)) {
                    parentViewHolder.parentQuestion.setText(parentQuestion.getQuestionText());
                    childAdapter = new ChildAdapter(parentQuestion.getChildOptions(), this);
                    layoutManager = new LinearLayoutManager(parentViewHolder
                            .rvChild
                            .getContext(),
                            LinearLayoutManager.VERTICAL,
                            false);
                    layoutManager.setInitialPrefetchItemCount(parentQuestion.getChildOptions().size());

                    parentViewHolder.rvChild.setLayoutManager(layoutManager);
                    parentViewHolder.rvChild.setAdapter(childAdapter);
                    parentViewHolder.rvChild.setRecycledViewPool(viewPool);
                }
            }
        }
*/
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

    @Override
    public void onClick(ChildOptions childOptions) {
        Log.i("Clicked this", String.valueOf(childOptions.getChoiceId()));
        int choice_clicked = childOptions.getChoiceId();
        for (int i = 0; i < parentList.size(); i++) {
            if (parentList.get(i).getSubc_id() == choice_clicked) {
                parentList.get(i).setShow(true);
            }
        }
        notifyDataSetChanged();
       /* childArrayList.add(childOptions.getChoiceId());
        for (int i = 0; i < parentList.size(); i++) {
            ParentQuestion question_set = parentList.get(i);
            List<ChildOptions> childOptions1 = question_set.getChildOptions();
            for (int j = 0; j < childOptions1.size(); j++) {
                if (childOptions1.get(j).getChoiceId() == choice_clicked) {
                    Log.i("Parent value", question_set.getQuestionText());
                }
            }
        }*/
        //Log.i("Parent valye", parentList.get(i).getQuestionText());
        //Log.i("Child list",childArrayList.toString());
        //notifyDataSetChanged();
        //run function to check if clicked twice. we dont want same number...
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
