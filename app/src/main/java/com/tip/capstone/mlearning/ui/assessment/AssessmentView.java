package com.tip.capstone.mlearning.ui.assessment;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.Letter;
import com.tip.capstone.mlearning.model.UserAnswer;

import java.util.List;

/**
 * @author pocholomia
 * @see com.hannesdorfmann.mosby.mvp.MvpView
 * @since 22/11/2016
 */

@SuppressWarnings("WeakerAccess")
public interface AssessmentView extends MvpView {

    // let view to be public for DataBinding Uses (R.layout.activity_assessment)

    /**
     * Return to previous Assessment Question
     */
    void onPrevious();

    /**
     * Go to the next Assessment Question
     */
    void onNext();

    /**
     * Restore Data on Orientation Change or similar scenarios
     *
     * @param counter        the current counter/index of the question
     * @param assessmentList the assessment question list (algorithm already applied)
     * @param userAnswerList the user answer list
     */
    void restoreData(int counter, List<Assessment> assessmentList, List<UserAnswer> userAnswerList);

    void onLetterClicked(int position, boolean choice, Letter letter);
}
