package com.tip.capstone.mlearning.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class Topic extends RealmObject {

    public static final String COL_SEQ = "sequence";

    @PrimaryKey
    private int id;
    private int sequence;
    private String title;
    private String description;
    private String objective;
    private String image;
    private RealmList<Lesson> lessonRealmList;
    private RealmList<Question> questionRealmList;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public RealmList<Lesson> getLessonRealmList() {
        return lessonRealmList;
    }

    public void setLessonRealmList(RealmList<Lesson> lessonRealmList) {
        this.lessonRealmList = lessonRealmList;
    }

    public RealmList<Question> getQuestionRealmList() {
        return questionRealmList;
    }

    public void setQuestionRealmList(RealmList<Question> questionRealmList) {
        this.questionRealmList = questionRealmList;
    }
}
