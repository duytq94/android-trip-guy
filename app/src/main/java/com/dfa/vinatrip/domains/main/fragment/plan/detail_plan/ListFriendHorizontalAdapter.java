package com.dfa.vinatrip.domains.main.fragment.plan.detail_plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.UserFriend;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListFriendHorizontalAdapter extends RecyclerView.Adapter<ListFriendHorizontalAdapter.ProfileViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<UserFriend> friendInvitedList;
    private TextView tvFriendNotAvailable;

    public ListFriendHorizontalAdapter(Context context, List<UserFriend> friendInvitedList, TextView tvFriendNotAvailable) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.tvFriendNotAvailable = tvFriendNotAvailable;
        this.friendInvitedList = friendInvitedList;

        if (this.friendInvitedList.size() == 0)
            this.tvFriendNotAvailable.setVisibility(View.VISIBLE);
        else this.tvFriendNotAvailable.setVisibility(View.GONE);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_friend_horizontal, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        final UserFriend userFriend = friendInvitedList.get(position);

        holder.tvNickname.setText(userFriend.getNickname());

        if (!userFriend.getAvatar().equals("")) {
            Picasso.with(context).load(userFriend.getAvatar())
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.photo_not_available)
                    .into(holder.civAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return friendInvitedList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickname;
        private ImageView civAvatar;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            tvNickname = (TextView) itemView.findViewById(R.id.item_friend_horizontal_tv_nickname);
            civAvatar = (ImageView) itemView.findViewById(R.id.item_friend_horizontal_civ_avatar);
        }
    }
}