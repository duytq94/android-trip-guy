package com.dfa.vinatrip.domains.main.plan.make_plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;

import java.util.List;

/**
 * Created by DFA on 5/21/2017.
 */

public class ChooseBackgroundPlanAdapter
        extends RecyclerView.Adapter<ChooseBackgroundPlanAdapter.BackgroundViewHolder> {
    private LayoutInflater layoutInflater;
    List<Integer> backgroundList;

    public ChooseBackgroundPlanAdapter(Context context, List<Integer> backgroundList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.backgroundList = backgroundList;
    }

    @Override
    public BackgroundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_background_plan, parent, false);
        return new BackgroundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BackgroundViewHolder holder, int position) {
        Integer backgroundId = backgroundList.get(position);
        holder.ivBackground.setImageResource(backgroundId);
    }

    @Override
    public int getItemCount() {
        return backgroundList.size();
    }

    public static class BackgroundViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBackground;

        public BackgroundViewHolder(View itemView) {
            super(itemView);
            ivBackground = (ImageView) itemView.findViewById(R.id.item_background_plan_iv);
        }
    }

}
