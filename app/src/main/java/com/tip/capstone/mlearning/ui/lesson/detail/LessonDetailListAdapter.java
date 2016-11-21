package com.tip.capstone.mlearning.ui.lesson.detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailImageBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailTextBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonHeaderBinding;
import com.tip.capstone.mlearning.helper.ImageHelper;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class LessonDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HEADER = 0;
    private static final int VIEW_TEXT = 1;
    private static final int VIEW_IMAGE = 2;

    private Context context;
    private Lesson lesson;
    private List<LessonDetail> lessonDetails;

    public LessonDetailListAdapter(Context context, Lesson lesson) {
        this.context = context;
        this.lesson = lesson;
        lessonDetails = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && lesson != null) {
            return VIEW_HEADER;
        } else {
            int index = lesson != null ? position + 1 : position;
            switch (lessonDetails.get(index).getDetailType()) {
                case Constant.DETAIL_TYPE_TEXT:
                    return VIEW_TEXT;
                case Constant.DETAIL_TYPE_IMAGE:
                    return VIEW_IMAGE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                ItemLessonHeaderBinding itemLessonHeaderBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_header,
                        parent,
                        false);
                return new LessonHeaderViewHolder(itemLessonHeaderBinding);
            case VIEW_TEXT:
                ItemLessonDetailTextBinding itemLessonDetailTextBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_text,
                        parent,
                        false);
                return new LessonDetailTextViewHolder(itemLessonDetailTextBinding);
            case VIEW_IMAGE:
                ItemLessonDetailImageBinding itemLessonDetailImageBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_image,
                        parent,
                        false);
                return new LessonDetailImageViewHolder(itemLessonDetailImageBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_HEADER:
                LessonHeaderViewHolder lessonHeaderViewHolder = (LessonHeaderViewHolder) holder;
                lessonHeaderViewHolder.itemLessonHeaderBinding.setLesson(lesson);
                break;
            case VIEW_TEXT:
                LessonDetailTextViewHolder lessonDetailTextViewHolder = (LessonDetailTextViewHolder) holder;
                lessonDetailTextViewHolder.itemLessonDetailTextBinding
                        .setLessonDetail(lessonDetails.get(lesson != null ? position + 1 : position));
                break;
            case VIEW_IMAGE:
                LessonDetailImageViewHolder lessonDetailImageViewHolder = (LessonDetailImageViewHolder) holder;
                lessonDetailImageViewHolder.itemLessonDetailImageBinding
                        .setLessonDetail(lessonDetails.get(lesson != null ? position + 1 : position));
                Glide.with(context)
                        .load(ImageHelper.getResourceId(context,
                                lessonDetails.get(lesson != null ? position + 1 : position).getBody()))
                        .into(lessonDetailImageViewHolder.itemLessonDetailImageBinding.imageLessonDetail);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int size = lessonDetails.size();
        if (lesson != null) size++;
        return size;
    }

    public void setLessonDetails(List<LessonDetail> lessonDetails) {
        this.lessonDetails.clear();
        this.lessonDetails.addAll(lessonDetails);
    }

    public class LessonHeaderViewHolder extends RecyclerView.ViewHolder {
        private ItemLessonHeaderBinding itemLessonHeaderBinding;

        public LessonHeaderViewHolder(ItemLessonHeaderBinding itemLessonHeaderBinding) {
            super(itemLessonHeaderBinding.getRoot());
            this.itemLessonHeaderBinding = itemLessonHeaderBinding;
        }
    }

    public class LessonDetailTextViewHolder extends RecyclerView.ViewHolder {
        private ItemLessonDetailTextBinding itemLessonDetailTextBinding;

        public LessonDetailTextViewHolder(ItemLessonDetailTextBinding itemLessonDetailTextBinding) {
            super(itemLessonDetailTextBinding.getRoot());
            this.itemLessonDetailTextBinding = itemLessonDetailTextBinding;
        }
    }

    public class LessonDetailImageViewHolder extends RecyclerView.ViewHolder {
        private ItemLessonDetailImageBinding itemLessonDetailImageBinding;

        public LessonDetailImageViewHolder(ItemLessonDetailImageBinding itemLessonDetailImageBinding) {
            super(itemLessonDetailImageBinding.getRoot());
            this.itemLessonDetailImageBinding = itemLessonDetailImageBinding;
        }
    }

}
