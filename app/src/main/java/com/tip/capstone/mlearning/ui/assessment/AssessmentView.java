package com.tip.capstone.mlearning.ui.assessment;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.UserAnswer;

import java.util.List;

/**
 * @author pocholomia
 * @since 22/11/2016
 */

public interface AssessmentView extends MvpView {
    void onPrevious();

    void onNext();

    void restoreData(int counter, List<Assessment> assessmentList, List<UserAnswer> userAnswerList);
}
