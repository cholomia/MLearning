package com.tip.capstone.mlearning.ui.lesson;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.ui.lesson.detail.LessonDetailListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

class LessonPageAdapter extends FragmentPagerAdapter {

    private List<Lesson> lessonList;

    LessonPageAdapter(FragmentManager fm) {
        super(fm);
        lessonList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return LessonDetailListFragment.newInstance(lessonList.get(position).getId());
    }

    @Override
    public int getCount() {
        return lessonList.size();
    }

    void setLessonList(List<Lesson> lessonList) {
        this.lessonList.clear();
        this.lessonList.addAll(lessonList);
        notifyDataSetChanged();
    }
}
