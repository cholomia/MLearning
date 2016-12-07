package com.tip.capstone.mlearning.ui.quiz;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.tip.capstone.mlearning.model.PreQuizGrade;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.QuizGrade;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.model.UserAnswer;
import com.tip.capstone.mlearning.ui.adapter.SummaryListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * @author pocholomia
 * @since 21/11/2016
 */
public class QuizActivity extends MvpViewStateActivity<QuizView, QuizPresenter> implements QuizView {

    private static final String TAG = QuizActivity.class.getSimpleName();
    private Realm realm;
    private ActivityQuizBinding binding;
    private Topic topic;
    private List<Question> questionList;
    private List<UserAnswer> userAnswerList;
    private ChoiceListAdapter choiceAdapter;
    private boolean preQuiz;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance(); // init realm db
        // check for intent
        int id = getIntent().getIntExtra(Constant.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found!", Toast.LENGTH_SHORT).show();
            finish();
        }
        // check if has data
        topic = realm.where(Topic.class).equalTo(Constant.ID, id).findFirst();
        if (topic == null) {
            Toast.makeText(getApplicationContext(), "No Topic Data Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        preQuiz = getIntent().getBooleanExtra(Constant.PRE_QUIZ, false);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);
        // assumes theme has toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // TODO: 24/11/2016 setup activity title on manifest instead of here
        getSupportActionBar().setTitle("Short Quiz");
        // setup RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // setup adapter
        choiceAdapter = new ChoiceListAdapter();
        binding.recyclerView.setAdapter(choiceAdapter);
        // setup additional data on layout using DataBinding
        binding.setView(getMvpView());
        String strNumItems = "Number of Items: " + topic.getQuestions().size();
        binding.txtNumItems.setText(strNumItems);
        Log.d(TAG, "onCreate: realm is instantiated");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO: 24/11/2016 override onBackPressed to alert user, do the same on AssessmentActivity
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
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
        setRetainInstance(true);
        return new QuizViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        // alert user that quiz already taken
        if (topic.getQuestions().size() <= 0) {
            new AlertDialog.Builder(this)
                    .setTitle("No Questions")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            QuizActivity.this.finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
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
        // setup data
        Log.d(TAG, "onNewViewStateInstance: applying algorithm to quiz realm results");
        ((QuizViewState) getViewState()).setCounter(0);

        userAnswerList = new ArrayList<>();
        questionList = presenter.getShuffledQuestionList(realm.copyFromRealm(topic.getQuestions()));

        ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);
        ((QuizViewState) getViewState()).setQuestionList(questionList);

        if (questionList.size() > 0)
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
    }

    /**
     * @param question question to display
     */
    private void onSetQuestion(Question question) {
        String header = "Short Quiz for " + topic.getTitle();
        binding.txtHeader.setText(header);
        binding.txtQuestion.setText((((QuizViewState) getViewState()).getCounter() + 1) + ".) " + question.getQuestion());
        if (question.getQuestion_type() == Constant.QUESTION_TYPE_MULTIPLE) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.etAnswer.setVisibility(View.GONE);
            choiceAdapter.setChoiceList(question.getChoices());
        } else if (question.getQuestion_type() == Constant.QUESTION_TYPE_IDENTIFICATION) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.etAnswer.setText("");
            binding.etAnswer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPrevious() {
        if (((QuizViewState) getViewState()).getCounter() <= 0) {
            // TODO: 24/11/2016 disable previous button instead of alert here if counter is 0
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
            userAnswerList.remove(((QuizViewState) getViewState()).getCounter() - 1);
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

        if (question.getQuestion_type() == Constant.QUESTION_TYPE_MULTIPLE) {
            Choice choice = choiceAdapter.getSelectedChoice();
            if (choice != null) {
                userAnswer.setUserAnswer(choice.getBody());
                userAnswer.setChoiceType(choice.getChoice_type());
            } else {
                Snackbar.make(binding.getRoot(), "Select Answer", Snackbar.LENGTH_SHORT).show();
                return;
            }
        } else if (question.getQuestion_type() == Constant.QUESTION_TYPE_IDENTIFICATION) {
            if (binding.etAnswer.getText().toString().isEmpty()) {
                Snackbar.make(binding.getRoot(), "Enter Answer", Snackbar.LENGTH_SHORT).show();
                return;
            }
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

    /**
     * Show Dialog Susmmary
     */
    private void showSummary() {
        DialogQuizSummaryBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_quiz_summary,
                null,
                false);
        int score = 0;
        final int items = userAnswerList.size();
        for (UserAnswer userAnswer : userAnswerList) {
            if (userAnswer.isCorrect()) score++;
        }
        dialogBinding.txtRawScore.setText(score + "/" + items);
        String ave = presenter.getAverage(score, items) + "%";
        dialogBinding.txtAverage.setText(ave);

        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        SummaryListAdapter summaryListAdapter = new SummaryListAdapter();
        summaryListAdapter.setUserAnswerList(userAnswerList);
        dialogBinding.recyclerView.setAdapter(summaryListAdapter);

        if (preQuiz) {
            dialogBinding.recyclerView.setVisibility(View.GONE);
        }

        final int finalScore = score;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (preQuiz) {
                    PreQuizGrade preQuizGrade = new PreQuizGrade();
                    preQuizGrade.setId(topic.getId());
                    preQuizGrade.setRawScore(finalScore);
                    preQuizGrade.setItemCount(items);
                    preQuizGrade.setDateUpdated(new Date().getTime());
                    realm.copyToRealmOrUpdate(preQuizGrade);
                } else {
                    QuizGrade quizGrade = new QuizGrade();
                    quizGrade.setId(topic.getId());
                    quizGrade.setRawScore(finalScore);
                    quizGrade.setItemCount(items);
                    quizGrade.setDateUpdated(new Date().getTime());
                    realm.copyToRealmOrUpdate(quizGrade);
                }
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
                })
                .show();
    }

}
