package com.tip.capstone.mlearning.ui.term;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Term;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public interface TermView extends MvpView {

    void onTermClicked(Term term);

    void showAlert(String title, String message);
}
