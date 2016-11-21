package com.tip.capstone.mlearning.ui.lesson;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public interface LessonView extends MvpView {
    void showAlert(String title, String message);
}
