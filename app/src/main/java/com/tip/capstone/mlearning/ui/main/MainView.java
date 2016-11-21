package com.tip.capstone.mlearning.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 18/11/2016
 */
public interface MainView extends MvpView {

    void onStudyClicked();

    void onAssessmentClicked();

    void onVideosClicked();

    void onSimulationClicked();

    void onGlossaryClicked();
}
