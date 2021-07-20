package com.example.fasaltech.watermelon;

import com.example.fasaltech.crop_data.CropDataModel;
import com.example.fasaltech.model.ChildOptions;
import com.example.fasaltech.model.ParentQuestion;

import java.util.List;

public interface WatermelonQuestionClickListener {
    void onClick(ChildOptions childOptions, List<ChildOptions> childOptionsList);
}
