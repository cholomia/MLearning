package com.tip.capstone.mlearning.ui.quiz;

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
import com.tip.capstone.mlearning.databinding.ActivityQuizBinding;
import com.tip.capstone.mlearning.databinding.DialogQuizSummaryBinding;
import com.tip.capstone.mlearning.model.Choice;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.QuizGrade;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.model.UserAnswer;
import com.tip.capstone.mlearning.ui.adapter.ChoiceListAdapter;
import com.tip.capstone.mlearning.ui.adapter.SummaryListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class QuizActivity extends MvpViewStateActivity<QuizView, QuizPresenter> implements QuizView {

    private static final String TAG = QuizActivity.class.getSimpleName();
    private Realm realm;
    private ActivityQuizBinding binding;
    private Topic topic;
    private List<Question> questionList;
    private List<UserAnswer> userAnswerList;
    private ChoiceListAdapter choiceAdapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        int id = getIntent().getIntExtra(Constant.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found!", Toast.LENGTH_SHORT).show();
            finish();
        }
        topic = realm.where(Topic.class).equalTo(Constant.ID, id).findFirst();
        if (topic == null) {
            Toast.makeText(getApplicationContext(), "No Topic Data Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Short Quiz");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        choiceAdapter = new ChoiceListAdapter(this);

        QuizGrade quizGrade = realm.where(QuizGrade.class).equalTo(Constant.ID, topic.getId()).findFirst();
        if (quizGrade != null) {
            // already taken the quiz
            new AlertDialog.Builder(this)
                    .setTitle("Retake Quiz?")
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
                            QuizActivity.this.finish();
                        }
                    })
                    .show();
        }

        Log.d(TAG, "onCreate: realm is instantiated");
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
        Log.d(TAG, "onDestroy: realm is closed");
        super.onDestroy();
    }

    @NonNull
    @Override
    public QuizPresenter createPresenter() {
        return new QuizPresenter();
    }

    @NonNull
    @Override
    public ViewState<QuizView> createViewState() {
        return new QuizViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        Log.d(TAG, "onNewViewStateInstance: applying algorithm to quiz realm results");
        ((QuizViewState) getViewState()).setCounter(0);

        userAnswerList = new ArrayList<>();
        questionList = presenter.getShuffledQuestionList(realm.copyFromRealm(topic.getQuestionRealmList()));

        ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);
        ((QuizViewState) getViewState()).setQuestionList(questionList);

        if (questionList.size() > 0)
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
    }

    private void onSetQuestion(Question question) {
        binding.txtHeader.setText("Short Quiz for " + topic.getTitle());
        binding.txtQuestion.setText((((QuizViewState) getViewState()).getCounter() + 1) + ".) " + question.getQuestion());
        if (question.getQuestionType() == Constant.QUESTION_TYPE_MULTIPLE) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.etAnswer.setVisibility(View.GONE);
            choiceAdapter.setChoiceList(question.getChoiceRealmList());
        } else if (question.getQuestionType() == Constant.QUESTION_TYPE_IDENTIFICATION) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.etAnswer.setText("");
            binding.etAnswer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPrevious() {
        if (((QuizViewState) getViewState()).getCounter() <= 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            QuizActivity.this.finish();
                        }
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();
        } else {
            userAnswerList.remove(((QuizViewState) getViewState()).getCounter());
            ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);

            ((QuizViewState) getViewState()).decrementCounter();
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
        }
    }

    @Override
    public void onNext() {
        Question question = questionList.get(((QuizViewState) getViewState()).getCounter());
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(question.getId());
        userAnswer.setCorrectAnswer(question.getAnswer());

        if (question.getQuestionType() == Constant.QUESTION_TYPE_MULTIPLE) {
            Choice choice = choiceAdapter.getSelectedChoice();
            if (choice != null) {
                userAnswer.setUserAnswer(choice.getBody());
                userAnswer.setChoiceType(choice.getChoiceType());
            }
        } else if (question.getQuestionType() == Constant.QUESTION_TYPE_IDENTIFICATION) {
            userAnswer.setUserAnswer(binding.etAnswer.getText().toString());
            userAnswer.setChoiceType(Constant.DETAIL_TYPE_IMAGE);
        }
        userAnswerList.add(userAnswer);
        ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);

        if (((QuizViewState) getViewState()).getCounter() < questionList.size() - 1) {
            ((QuizViewState) getViewState()).incrementCounter();
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
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
                            userAnswerList.remove(((QuizViewState) getViewState()).getCounter());
                            ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);
                            QuizActivity.this.onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
                        }
                    })
                    .show();

        }
    }

    @Override
    public void restoreData(int counter, List<Question> questionList, List<UserAnswer> userAnswerList) {
        this.questionList = questionList;
        this.userAnswerList = userAnswerList;
        if (questionList.size() > 0)
            onSetQuestion(questionList.get(counter));
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

        final QuizGrade quizGrade = new QuizGrade();
        quizGrade.setId(topic.getId());
        quizGrade.setRawScore(score);
        quizGrade.setItemCount(items);
        quizGrade.setDateUpdated(new Date().getTime());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(quizGrade);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Summary")
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        QuizActivity.this.finish();
                    }
                });
    }

}
