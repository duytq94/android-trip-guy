package com.dfa.vinatrip.domains.main.fragment.plan.detail_plan;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ListFriendHorizontalAdapter extends RecyclerView.Adapter<ListFriendHorizontalAdapter.ProfileViewHolder> {
    private LayoutInflater layoutInflater;
    private List<UserInPlan> friendInvitedList;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;
    private Plan plan;

    public ListFriendHorizontalAdapter(Context context, Plan plan) {
        this.layoutInflater = LayoutInflater.from(context);
        this.friendInvitedList = plan.getInvitedFriendList();
        this.imageLoader = ImageLoader.getInstance();
        this.imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_avatar)
                .showImageForEmptyUri(R.drawable.ic_avatar)
                .showImageOnFail(R.drawable.ic_avatar)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        this.plan = plan;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_friend_horizontal, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        final UserInPlan userFriend = friendInvitedList.get(position);
        if (userFriend.getId() != plan.getIdUserMakePlan()) {
            holder.llRoot.setVisibility(View.VISIBLE);
            holder.tvNickname.setText(userFriend.getUsername());
            if (!userFriend.getAvatar().equals("")) {
                imageLoader.displayImage(userFriend.getAvatar(), holder.civAvatar, imageOptions);
            }
        } else {
            holder.llRoot.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return friendInvitedList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickname;
        private ImageView civAvatar;
        private LinearLayout llRoot;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            tvNickname = (TextView) itemView.findViewById(R.id.item_friend_horizontal_tv_nickname);
            civAvatar = (ImageView) itemView.findViewById(R.id.item_friend_horizontal_civ_avatar);
            llRoot = (LinearLayout) itemView.findViewById(R.id.item_friend_horizontal_ll_root);
        }
    }
}