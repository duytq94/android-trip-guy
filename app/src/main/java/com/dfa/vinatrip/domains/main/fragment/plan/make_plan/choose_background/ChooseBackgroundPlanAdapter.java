package com.dfa.vinatrip.domains.main.fragment.plan.make_plan.choose_background;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;

/**
 * Created by DFA on 5/21/2017.
 */

public class ChooseBackgroundPlanAdapter
        extends RecyclerView.Adapter<ChooseBackgroundPlanAdapter.BackgroundViewHolder> {

    private OnItemClick onItemClick;
    private int[] listBackground;
    private boolean[] listIsPhotoChoose;

    public ChooseBackgroundPlanAdapter(int[] listBackground, boolean[] listIsPhotoChoose) {
        this.listBackground = listBackground;
        this.listIsPhotoChoose = listIsPhotoChoose;
    }

    @Override
    public BackgroundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_background_plan, parent, false);
        return new BackgroundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BackgroundViewHolder holder, final int position) {
        int backgroundId = listBackground[position];
        holder.ivBackground.setImageResource(backgroundId);

        if (listIsPhotoChoose[position]) {
            holder.ivBorder.setVisibility(View.VISIBLE);
            holder.ivCover.setVisibility(View.GONE);
        } else {
            holder.ivBorder.setVisibility(View.GONE);
            holder.ivCover.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(view -> onItemClick.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return listBackground.length;
    }

    public static class BackgroundViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBackground, ivCover, ivBorder;

        public BackgroundViewHolder(View itemView) {
            super(itemView);
            ivBackground = (ImageView) itemView.findViewById(R.id.item_background_plan_iv);
            ivBorder = (ImageView) itemView.findViewById(R.id.item_background_plan_iv_border);
            ivCover = (ImageView) itemView.findViewById(R.id.item_background_plan_iv_cover);
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }
}
