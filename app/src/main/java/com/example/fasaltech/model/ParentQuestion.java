package com.example.fasaltech.model;

import java.util.List;

public class ParentQuestion {
    private int QId;
    private int mcId;
    private String questionText;
    private List<ChildOptions> childOptions;

    public ParentQuestion(int QId, int mcId, String questionText, List<ChildOptions> childOptions) {
        this.QId = QId;
        this.mcId = mcId;
        this.questionText = questionText;
        this.childOptions = childOptions;
    }

    public int getQId() {
        return QId;
    }

    public void setQId(int QId) {
        this.QId = QId;
    }

    public int getMcId() {
        return mcId;
    }

    public void setMcId(int mcId) {
        this.mcId = mcId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<ChildOptions> getChildOptions() {
        return childOptions;
    }

    public void setChildOptions(List<ChildOptions> childOptions) {
        this.childOptions = childOptions;
    }
}
