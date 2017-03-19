package com.dfa.vinatrip.MainFunction.Me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListFriendVerticalAdapter extends RecyclerView.Adapter<ListFriendVerticalAdapter.ProfileViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<UserFriend> userFriendList;
    private TextView tvFriendNotAvailable;

    public ListFriendVerticalAdapter(Context context, List<UserFriend> userFriendList, TextView tvFriendNotAvailable) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.tvFriendNotAvailable = tvFriendNotAvailable;
        this.userFriendList = userFriendList;

        if (this.userFriendList.size() == 0) this.tvFriendNotAvailable.setVisibility(View.VISIBLE);
        else this.tvFriendNotAvailable.setVisibility(View.GONE);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_friend_vertical, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        final UserFriend userFriend = userFriendList.get(position);

        holder.tvNickname.setText(userFriend.getNickname());
        holder.tvEmail.setText(userFriend.getEmail());

        if (!userFriend.getAvatar().equals("")) {
            Picasso.with(context).load(userFriend.getAvatar())
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.photo_not_available)
                    .into(holder.ivAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return userFriendList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickname, tvEmail;
        private ImageView ivAvatar;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            tvNickname = (TextView) itemView.findViewById(R.id.item_friend_vertical_tv_nickname);
            tvEmail = (TextView) itemView.findViewById(R.id.item_friend_vertical_tv_email);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_friend_vertical_iv_avatar);
        }
    }
}