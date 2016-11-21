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
import com.tip.capstone.mlearning.model.Choice;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.model.UserAnswer;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class QuizActivity extends MvpViewStateActivity<QuizView, QuizPresenter> implements QuizView {

    private static final String TAG = QuizActivity.class.getSimpleName();
    private Realm realm;
    private ActivityQuizBinding binding;
    private Topic topic;
    private List<Question> questionList;
    private List<UserAnswer> userAnswerList;
    private int counter;
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
        counter = 0;
        userAnswerList = new ArrayList<>();
        questionList = presenter.getShuffledQuestionList(realm.copyFromRealm(topic.getQuestionRealmList()));
        onSetQuestion(questionList.get(counter));
    }

    private void onSetQuestion(Question question) {
        binding.txtQuestion.setText((counter + 1) + ".) " + question.getQuestion());
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
        if (counter <= 0) {
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
            userAnswerList.remove(counter);
            counter--;
            onSetQuestion(questionList.get(counter));
        }
    }

    @Override
    public void onNext() {

        Question question = questionList.get(counter);
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

        if (counter < questionList.size() - 1) {
            counter++;
            onSetQuestion(questionList.get(counter));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Submit?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // todo: compute score and show answer summary
                        }
                    })
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userAnswerList.remove(counter);
                            QuizActivity.this.onSetQuestion(questionList.get(counter));
                        }
                    })
                    .show();

        }
    }
}
