package com.tip.capstone.mlearning.ui.assessment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.UserAnswer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 22/11/2016
 */

public class AssessmentViewState implements RestorableViewState<AssessmentView> {

    private static final String KEY_COUNTER = "key_counter";
    private static final String KEY_ANSWER = "key_user_answer_list";
    private static final String KEY_ASSESSMENT = "key_assessment_list";

    private int counter;
    private List<UserAnswer> userAnswerList;
    private List<Assessment> assessmentList;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putInt(KEY_COUNTER, counter);
        // todo: convert list user answer and question to json string then save on bundle out
    }

    @Override
    public RestorableViewState<AssessmentView> restoreInstanceState(Bundle in) {
        counter = in.getInt(KEY_COUNTER, 0);
        // todo: get json string of question and answer to list then assigned to field
        return this;
    }

    @Override
    public void apply(AssessmentView view, boolean retained) {
        view.restoreData(counter, assessmentList, userAnswerList);
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void decrementCounter() {
        counter--;
    }

    public void incrementCounter() {
        counter++;
    }

    public List<UserAnswer> getUserAnswerList() {
        return userAnswerList != null ? userAnswerList : new ArrayList<UserAnswer>();
    }

    public void setUserAnswerList(List<UserAnswer> userAnswerList) {
        this.userAnswerList = userAnswerList;
    }

    public List<Assessment> getAssessmentList() {
        return assessmentList != null ? assessmentList : new ArrayList<Assessment>();
    }

    public void setAssessmentList(List<Assessment> assessmentList) {
        this.assessmentList = assessmentList;
    }
}
