package com.tip.capstone.mlearning.ui.quiz;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.UserAnswer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @see com.tip.capstone.mlearning.ui.assessment.AssessmentViewState
 * similar to AssessmentViewState
 * @since 21/11/2016
 */

class QuizViewState implements RestorableViewState<QuizView> {
    private static final String KEY_COUNTER = "key_counter";
    private static final String KEY_ANSWER = "key_user_answer_list";
    private static final String KEY_QUESTION = "key_question_list";

    private int counter;
    private List<UserAnswer> userAnswerList;
    private List<Question> questionList;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putInt(KEY_COUNTER, counter);
        // todo: save identification input and selected on multiple choice
        Gson gson = new GsonBuilder().create();
        ArrayList<String> questionJson = new ArrayList<>();
        for (Question question : questionList) {
            questionJson.add(gson.toJson(question));
        }
        ArrayList<String> userAnswerJson = new ArrayList<>();
        for (UserAnswer userAnswer : userAnswerList) {
            userAnswerJson.add(gson.toJson(userAnswer));
        }
        out.putStringArrayList(KEY_QUESTION, questionJson);
        out.putStringArrayList(KEY_ANSWER, userAnswerJson);
    }

    @Override
    public RestorableViewState<QuizView> restoreInstanceState(Bundle in) {
        counter = in.getInt(KEY_COUNTER, 0);
        // todo: get identification input and selected on multiple choice
        Gson gson = new GsonBuilder().create();
        ArrayList<String> userAnswerJson = in.getStringArrayList(KEY_ANSWER);
        ArrayList<String> questionJson = in.getStringArrayList(KEY_QUESTION);
        questionList = new ArrayList<>();
        for (String question : questionJson != null ? questionJson : new ArrayList<String>()) {
            questionList.add(gson.fromJson(question, Question.class));
        }
        userAnswerList = new ArrayList<>();
        for (String answer : userAnswerJson != null ? userAnswerJson : new ArrayList<String>()) {
            userAnswerList.add(gson.fromJson(answer, UserAnswer.class));
        }
        return this;
    }

    @Override
    public void apply(QuizView view, boolean retained) {
        view.restoreData(counter, questionList, userAnswerList);
    }

    void setCounter(int counter) {
        this.counter = counter;
    }

    int getCounter() {
        return counter;
    }

    void decrementCounter() {
        counter--;
    }

    void incrementCounter() {
        counter++;
    }

    void setUserAnswerList(List<UserAnswer> userAnswerList) {
        this.userAnswerList = userAnswerList;
    }

    void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
