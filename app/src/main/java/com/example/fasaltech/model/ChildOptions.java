package com.example.fasaltech.model;

public class ChildOptions {

   private int choiceId;
   private String choice;

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
}
