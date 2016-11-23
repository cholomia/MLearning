package com.tip.capstone.mlearning.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemUserAnswerBinding;
import com.tip.capstone.mlearning.helper.ImageHelper;
import com.tip.capstone.mlearning.model.UserAnswer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 22/11/2016
 */

public class SummaryListAdapter extends RecyclerView.Adapter<SummaryListAdapter.ViewHolder> {

    private List<UserAnswer> userAnswerList;

    public SummaryListAdapter() {
        userAnswerList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemUserAnswerBinding itemUserAnswerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user_answer,
                parent,
                false);
        return new ViewHolder(itemUserAnswerBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserAnswer userAnswer = userAnswerList.get(position);
        holder.itemUserAnswerBinding.setAnswer(userAnswer);
        if (userAnswer.getChoiceType() == 1) {
            Glide.with(holder.itemUserAnswerBinding.getRoot().getContext())
                    .load(ImageHelper.getResourceId(holder.itemUserAnswerBinding.getRoot().getContext(), userAnswer.getUserAnswer()))
                    .into(holder.itemUserAnswerBinding.imgUserAnswer);
            Glide.with(holder.itemUserAnswerBinding.getRoot().getContext())
                    .load(ImageHelper.getResourceId(holder.itemUserAnswerBinding.getRoot().getContext(), userAnswer.getCorrectAnswer()))
                    .into(holder.itemUserAnswerBinding.imgCorrectAnswer);
        }
    }

    @Override
    public int getItemCount() {
        return userAnswerList.size();
    }

    public void setUserAnswerList(List<UserAnswer> userAnswerList) {
        this.userAnswerList.clear();
        this.userAnswerList.addAll(userAnswerList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemUserAnswerBinding itemUserAnswerBinding;

        public ViewHolder(ItemUserAnswerBinding itemUserAnswerBinding) {
            super(itemUserAnswerBinding.getRoot());
            this.itemUserAnswerBinding = itemUserAnswerBinding;
        }
    }
}
