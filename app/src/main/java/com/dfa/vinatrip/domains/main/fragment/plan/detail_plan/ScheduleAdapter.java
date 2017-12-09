package com.dfa.vinatrip.domains.main.fragment.plan.detail_plan;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;
import com.dfa.vinatrip.utils.AppUtil;

import java.util.List;

import static com.dfa.vinatrip.utils.Constants.FORMAT_DAY_VN;
import static com.dfa.vinatrip.utils.Constants.MILLISECOND_IN_DAY;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    private List<PlanSchedule> planScheduleList;
    private Context context;
    private Plan plan;

    public ScheduleAdapter(List<PlanSchedule> planScheduleList, Plan plan, Context context) {
        this.planScheduleList = planScheduleList;
        this.context = context;
        this.plan = plan;
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.ScheduleHolder holder, int position) {
        PlanSchedule planSchedule = planScheduleList.get(position);
        holder.tvTitle.setText(planSchedule.getTitle());
        holder.tvContent.setText(planSchedule.getContent());
        holder.tvDate.setText(AppUtil.formatTime(FORMAT_DAY_VN, planSchedule.getTimestamp()));
        if (AppUtil.getDateType(planSchedule.getTimestamp()) == 0) {
            holder.ivCircle.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_timeline_green));
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
            holder.tvContent.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            holder.viewLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorMain));
        } else {
            holder.ivCircle.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_timeline_gray));
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            holder.tvContent.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            holder.viewLine.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }
        if ((position == planScheduleList.size() - 1)
                && (planSchedule.getTimestamp() + MILLISECOND_IN_DAY) < System.currentTimeMillis()) {
            holder.llFinish.setVisibility(View.VISIBLE);
            plan.setExpired(true);
        } else {
            holder.llFinish.setVisibility(View.GONE);
            plan.setExpired(false);
        }
    }

    @Override
    public int getItemCount() {
        if (planScheduleList != null) {
            return planScheduleList.size();
        }
        return 0;
    }


    public static class ScheduleHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent, tvDate;
        private ImageView ivCircle;
        private View viewLine;
        private LinearLayout llFinish;

        public ScheduleHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.item_schedule_tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.item_schedule_tv_content);
            tvDate = (TextView) itemView.findViewById(R.id.item_schedule_tv_date);
            ivCircle = (ImageView) itemView.findViewById(R.id.item_schedule_iv_circle);
            viewLine = itemView.findViewById(R.id.item_schedule_view_line);
            llFinish = (LinearLayout) itemView.findViewById(R.id.item_schedule_ll_finish);
        }
    }
}
