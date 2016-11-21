package com.tip.capstone.mlearning.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.App;
import com.tip.capstone.mlearning.databinding.ActivityMainBinding;
import com.tip.capstone.mlearning.ui.term.TermActivity;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setView(getMvpView());
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onStudyClicked() {
        startActivity(new Intent(this, TermActivity.class));
    }

    @Override
    public void onAssessmentClicked() {

    }

    @Override
    public void onVideosClicked() {

    }

    @Override
    public void onSimulationClicked() {

    }

    @Override
    public void onGlossaryClicked() {

    }
}
