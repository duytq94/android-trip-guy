package com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ProfileViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private SwipeRefreshLayout srlReload;
    private List<UserProfile> listUserProfiles;
    private List<UserFriend> listUserFriends;
    private DatabaseReference referenceFriend;
    private UserProfile currentUser;

    public UserProfileAdapter(Context context, List<UserProfile> listUserProfiles,
                              List<UserFriend> listUserFriends, DatabaseReference referenceFriend,
                              UserProfile currentUser, SwipeRefreshLayout srlReload) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.srlReload = srlReload;

        this.listUserProfiles = listUserProfiles;
        for (int i = 0; i < listUserProfiles.size(); i++) {
            if (listUserProfiles.get(i).getUid().equals(currentUser.getUid())) {
                this.listUserProfiles.remove(i);
                break;
            }
        }

        this.listUserFriends = listUserFriends;
        this.currentUser = currentUser;
        this.referenceFriend = referenceFriend;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_user_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProfileViewHolder holder, int position) {
        final UserProfile userProfile = listUserProfiles.get(position);

        holder.tvNickname.setText(userProfile.getNickname());
        holder.tvEmail.setText(userProfile.getEmail());
        holder.tvSex.setText(userProfile.getSex());

        holder.btnMakeFriend.setText("Kết bạn");
        holder.btnMakeFriend.setTag("Kết bạn");
        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorGray));

        // If one userProfile is in listUserFriends of current user login
        for (int j = 0; j < listUserFriends.size(); j++) {
            if (userProfile.getUid().equals(listUserFriends.get(j).getFriendId())) {
                switch (listUserFriends.get(j).getState()) {
                    case "requested":
                        holder.btnMakeFriend.setText(R.string.sent);
                        holder.btnMakeFriend.setTag("Đã gửi");
                        break;
                    case "beRequested":
                        holder.btnMakeFriend.setText(R.string.agree);
                        holder.btnMakeFriend.setTag("Đồng ý");
                        break;
                    case "friend":
                        holder.btnMakeFriend.setText(R.string.friend);
                        holder.btnMakeFriend.setTag("Bạn bè");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorMain));
                        break;
                }
            }
        }
        holder.btnMakeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.btnMakeFriend.getTag().toString()) {
                    case "Đã gửi":
                        holder.btnMakeFriend.setText("Kết bạn");
                        holder.btnMakeFriend.setTag("Kết bạn");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
                        // Delete data to the current user login
                        referenceFriend.child("UserFriend")
                                .child(currentUser.getUid())
                                .child(userProfile.getUid())
                                .removeValue();
                        // Delete data to the user be requested
                        referenceFriend.child("UserFriend")
                                .child(userProfile.getUid())
                                .child(currentUser.getUid())
                                .removeValue();
                        break;

                    case "Đồng ý":
                        holder.btnMakeFriend.setText(R.string.friend);
                        holder.btnMakeFriend.setTag("Bạn bè");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorMain));
                        // Add profile user request to the current user
                        UserFriend userFriendBeRequested2 = new UserFriend(userProfile.getUid(),
                                userProfile.getNickname(), userProfile.getAvatar(),
                                userProfile.getEmail(), "friend");
                        referenceFriend.child("UserFriend")
                                .child(currentUser.getUid())
                                .child(userProfile.getUid())
                                .setValue(userFriendBeRequested2);
                        // Add profile current user to the user request
                        UserFriend userFriendCurrent2 = new UserFriend(currentUser.getUid(),
                                currentUser.getNickname(), currentUser.getAvatar(),
                                currentUser.getEmail(), "friend");
                        referenceFriend.child("UserFriend")
                                .child(userProfile.getUid())
                                .child(currentUser.getUid())
                                .setValue(userFriendCurrent2);
                        break;

                    case "Bạn bè":
                        holder.btnMakeFriend.setText("Kết bạn");
                        holder.btnMakeFriend.setTag("Kết bạn");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
                        // Delete data to the current user login
                        referenceFriend.child("UserFriend")
                                .child(currentUser.getUid())
                                .child(userProfile.getUid())
                                .removeValue();
                        // Delete data to the user be requested
                        referenceFriend.child("UserFriend")
                                .child(userProfile.getUid())
                                .child(currentUser.getUid())
                                .removeValue();
                        break;

                    case "Kết bạn":
                        holder.btnMakeFriend.setText(R.string.sent);
                        holder.btnMakeFriend.setTag("Đã gửi");
                        // Add profile user request to the current user
                        UserFriend userFriendBeRequested4 = new UserFriend(userProfile.getUid(),
                                userProfile.getNickname(), userProfile.getAvatar(),
                                userProfile.getEmail(), "requested");
                        referenceFriend.child("UserFriend")
                                .child(currentUser.getUid())
                                .child(userProfile.getUid())
                                .setValue(userFriendBeRequested4);
                        // Add profile current user to the user request
                        UserFriend userFriendCurrent4 = new UserFriend(currentUser.getUid(),
                                currentUser.getNickname(), currentUser.getAvatar(),
                                currentUser.getEmail(), "beRequested");
                        referenceFriend.child("UserFriend")
                                .child(userProfile.getUid())
                                .child(currentUser.getUid())
                                .setValue(userFriendCurrent4);
                        break;
                }
            }
        });

        if (userProfile.getAvatar().equals("")) {
            Picasso.with(context).load(R.drawable.ic_avatar).into(holder.ivAvatar);
        } else {
            Picasso.with(context).load(userProfile.getAvatar())
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
    }

    @Override
    public int getItemCount() {
        return listUserProfiles.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickname, tvEmail, tvSex;
        private ImageView ivAvatar;
        private Button btnMakeFriend;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            tvNickname = (TextView) itemView.findViewById(R.id.item_user_profile_tv_nickname);
            tvEmail = (TextView) itemView.findViewById(R.id.item_user_profile_tv_email);
            tvSex = (TextView) itemView.findViewById(R.id.item_user_profile_tv_sex);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_user_profile_iv_avatar);
            btnMakeFriend = (Button) itemView.findViewById(R.id.item_user_profile_btn_make_friend);
        }
    }
}