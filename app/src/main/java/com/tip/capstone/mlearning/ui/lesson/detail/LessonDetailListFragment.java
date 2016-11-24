package com.tip.capstone.mlearning.ui.lesson.detail;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.FragmentLessonBinding;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;

import java.util.Locale;

import io.realm.Realm;

/**
 * @author pocholomia
 * @since 18/11/2016
 */
public class LessonDetailListFragment
        extends MvpFragment<LessonDetailListView, LessonDetailListPresenter>
        implements LessonDetailListView, TextToSpeech.OnInitListener {

    private static final String ARG_LESSON_ID = "arg-lesson-id";

    private int lessonId;
    private Realm realm;
    private FragmentLessonBinding binding;
    private TextToSpeech textToSpeech;

    /**
     * Create New Instance of the fragment with the parameters as Bundle
     *
     * @param lessonId lesson id to be diplay on the fragment
     * @return new instance of LessonDetailFragment
     */
    public static LessonDetailListFragment newInstance(int lessonId) {
        LessonDetailListFragment fragment = new LessonDetailListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LESSON_ID, lessonId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public LessonDetailListPresenter createPresenter() {
        return new LessonDetailListPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retrieving parameters/bundle/arguments
        if (getArguments() != null) lessonId = getArguments().getInt(ARG_LESSON_ID, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lesson, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // init data
        realm = Realm.getDefaultInstance();
        Lesson lesson = realm.where(Lesson.class).equalTo(Constant.ID, lessonId).findFirst();
        LessonDetailListAdapter lessonDetailListAdapter = new LessonDetailListAdapter(lesson, getMvpView());
        binding.recyclerView.setAdapter(lessonDetailListAdapter);
        lessonDetailListAdapter.setLessonDetails(realm.copyFromRealm(lesson.getLessondetails().sort(LessonDetail.COL_SEQ)));
        textToSpeech = new TextToSpeech(getContext(), this);
    }

    @Override
    public void onStop() {
        // Don't forget to shutdown textToSpeech!
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        realm.close(); // close realm
        super.onStop();
    }

    @Override
    public void onDetailClick(final LessonDetail lessonDetail) {
        new AlertDialog.Builder(getContext())
                .setTitle("Text-to-Speech")
                .setMessage("Listen to the selected Lesson?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeech.speak(lessonDetail.getBody(), TextToSpeech.QUEUE_FLUSH, null, lessonDetail.getId() + "");
                        } else {
                            //noinspection deprecation
                            textToSpeech.speak(lessonDetail.getBody(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                Snackbar.make(binding.getRoot(), "The TextToSpeech Language is not Supported",
                        Snackbar.LENGTH_SHORT).show();
            }

        } else {
            Snackbar.make(binding.getRoot(), "TextToSpeech Initialization Failed!",
                    Snackbar.LENGTH_SHORT).show();
            Log.e("TTS", "Initilization Failed!");
        }
    }
}
