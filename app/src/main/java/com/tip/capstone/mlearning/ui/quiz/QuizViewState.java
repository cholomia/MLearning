package com.tip.capstone.mlearning.ui.quiz;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public class QuizViewState implements RestorableViewState<QuizView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<QuizView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(QuizView view, boolean retained) {

    }
}
