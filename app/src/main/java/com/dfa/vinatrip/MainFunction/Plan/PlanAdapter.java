package com.dfa.vinatrip.MainFunction.Plan;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plan> planList;
    private LayoutInflater layoutInflater;
    private Context context;
    private SwipeRefreshLayout srlReload;

    public PlanAdapter(Context context, List<Plan> planList, SwipeRefreshLayout srlReload) {
        this.planList = planList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.srlReload = srlReload;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanAdapter.PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);

        holder.tvName.setText(plan.getName());
        holder.tvDestination.setText(plan.getDestination());
        holder.tvDate.setText(plan.getDateGo() + " -> " + plan.getDateBack());
        holder.tvUserName.setText(plan.getUserMakePlan().getNickname());
        Picasso.with(context).load(plan.getUserMakePlan().getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivAvatar,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                // turn icon waiting off when finish
                                srlReload.setRefreshing(false);
                            }

                            @Override
                            public void onError() {
                            }
                        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDestination, tvDate, tvUserName, tvUpdate;
        private ImageView ivAvatar, ivIsMyPlan;

        public PlanViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_plan_tv_name);
            tvDestination = (TextView) itemView.findViewById(R.id.item_plan_tv_destination);
            tvDate = (TextView) itemView.findViewById(R.id.item_plan_tv_date);
            tvUserName = (TextView) itemView.findViewById(R.id.item_plan_tv_user_name);
            tvUpdate = (TextView) itemView.findViewById(R.id.item_plan_tv_update);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_plan_iv_avatar);
            ivIsMyPlan = (ImageView) itemView.findViewById(R.id.item_plan_iv_is_my_plan);
        }
    }
}
