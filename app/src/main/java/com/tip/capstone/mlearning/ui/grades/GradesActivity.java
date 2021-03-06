package com.tip.capstone.mlearning.ui.grades;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityGradesBinding;
import com.tip.capstone.mlearning.model.AssessmentGrade;
import com.tip.capstone.mlearning.model.Grades;
import com.tip.capstone.mlearning.model.PreQuizGrade;
import com.tip.capstone.mlearning.model.QuizGrade;
import com.tip.capstone.mlearning.model.Term;
import com.tip.capstone.mlearning.model.Topic;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author pocholomia
 * @since 22/11/2016
 */
public class GradesActivity extends MvpActivity<GradesView, GradesPresenter> implements GradesView {

    private Realm realm;
    private GradesListAdapter adapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        ActivityGradesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_grades);

        // assumes that theme has toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Grades");

        // setup RecyclerView and adapter
        adapter = new GradesListAdapter();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        getData();
    }

    /**
     * Setup the Grades Data for both Short Quiz and Final Assessment
     */
    private void getData() {
        List<Grades> gradesList = new ArrayList<>();
        RealmResults<Term> termRealmResults = realm.where(Term.class).findAllSorted(Term.COL_SEQ);
        for (Term term : termRealmResults) {
            Grades headerTerm = new Grades();
            headerTerm.setHeader(true);
            headerTerm.setTitle(term.getTitle());
            headerTerm.setSequence(gradesList.size() + 1);
            gradesList.add(headerTerm);

            for (Topic topic : term.getTopics().sort(Topic.COL_SEQ)) {
                // topic header
                Grades topicHeader = new Grades();
                topicHeader.setHeader(true);
                topicHeader.setTitle(topic.getTitle());
                topicHeader.setSequence(gradesList.size() + 1);
                gradesList.add(topicHeader);

                Grades nonHeader = new Grades();

                nonHeader.setHeader(false);
                nonHeader.setTitle("Pre Quiz");
                PreQuizGrade preQuizGrade = realm.where(PreQuizGrade.class).equalTo(Constant.ID, topic.getId()).findFirst();
                if (preQuizGrade != null) {
                    QuizGrade preGrade = new QuizGrade();
                    preGrade.setRawScore(preQuizGrade.getRawScore());
                    preGrade.setItemCount(preQuizGrade.getItemCount());
                    nonHeader.setQuizGrade(preGrade);
                } else {
                    nonHeader.setQuizGrade(null);
                }
                nonHeader.setSequence(gradesList.size() + 1);
                gradesList.add(nonHeader);

                nonHeader = new Grades();
                nonHeader.setHeader(false);
                nonHeader.setTitle("Post Quiz");
                QuizGrade quizGrade = realm.where(QuizGrade.class).equalTo(Constant.ID, topic.getId()).findFirst();

                if (quizGrade != null) {
                    nonHeader.setQuizGrade(realm.copyFromRealm(quizGrade));
                } else {
                    nonHeader.setQuizGrade(null);
                }

                nonHeader.setSequence(gradesList.size() + 1);
                gradesList.add(nonHeader);
            }

        }

        AssessmentGrade assessmentGrade = realm.where(AssessmentGrade.class).findFirst();

        Grades gradeAssessmentHeader = new Grades();
        gradeAssessmentHeader.setTitle("Assessment");
        gradeAssessmentHeader.setSequence(gradesList.size() + 1);
        gradeAssessmentHeader.setAssessmentGrade(assessmentGrade != null ? realm.copyFromRealm(assessmentGrade) : new AssessmentGrade());
        gradesList.add(gradeAssessmentHeader);

        adapter.setGradesList(gradesList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grades, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_reset_grades:
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(PreQuizGrade.class);
                        realm.delete(QuizGrade.class);
                        realm.delete(AssessmentGrade.class);
                    }
                });
                getData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public GradesPresenter createPresenter() {
        return new GradesPresenter();
    }
}
