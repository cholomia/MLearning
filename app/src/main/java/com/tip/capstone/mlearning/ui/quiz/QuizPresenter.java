package com.tip.capstone.mlearning.ui.quiz;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.capstone.mlearning.model.Choice;
import com.tip.capstone.mlearning.model.Question;

import java.util.List;

import io.realm.RealmList;

/**
 * @author pocholomia
 * @since 21/11/2016
 */
public class QuizPresenter extends MvpNullObjectBasePresenter<QuizView> {

    private static final String TAG = QuizPresenter.class.getSimpleName();

    /**
     * Fisher-Yates Shuffle Algorithm for both the questions and the choices for each question.
     *
     * @param questionList the list of quiz question to shuffle
     * @return a shuffled list of quiz question
     */
    public List<Question> getShuffledQuestionList(List<Question> questionList) {
        int n = questionList.size();
        for (int i = 0; i < n; i++) {
            // Get a random index of the array past i.
            int random = getRandomInt(i, n);
            // Swap the random element with the present element.
            Question randomQuestion = questionList.get(random);
            questionList.set(random, questionList.get(i));

            RealmList<Choice> choiceList = randomQuestion.getChoices();
            int m = choiceList.size();
            for (int j = 0; j < m; j++) {
                int r = getRandomInt(j, m);
                Choice randomChoice = choiceList.get(r);
                choiceList.set(r, choiceList.get(j));
                choiceList.set(j, randomChoice);
            }
            randomQuestion.setChoices(choiceList);


            questionList.set(i, randomQuestion);
        }
        return questionList;
    }

    /**
     * @param i    index
     * @param size size of the list
     * @return random index to used
     */
    private int getRandomInt(int i, int size) {
        return i + (int) (Math.random() * (size - i));
    }

    /**
     * @param score raw score
     * @param items total number of items
     * @return return average using score/items * 50 + 50
     */
    double getAverage(int score, int items) {
        double ave = (((double) score / (double) items) * 50.0) + 50.0;
        double a = score / items;
        Log.d(TAG, "getAverage: a:" + a);
        double b = a * 50;
        Log.d(TAG, "getAverage: b:" + b);
        double c = b + 50;
        Log.d(TAG, "getAverage: c:" + c);
        Log.d(TAG, "getAverage: s:" + score + ", i:" + items + ", a:" + ave);
        return ave;
    }
}
