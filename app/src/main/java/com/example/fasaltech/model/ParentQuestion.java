package com.example.fasaltech.model;

import java.util.List;

public class ParentQuestion {
    private int QId;
    private int mcId;
    private String questionText;
    private List<ChildOptions> childOptions;
    private int subq_id;
    private int subc_id;
    private boolean isShow = true;


    public ParentQuestion(int QId, int mcId, String questionText, List<ChildOptions> childOptions, int subq_id, int subc_id,boolean isShow) {
        this.QId = QId;
        this.mcId = mcId;
        this.questionText = questionText;
        this.childOptions = childOptions;
        this.subq_id = subq_id;
        this.subc_id = subc_id;
        this.isShow = isShow;
    }

    public int getSubq_id() {
        return subq_id;
    }

    public void setSubq_id(int subq_id) {
        this.subq_id = subq_id;
    }

    public int getSubc_id() {
        return subc_id;
    }

    public void setSubc_id(int subc_id) {
        this.subc_id = subc_id;
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

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
