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
    private int rawScore;
    private int itemCount;
    private long dateUpdated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    /**
     * @return average of the grade score/items * 50 + 50
     */
    public double average() {
        return (((double) rawScore / (double) itemCount) * 50.0) + 50.0;
    }
}
