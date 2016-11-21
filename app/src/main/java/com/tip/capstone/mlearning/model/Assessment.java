package com.tip.capstone.mlearning.model;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public class Assessment {

    @PrimaryKey
    private int id;
    private String question;
    private String answer;
    private RealmList<Choice> choiceRealmList;
    private int questionType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public RealmList<Choice> getChoiceRealmList() {
        return choiceRealmList;
    }

    public void setChoiceRealmList(RealmList<Choice> choiceRealmList) {
        this.choiceRealmList = choiceRealmList;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }
}
