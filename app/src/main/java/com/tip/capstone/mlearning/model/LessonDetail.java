package com.tip.capstone.mlearning.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class LessonDetail extends RealmObject {

    public static final String COL_SEQ = "sequence";
    @PrimaryKey
    private int id;
    private int sequence;
    private String body;
    private String bodyCaption;
    private int detailType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyCaption() {
        return bodyCaption;
    }

    public void setBodyCaption(String bodyCaption) {
        this.bodyCaption = bodyCaption;
    }

    public int getDetailType() {
        return detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }
}
