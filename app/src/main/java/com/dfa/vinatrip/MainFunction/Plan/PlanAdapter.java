package com.dfa.vinatrip.MainFunction.Plan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.Plan.DetailPlan.DetailPlanActivity_;
import com.dfa.vinatrip.MainFunction.Plan.MakePlan.MakePlanActivity_;
import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plan> planList;
    private LayoutInflater layoutInflater;
    private Context context;
    private UserProfile currentUser;

    public PlanAdapter(Context context, List<Plan> planList, UserProfile currentUser) {
        this.planList = planList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.currentUser = currentUser;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanAdapter.PlanViewHolder holder, final int position) {
        final Plan plan = planList.get(position);

        holder.llDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailPlanActivity_.class);

                // Send Plan to DetailPlanActivity
                intent.putExtra("Plan", planList.get(position));
                context.startActivity(intent);
            }
        });

        holder.tvName.setText(plan.getName());
        holder.tvDestination.setText(plan.getDestination());
        holder.tvDate.setText(plan.getDateGo() + " " + Html.fromHtml("&#10132;") + " " + plan.getDateBack());

        if (plan.getUserMakePlan().getUid().equals(currentUser.getUid())) {
            holder.tvUserName.setText("Tôi");
            holder.tvUpdate.setVisibility(View.VISIBLE);
            holder.tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MakePlanActivity_.class);

                    // Send Plan to MakePlanActivity to update info
                    intent.putExtra("Plan", planList.get(position));
                    context.startActivity(intent);
                }
            });
        } else {
            holder.ivIsMyPlan.setImageResource(0);
            holder.tvUpdate.setVisibility(View.GONE);
            holder.tvUpdate.setTextColor(android.R.color.darker_gray);
            holder.tvUserName.setText(plan.getUserMakePlan().getNickname());
        }

        Picasso.with(context).load(plan.getUserMakePlan().getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDestination, tvDate, tvUserName, tvUpdate;
        private ImageView ivAvatar, ivIsMyPlan;
        private LinearLayout llDetail;

        public PlanViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_plan_tv_name);
            tvDestination = (TextView) itemView.findViewById(R.id.item_plan_tv_destination);
            tvDate = (TextView) itemView.findViewById(R.id.item_plan_tv_date);
            tvUserName = (TextView) itemView.findViewById(R.id.item_plan_tv_user_name);
            tvUpdate = (TextView) itemView.findViewById(R.id.item_plan_tv_update);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_plan_iv_avatar);
            ivIsMyPlan = (ImageView) itemView.findViewById(R.id.item_plan_iv_is_my_plan);
            llDetail = (LinearLayout) itemView.findViewById(R.id.item_plan_ll_detail);
        }
    }
}
