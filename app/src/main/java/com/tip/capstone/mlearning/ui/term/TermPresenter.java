package com.tip.capstone.mlearning.ui.term;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.capstone.mlearning.model.Term;

import io.realm.Realm;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class TermPresenter extends MvpNullObjectBasePresenter<TermView> {

    private static final String TAG = TermPresenter.class.getSimpleName();

    public void refreshTermList(final String termJson) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Term.class);
                realm.createAllFromJson(Term.class, termJson);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                Log.d(TAG, "onSuccess: Realm Set Term List");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                realm.close();
                getView().showAlert("Database Error", "Error on Saving Topic List");
            }
        });
    }

}
