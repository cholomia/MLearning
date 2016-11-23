package com.tip.capstone.mlearning.ui.assessment;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityAssessmentBinding;
import com.tip.capstone.mlearning.databinding.DialogQuizSummaryBinding;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.AssessmentGrade;
import com.tip.capstone.mlearning.model.Choice;
import com.tip.capstone.mlearning.model.UserAnswer;
import com.tip.capstone.mlearning.ui.adapter.ChoiceListAdapter;
import com.tip.capstone.mlearning.ui.adapter.SummaryListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AssessmentActivity extends MvpViewStateActivity<AssessmentView, AssessmentPresenter>
        implements AssessmentView {

    private static final String TAG = AssessmentActivity.class.getSimpleName();
    private Realm realm;
    private ActivityAssessmentBinding binding;
    private ChoiceListAdapter choiceAdapter;
    private RealmResults<Assessment> assessmentRealmResults;
    private List<UserAnswer> userAnswerList;
    private List<Assessment> assessmentList;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        assessmentRealmResults = realm.where(Assessment.class).findAll();
        if (assessmentRealmResults.size() <= 0) {
            Toast.makeText(getApplicationContext(), "No Assessment Data Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assessment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Assessment");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        choiceAdapter = new ChoiceListAdapter(this);

        AssessmentGrade assessmentGrade = realm.where(AssessmentGrade.class).findFirst();
        if (assessmentGrade != null) {
            // already taken the assessment
            new AlertDialog.Builder(this)
                    .setTitle("Retake Assessment?")
                    .setMessage("If Submitted, it will overwrite previous grade!")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AssessmentActivity.this.finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
    public AssessmentPresenter createPresenter() {
        return new AssessmentPresenter();
    }

    @NonNull
    @Override
    public ViewState<AssessmentView> createViewState() {
        return new AssessmentViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        Log.d(TAG, "onNewViewStateInstance: applying algorithm to quiz realm results");
        ((AssessmentViewState) getViewState()).setCounter(0);

        userAnswerList = new ArrayList<>();
        assessmentList = presenter.getShuffledAssessmentList(realm.copyFromRealm(assessmentRealmResults));

        ((AssessmentViewState) getViewState()).setUserAnswerList(userAnswerList);
        ((AssessmentViewState) getViewState()).setAssessmentList(assessmentList);

        onSetQuestion(assessmentList.get(((AssessmentViewState) getViewState()).getCounter()));
    }

    private void onSetQuestion(Assessment assessment) {
        binding.txtQuestion.setText((((AssessmentViewState) getViewState()).getCounter() + 1) + ".) " + assessment.getQuestion());
        if (assessment.getQuestionType() == Constant.QUESTION_TYPE_MULTIPLE) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.etAnswer.setVisibility(View.GONE);
            choiceAdapter.setChoiceList(assessment.getChoiceRealmList());
        } else if (assessment.getQuestionType() == Constant.QUESTION_TYPE_IDENTIFICATION) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.etAnswer.setText("");
            binding.etAnswer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPrevious() {
        if (((AssessmentViewState) getViewState()).getCounter() <= 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AssessmentActivity.this.finish();
                        }
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();
        } else {
            userAnswerList.remove(((AssessmentViewState) getViewState()).getCounter());
            ((AssessmentViewState) getViewState()).setUserAnswerList(userAnswerList);

            ((AssessmentViewState) getViewState()).decrementCounter();
            onSetQuestion(assessmentList.get(((AssessmentViewState) getViewState()).getCounter()));
        }
    }

    @Override
    public void onNext() {
        Assessment assessment = assessmentList.get(((AssessmentViewState) getViewState()).getCounter());
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(assessment.getId());
        userAnswer.setCorrectAnswer(assessment.getAnswer());

        if (assessment.getQuestionType() == Constant.QUESTION_TYPE_MULTIPLE) {
            Choice choice = choiceAdapter.getSelectedChoice();
            if (choice != null) {
                userAnswer.setUserAnswer(choice.getBody());
                userAnswer.setChoiceType(choice.getChoiceType());
            }
        } else if (assessment.getQuestionType() == Constant.QUESTION_TYPE_IDENTIFICATION) {
            userAnswer.setUserAnswer(binding.etAnswer.getText().toString());
            userAnswer.setChoiceType(Constant.DETAIL_TYPE_IMAGE);
        }
        userAnswerList.add(userAnswer);
        ((AssessmentViewState) getViewState()).setUserAnswerList(userAnswerList);

        if (((AssessmentViewState) getViewState()).getCounter() < assessmentList.size() - 1) {
            ((AssessmentViewState) getViewState()).incrementCounter();
            onSetQuestion(assessmentList.get(((AssessmentViewState) getViewState()).getCounter()));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Submit?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // todo: compute score and show answer summary
                            showSummary();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userAnswerList.remove(((AssessmentViewState) getViewState()).getCounter());
                            ((AssessmentViewState) getViewState()).setUserAnswerList(userAnswerList);
                            AssessmentActivity.this.onSetQuestion(assessmentList.get(((AssessmentViewState) getViewState()).getCounter()));
                        }
                    })
                    .show();

        }
    }

    @Override
    public void restoreData(int counter, List<Assessment> assessmentList, List<UserAnswer> userAnswerList) {
        this.assessmentList = assessmentList;
        this.userAnswerList = userAnswerList;
        onSetQuestion(assessmentList.get(counter));
    }

    private void showSummary() {
        DialogQuizSummaryBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_quiz_summary,
                null,
                false);
        int score = 0;
        int items = userAnswerList.size();
        for (UserAnswer userAnswer : userAnswerList) {
            if (userAnswer.isCorrect()) score++;
        }
        dialogBinding.txtRawScore.setText(score + "/" + items);
        dialogBinding.txtAverage.setText(presenter.getAverage(score, items) + "%");

        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        SummaryListAdapter summaryListAdapter = new SummaryListAdapter();
        summaryListAdapter.setUserAnswerList(userAnswerList);
        dialogBinding.recyclerView.setAdapter(summaryListAdapter);

        final AssessmentGrade assessmentGrade = new AssessmentGrade();
        assessmentGrade.setId(1);
        assessmentGrade.setRawScore(score);
        assessmentGrade.setItemCount(items);
        assessmentGrade.setDateUpdated(new Date().getTime());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(assessmentGrade);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Summary")
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AssessmentActivity.this.finish();
                    }
                });
    }

}
