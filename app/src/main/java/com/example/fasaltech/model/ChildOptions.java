package com.example.fasaltech.model;

public class ChildOptions {

    private int choiceId;
    private String choice;
    private boolean isSelected = false;

    public ChildOptions(int choiceId, String choice) {
        this.choiceId = choiceId;
        this.choice = choice;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
