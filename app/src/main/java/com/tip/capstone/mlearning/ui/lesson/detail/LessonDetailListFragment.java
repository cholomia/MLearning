package com.tip.capstone.mlearning.ui.lesson.detail;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.FragmentLessonBinding;
import com.tip.capstone.mlearning.model.Lesson;

import io.realm.Realm;


public class LessonDetailListFragment extends MvpFragment<LessonDetailListView, LessonDetailListPresenter> implements LessonDetailListView {

    private static final String ARG_LESSON_ID = "arg-lesson-id";

    private int lessonId;
    private Realm realm;

    public static LessonDetailListFragment newInstance(int lessonId) {
        LessonDetailListFragment fragment = new LessonDetailListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LESSON_ID, lessonId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public LessonDetailListPresenter createPresenter() {
        return new LessonDetailListPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) lessonId = getArguments().getInt(ARG_LESSON_ID, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLessonBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lesson, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        Lesson lesson = realm.where(Lesson.class).equalTo(Constant.ID, lessonId).findFirst();
        LessonDetailListAdapter lessonDetailListAdapter = new LessonDetailListAdapter(getContext(), lesson);
        lessonDetailListAdapter.setLessonDetails(realm.copyFromRealm(lesson.getLessonDetailRealmList()));
    }

    @Override
    public void onStop() {
        realm.close();
        super.onStop();
    }
}
