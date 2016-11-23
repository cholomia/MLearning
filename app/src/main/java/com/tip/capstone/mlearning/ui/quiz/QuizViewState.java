package com.tip.capstone.mlearning.ui.quiz;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.UserAnswer;

import java.util.List;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public class QuizViewState implements RestorableViewState<QuizView> {
    private static final String KEY_COUNTER = "key_counter";
    private static final String KEY_ANSWER = "key_user_answer_list";
    private static final String KEY_QUESTION = "key_question_list";

    private int counter;
    private List<UserAnswer> userAnswerList;
    private List<Question> questionList;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putInt(KEY_COUNTER, counter);
        // todo: convert list user answer and question to json string then save on bundle out
    }

    @Override
    public RestorableViewState<QuizView> restoreInstanceState(Bundle in) {
        counter = in.getInt(KEY_COUNTER, 0);
        // todo: get json string of question and answer to list then assigned to field
        return this;
    }

    @Override
    public void apply(QuizView view, boolean retained) {
        view.restoreData(counter, questionList, userAnswerList);
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void decrementCounter() {
        counter--;
    }

    public void incrementCounter() {
        counter++;
    }

    public void setUserAnswerList(List<UserAnswer> userAnswerList) {
        this.userAnswerList = userAnswerList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
