package com.tip.capstone.mlearning.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class Term extends RealmObject {

    public static final String COL_SEQ = "sequence";

    @PrimaryKey
    private int id;
    private int sequence;
    private String code;
    private String title;
    private String objective;
    private RealmList<Topic> topicRealmList;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public RealmList<Topic> getTopicRealmList() {
        return topicRealmList;
    }

    public void setTopicRealmList(RealmList<Topic> topicRealmList) {
        this.topicRealmList = topicRealmList;
    }
}
