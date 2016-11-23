package com.tip.capstone.mlearning.ui.lesson;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityLessonBinding;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.ui.quiz.QuizActivity;

import io.realm.Realm;

public class LessonActivity extends MvpActivity<LessonView, LessonPresenter>
        implements LessonView, ViewPager.OnPageChangeListener {

    private ActivityLessonBinding binding;
    private Realm realm;

    private int dotsCount;
    private ImageView[] dots;
    private LessonPageAdapter lessonPageAdapter;
    private Topic topic;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lesson);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int topicId = getIntent().getIntExtra(Constant.ID, -1);
        if (topicId == -1) {
            Toast.makeText(this, "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        topic = realm.where(Topic.class).equalTo(Constant.ID, topicId).findFirst();
        if (topic == null) {
            Toast.makeText(this, "No Topic Object Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        getSupportActionBar().setTitle(topic.getTitle());

        lessonPageAdapter = new LessonPageAdapter(getSupportFragmentManager());
        binding.container.addOnPageChangeListener(this);
        binding.container.setAdapter(lessonPageAdapter);

        setUiPageViewController();
        showObjectives();

    }

    private void showObjectives() {
        new AlertDialog.Builder(this)
                .setTitle(topic.getTitle())
                .setMessage("Objective:\n" + topic.getObjective())
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public LessonPresenter createPresenter() {
        return new LessonPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lesson, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_quiz:
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra(Constant.ID, topic.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_selected_item_dot));
        }
        dots[position].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_item_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setUiPageViewController() {
        lessonPageAdapter.setLessonList(realm.copyFromRealm(topic.getLessons().sort(Lesson.COL_SEQ)));
        dotsCount = lessonPageAdapter.getCount();
        if (dotsCount <= 0) return;
        dots = new ImageView[dotsCount];
        binding.viewPagerCountDots.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            ContextCompat.getDrawable(this, R.drawable.non_selected_item_dot);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_selected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);

            binding.viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_item_dot));
    }

    @Override
    public void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", null)
                .show();
    }
}
