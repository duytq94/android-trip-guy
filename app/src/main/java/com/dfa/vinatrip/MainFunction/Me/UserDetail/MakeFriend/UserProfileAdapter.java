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
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseUser firebaseUser;

    public UserProfileAdapter(Context context,
                              List<UserProfile> listUserProfiles,
                              List<UserFriend> listUserFriends,
                              DatabaseReference referenceFriend,
                              FirebaseUser firebaseUser,
                              SwipeRefreshLayout srlReload) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.srlReload = srlReload;

        this.listUserProfiles = listUserProfiles;
        for (int i = 0; i < listUserProfiles.size(); i++) {
            if (listUserProfiles.get(i).getUid().equals(firebaseUser.getUid())) {
                this.listUserProfiles.remove(i);
                break;
            }
        }

        this.listUserFriends = listUserFriends;
        this.referenceFriend = referenceFriend;
        this.firebaseUser = firebaseUser;
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
        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorGray));

        // If one userProfile is in listUserFriends of current user login
        for (int j = 0; j < listUserFriends.size(); j++) {
            if (userProfile.getUid().equals(listUserFriends.get(j).getFriendId())) {
                switch (listUserFriends.get(j).getState()) {
                    case "requested":
                        holder.btnMakeFriend.setText("Đã gửi");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        break;
                    case "beRequested":
                        holder.btnMakeFriend.setText("Đồng ý");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        break;
                    case "friend":
                        holder.btnMakeFriend.setText("Bạn bè");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorMain));
                        break;
                }
            }
        }
        holder.btnMakeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.btnMakeFriend.getText().toString()) {
                    case "Đã gửi":
                        holder.btnMakeFriend.setText("Kết bạn");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
                        // Delete data to the current user login
                        referenceFriend.child("UserFriend")
                                .child(firebaseUser.getUid())
                                .child(userProfile.getUid())
                                .removeValue();
                        // Delete data to the user be requested
                        referenceFriend.child("UserFriend")
                                .child(userProfile.getUid())
                                .child(firebaseUser.getUid())
                                .removeValue();
                        break;

                    case "Đồng ý":
                        holder.btnMakeFriend.setText("Bạn bè");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorMain));
                        // Add data to the current user login
                        UserFriend userFriendBeRequested = new UserFriend(userProfile.getUid(), "friend");
                        referenceFriend.child("UserFriend")
                                .child(firebaseUser.getUid())
                                .child(userProfile.getUid())
                                .setValue(userFriendBeRequested);
                        // Add data to the user be requested
                        UserFriend userFriendCurrent = new UserFriend(firebaseUser.getUid(), "friend");
                        referenceFriend.child("UserFriend")
                                .child(userProfile.getUid())
                                .child(firebaseUser.getUid())
                                .setValue(userFriendCurrent);
                        break;

                    case "Bạn bè":
                        holder.btnMakeFriend.setText("Kết bạn");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
                        // Delete data to the current user login
                        referenceFriend.child("UserFriend")
                                .child(firebaseUser.getUid())
                                .child(userProfile.getUid())
                                .removeValue();
                        // Delete data to the user be requested
                        referenceFriend.child("UserFriend")
                                .child(userProfile.getUid())
                                .child(firebaseUser.getUid())
                                .removeValue();
                        break;

                    case "Kết bạn":
                        holder.btnMakeFriend.setText("Đã gửi");
                        holder.btnMakeFriend.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        // Add data to the current user login
                        UserFriend userFriendBeRequested4 = new UserFriend(userProfile.getUid(), "requested");
                        referenceFriend.child("UserFriend")
                                .child(firebaseUser.getUid())
                                .child(userProfile.getUid())
                                .setValue(userFriendBeRequested4);
                        // Add data to the user be requested
                        UserFriend userFriendCurrent4 = new UserFriend(firebaseUser.getUid(), "beRequested");
                        referenceFriend.child("UserFriend")
                                .child(userProfile.getUid())
                                .child(firebaseUser.getUid())
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