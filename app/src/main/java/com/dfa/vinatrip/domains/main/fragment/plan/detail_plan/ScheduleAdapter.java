package com.dfa.vinatrip.domains.main.fragment.plan.detail_plan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;
import com.dfa.vinatrip.utils.AppUtil;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    private List<PlanSchedule> planScheduleList;

    public ScheduleAdapter(List<PlanSchedule> planScheduleList) {
        this.planScheduleList = planScheduleList;
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.ScheduleHolder holder, final int position) {
        PlanSchedule planSchedule = planScheduleList.get(position);
        holder.tvTitle.setText(planSchedule.getTitle());
        holder.tvContent.setText(planSchedule.getContent());
        holder.tvDate.setText(AppUtil.formatTime("dd/MM/yyyy", planSchedule.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return planScheduleList.size();
    }


    public static class ScheduleHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent, tvDate;
        private ImageView ivCircleGreen, ivCircleGray;

        public ScheduleHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.item_schedule_tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.item_schedule_tv_content);
            tvDate = (TextView) itemView.findViewById(R.id.item_schedule_tv_date);
            ivCircleGreen = (ImageView) itemView.findViewById(R.id.item_schedule_iv_circle_green);
            ivCircleGray = (ImageView) itemView.findViewById(R.id.item_schedule_iv_circle_gray);
        }
    }
}
