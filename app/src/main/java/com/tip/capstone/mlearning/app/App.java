package com.tip.capstone.mlearning.app;

import android.app.Application;
import android.util.Log;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.helper.StringHelper;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;
import com.tip.capstone.mlearning.model.Term;
import com.tip.capstone.mlearning.model.Topic;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        // setting up realm
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.delete(Term.class);
                realm.createAllFromJson(Term.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.terms));

                realm.delete(Topic.class);
                realm.createOrUpdateAllFromJson(Topic.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.topics));

                realm.delete(Lesson.class);
                realm.createOrUpdateAllFromJson(Lesson.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.lessons));

                realm.delete(LessonDetail.class);
                realm.createOrUpdateAllFromJson(Lesson.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.lesson_details));

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                realm.close();
            }
        });

    }
}
