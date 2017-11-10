package com.dfa.vinatrip.domains.main.fragment.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.user.User;

import java.util.List;

public class ListFriendVerticalAdapter extends RecyclerView.Adapter<ListFriendVerticalAdapter.ProfileViewHolder> {
    private Context context;
    private List<User> friendList;

    public ListFriendVerticalAdapter(Context context) {
        this.context = context;
        this.friendList = friendList;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_vertical, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        final User friend = friendList.get(position);

//        holder.tvNickname.setText(friend.getNickname());
//        holder.tvEmail.setText(friend.getEmail());
//
//        if (!userFriend.getAvatar().equals("")) {
//            Picasso.with(context).load(userFriend.getAvatar())
//                   .placeholder(R.drawable.ic_loading)
//                   .error(R.drawable.photo_not_available)
//                   .into(holder.ivAvatar);
//        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
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