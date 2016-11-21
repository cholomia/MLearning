package com.tip.capstone.mlearning.ui.quiz;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 21/11/2016
 */
public interface QuizView extends MvpView {
    void onPrevious();

    void onNext();
}
