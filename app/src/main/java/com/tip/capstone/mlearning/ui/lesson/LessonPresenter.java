package com.tip.capstone.mlearning.ui.lesson;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.Topic;

import io.realm.Realm;

import static android.content.ContentValues.TAG;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public class LessonPresenter extends MvpNullObjectBasePresenter<LessonView> {
    public void refreshLessons(final String lessonJson) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Lesson.class);
                realm.createOrUpdateAllFromJson(Lesson.class, lessonJson);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                Log.d(TAG, "onSuccess: Realm Set Lesson List");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                realm.close();
                getView().showAlert("Database Error", "Error on Saving Lesson List");
            }
        });
    }
}
