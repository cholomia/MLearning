package com.tip.capstone.mlearning.ui.quiz;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ItemChoiceBinding;
import com.tip.capstone.mlearning.helper.ImageHelper;
import com.tip.capstone.mlearning.model.Choice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public class ChoiceListAdapter extends RecyclerView.Adapter<ChoiceListAdapter.ViewHolder> {

    private List<Choice> choiceList;
    private boolean[] selected;
    private Context context;

    public ChoiceListAdapter(Context context) {
        this.context = context;
        choiceList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemChoiceBinding itemChoiceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_choice,
                parent,
                false);
        return new ViewHolder(itemChoiceBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Choice choice = choiceList.get(position);
        holder.itemChoiceBinding.setChoice(choice);
        holder.itemChoiceBinding.setLetter(String.valueOf('A' + position));
        if (choice.getChoiceType() == Constant.DETAIL_TYPE_IMAGE) {
            Glide.with(context)
                    .load(ImageHelper.getResourceId(context, choice.getBody()))
                    .into(holder.itemChoiceBinding.imgChoice);
        }
        holder.itemChoiceBinding.checkbox.setChecked(selected[position]);
        holder.itemChoiceBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                resetSelected();
                selected[holder.getAdapterPosition()] = true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return choiceList.size();
    }

    public void setChoiceList(List<Choice> choiceList) {
        this.choiceList.clear();
        this.choiceList.addAll(choiceList);
        resetSelected();
        notifyDataSetChanged();
    }

    public void resetSelected() {
        selected = new boolean[choiceList.size()];
        for (int i = 0; i < choiceList.size(); i++) {
            selected[i] = false;
        }
    }

    public Choice getSelectedChoice() {
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                return choiceList.get(i);
            }
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemChoiceBinding itemChoiceBinding;

        public ViewHolder(ItemChoiceBinding itemChoiceBinding) {
            super(itemChoiceBinding.getRoot());
            this.itemChoiceBinding = itemChoiceBinding;
        }
    }
}
