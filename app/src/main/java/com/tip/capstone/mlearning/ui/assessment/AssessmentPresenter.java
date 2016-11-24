package com.tip.capstone.mlearning.ui.assessment;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.AssessmentChoice;

import java.util.List;

import io.realm.RealmList;

/**
 * @author pocholomia
 * @since 22/11/2016
 */

class AssessmentPresenter extends MvpNullObjectBasePresenter<AssessmentView> {

    /**
     * Fisher-Yates Shuffle Algorithm for both the questions and the choices for each question.
     *
     * @param assessmentList the list of quiz question to shuffle
     * @return a shuffled list of quiz question
     */
    List<Assessment> getShuffledAssessmentList(List<Assessment> assessmentList) {
        int n = assessmentList.size();
        for (int i = 0; i < n; i++) {
            // Get a random index of the array past i.
            int random = getRandomInt(i, n);
            // Swap the random element with the present element.
            Assessment randomAssessment = assessmentList.get(random);
            assessmentList.set(random, assessmentList.get(i));

            RealmList<AssessmentChoice> choiceList = randomAssessment.getAssessmentchoices();
            int m = choiceList.size();
            for (int j = 0; j < m; j++) {
                int r = getRandomInt(j, m);
                AssessmentChoice randomChoice = choiceList.get(r);
                choiceList.set(r, choiceList.get(j));
                choiceList.set(j, randomChoice);
            }
            randomAssessment.setAssessmentchoices(choiceList);


            assessmentList.set(i, randomAssessment);
        }
        return assessmentList;
    }

    /**
     * @param i    index
     * @param size size of the list
     * @return random index to used
     */
    private int getRandomInt(int i, int size) {
        return i + (int) (Math.random() * (size - i));
    }

    int getAverage(int score, int items) {
        return ((score / items) * 50) + 50;
    }

}
