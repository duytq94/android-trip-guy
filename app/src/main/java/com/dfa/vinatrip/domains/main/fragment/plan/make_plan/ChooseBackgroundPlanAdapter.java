package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import android.content.Context;
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

    private LayoutInflater layoutInflater;
    private OnItemClick onItemClick;
    //    private List<Integer> listBackground;
//    private List<Boolean> listIsPhotoChoose;
    private int[] listBackground;
    private boolean[] listIsPhotoChoose;
    private Context context;

    public ChooseBackgroundPlanAdapter(Context context, int[] listBackground, boolean[] listIsPhotoChoose) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listBackground = listBackground;
        this.listIsPhotoChoose = listIsPhotoChoose;
        this.context = context;
    }

    @Override
    public BackgroundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_background_plan, parent, false);
        return new BackgroundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BackgroundViewHolder holder, final int position) {
        int backgroundId = listBackground[position];
        holder.ivBackground.setImageResource(backgroundId);
//        Picasso.with(context).load(backgroundId).into(holder.ivBackground);

        if (listIsPhotoChoose[position]) {
            holder.ivBorder.setVisibility(View.VISIBLE);
            holder.ivCover.setVisibility(View.GONE);
        } else {
            holder.ivBorder.setVisibility(View.GONE);
            holder.ivCover.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(position);
            }
        });
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
