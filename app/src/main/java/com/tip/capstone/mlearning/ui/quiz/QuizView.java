package com.tip.capstone.mlearning.ui.quiz;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.UserAnswer;

import java.util.List;

/**
 * @author pocholomia
 * @since 21/11/2016
 */
public interface QuizView extends MvpView {
    void onPrevious();

    void onNext();

    void restoreData(int counter, List<Question> questionList, List<UserAnswer> userAnswerList);
}
