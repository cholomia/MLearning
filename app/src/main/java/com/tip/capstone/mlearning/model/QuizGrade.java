package com.tip.capstone.mlearning.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public class QuizGrade extends RealmObject {

    @PrimaryKey
    private int id;
    private int lessonId;
    private int rawScore;
    private int itemCount;
    private int dateUpdated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getRawScore() {
        return rawScore;
    }

    public void setRawScore(int rawScore) {
        this.rawScore = rawScore;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(int dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
