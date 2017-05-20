package com.dfa.vinatrip.MainFunction.Me.MeDetail.MakeFriend;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListUserProfileAdapter extends RecyclerView.Adapter<ListUserProfileAdapter.ProfileViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<UserProfile> userProfileList;
    private List<UserFriend> userFriendList;
    private DatabaseReference referenceFriend;
    private UserProfile currentUser;

    public ListUserProfileAdapter(Context context, List<UserProfile> userProfileList,
                                  List<UserFriend> userFriendList, DatabaseReference referenceFriend,
                                  UserProfile currentUser) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;

        this.userProfileList = userProfileList;
        for (int i = 0; i < userProfileList.size(); i++) {
            if (userProfileList.get(i).getUid().equals(currentUser.getUid())) {
                this.userProfileList.remove(i);
                break;
            }
        }

        this.userFriendList = userFriendList;
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
        final UserProfile userProfile = userProfileList.get(position);

        holder.tvNickname.setText(userProfile.getNickname());
        holder.tvEmail.setText(userProfile.getEmail());
        holder.tvSex.setText(userProfile.getSex());

        holder.btnMakeFriend.setText("Kết bạn");
        holder.btnMakeFriend.setTag("Kết bạn");
        holder.btnMakeFriend.setBackgroundResource(R.drawable.custom_button_neutral);

        // If one userProfile is in userFriendList of current user login
        for (int j = 0; j < userFriendList.size(); j++) {
            if (userProfile.getUid().equals(userFriendList.get(j).getFriendId())) {
                switch (userFriendList.get(j).getState()) {
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
                        holder.btnMakeFriend.setBackgroundResource(R.drawable.custom_button_positive);
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
                        holder.btnMakeFriend.setBackgroundResource(R.drawable.custom_button_neutral);
                        // Delete data from the current user login
                        referenceFriend.child("UserFriend")
                                .child(currentUser.getUid())
                                .child(userProfile.getUid())
                                .removeValue();
                        // Delete data from the user be requested
                        referenceFriend.child("UserFriend")
                                .child(userProfile.getUid())
                                .child(currentUser.getUid())
                                .removeValue();
                        break;

                    case "Đồng ý":
                        holder.btnMakeFriend.setText(R.string.friend);
                        holder.btnMakeFriend.setTag("Bạn bè");
                        holder.btnMakeFriend.setBackgroundResource(R.drawable.custom_button_positive);
                        // Add profile user request to the current user login
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
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Hủy kết bạn");
                        alertDialog.setMessage("Bạn có chắc chắn muốn hủy kết bạn với người này?");
                        alertDialog.setIcon(R.drawable.ic_notification);
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ĐỒNG Ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.btnMakeFriend.setText("Kết bạn");
                                holder.btnMakeFriend.setTag("Kết bạn");
                                holder.btnMakeFriend.setBackgroundResource(R.drawable.custom_button_neutral);
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
                            }
                        });
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "KHÔNG", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.show();
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
                    .into(holder.ivAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
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