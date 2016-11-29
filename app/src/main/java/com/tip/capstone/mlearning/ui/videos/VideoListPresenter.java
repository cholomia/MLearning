package com.tip.capstone.mlearning.ui.videos;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 29/11/2016
 */

public class VideoListPresenter extends MvpNullObjectBasePresenter<VideoListView> {

    private static final int[] VIDEO_RES_ID = {R.raw.sample_video};
    private static final String[] VIDEO_NAME_WITHOUT_EXTENSION = {"sample_video"};
    private static final String[] VIDEO_TITLE = {"This is a sample video"};

    public void loadVideoList() {
        List<Video> videoList = new ArrayList<>();
        for (int i = 0; i < VIDEO_RES_ID.length; i++) {
            int resId = VIDEO_RES_ID[i];
            String nameWithoutExtension = i < VIDEO_NAME_WITHOUT_EXTENSION.length ?
                    VIDEO_NAME_WITHOUT_EXTENSION[i] : "";
            String videoTitle = i < VIDEO_TITLE.length ?
                    VIDEO_TITLE[i] : "";
            Video video = new Video(resId, nameWithoutExtension, videoTitle);
            videoList.add(video);
        }
        getView().setVideoList(videoList);
    }
}
