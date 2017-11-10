package com.dfa.vinatrip.domains.main.fragment.plan;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.user.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plan> planList;
    private Context context;
    private User currentUser;
    private OnUpdateOrRemoveClick onUpdateOrRemoveClick;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public PlanAdapter(Context context, List<Plan> planList, User currentUser) {
        this.planList = planList;
        this.context = context;
        this.currentUser = currentUser;
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_green)
                .showImageForEmptyUri(R.drawable.photo_not_available)
                .showImageOnFail(R.drawable.photo_not_available)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanAdapter.PlanViewHolder holder, int position) {
        final Plan plan = planList.get(position);
        int lastItemPosition = planList.size() - 1;

        if (position == lastItemPosition && planList.size() > 2) {
            holder.viewFooter.setVisibility(View.VISIBLE);
        } else {
            holder.viewFooter.setVisibility(View.GONE);
        }

        holder.llDetail.setOnClickListener(view -> onUpdateOrRemoveClick.onClick(position));

        holder.tvName.setText(plan.getName());
        holder.tvDestination.setText(plan.getDestination());
        holder.tvDate.setText(String.format("%s %s %s", plan.getDateGo(), Html.fromHtml("&#10132;"), plan.getDateBack()));

        if (plan.getIdUserMakePlan() == currentUser.getId()) {
            holder.tvUserName.setText("Tôi");
            holder.tvUpdate.setVisibility(View.VISIBLE);

            holder.tvUpdate.setOnClickListener(view -> onUpdateOrRemoveClick.onUpdate(position));
        } else {
            holder.tvUpdate.setVisibility(View.GONE);
            holder.tvUpdate.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            holder.tvUserName.setText(plan.getUsernameUserMakePlan());
        }

        holder.tvRemove.setOnClickListener(view -> onUpdateOrRemoveClick.onRemove(position));

        if (plan.getAvatarUserMakePlan() != null) {
            imageLoader.displayImage(plan.getAvatarUserMakePlan(), holder.ivAvatar, imageOptions);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ic_avatar);
        }

        holder.ivBackground.setImageResource(plan.getIdBackground());
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public void setOnUpdateOrRemoveClick(OnUpdateOrRemoveClick onUpdateOrRemoveClick) {
        this.onUpdateOrRemoveClick = onUpdateOrRemoveClick;
    }

    public interface OnUpdateOrRemoveClick {
        void onUpdate(int position);

        void onRemove(int position);

        void onClick(int position);
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDestination, tvDate, tvUserName, tvUpdate, tvRemove;
        private ImageView ivAvatar, ivBackground;
        private LinearLayout llDetail;
        private View viewFooter;

        public PlanViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_plan_tv_name);
            tvDestination = (TextView) itemView.findViewById(R.id.item_plan_tv_destination);
            tvDate = (TextView) itemView.findViewById(R.id.item_plan_tv_date);
            tvUserName = (TextView) itemView.findViewById(R.id.item_plan_tv_user_name);
            tvUpdate = (TextView) itemView.findViewById(R.id.item_plan_tv_update);
            tvRemove = (TextView) itemView.findViewById(R.id.item_plan_tv_remove);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_plan_iv_avatar);
            llDetail = (LinearLayout) itemView.findViewById(R.id.item_plan_ll_detail);
            viewFooter = itemView.findViewById(R.id.item_plan_view_footer);
            ivBackground = (ImageView) itemView.findViewById(R.id.item_plan_iv_background);
        }
    }
}
