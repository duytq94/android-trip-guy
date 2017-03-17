package com.dfa.vinatrip.MainFunction.Plan.MakePlan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.InviteViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<UserFriend> userFriendList;
    private List<String> invitedFriendIdList;

    public InviteFriendAdapter(Context context, List<UserFriend> userFriendList, List<String> invitedFriendIdList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.userFriendList = userFriendList;
        this.invitedFriendIdList = invitedFriendIdList;
    }

    @Override
    public InviteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_user_profile, parent, false);
        return new InviteFriendAdapter.InviteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InviteViewHolder holder, int position) {
        final UserFriend userFriend = userFriendList.get(position);

        holder.tvNickname.setText(userFriend.getNickname());
        holder.tvEmail.setText(userFriend.getEmail());

        holder.btnMakeFriend.setText("Mời");
        holder.btnMakeFriend.setTag("Mời");
        holder.btnMakeFriend.setBackgroundResource(R.drawable.custom_button_negative);
        holder.btnMakeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.btnMakeFriend.getTag().toString()) {
                    case "Mời":
                        holder.btnMakeFriend.setText(R.string.invited);
                        holder.btnMakeFriend.setTag("Đã mời");
                        holder.btnMakeFriend.setBackgroundResource(R.drawable.custom_button_positive);
                        invitedFriendIdList.add(userFriend.getFriendId());
                        break;

                    case "Đã mời":
                        holder.btnMakeFriend.setText("Mời");
                        holder.btnMakeFriend.setTag("Mời");
                        holder.btnMakeFriend.setBackgroundResource(R.drawable.custom_button_negative);
                        for (int i = 0; i < invitedFriendIdList.size(); i++) {
                            if (invitedFriendIdList.get(i).equals(userFriend.getFriendId())) {
                                invitedFriendIdList.remove(i);
                                break;
                            }
                        }

                        break;
                }
            }
        });

        if (userFriend.getAvatar().equals("")) {
            Picasso.with(context).load(R.drawable.ic_avatar).into(holder.ivAvatar);
        } else {
            Picasso.with(context).load(userFriend.getAvatar())
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.photo_not_available)
                    .into(holder.ivAvatar,
                            new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {
                                }
                            });
        }
    }

    @Override
    public int getItemCount() {
        return userFriendList.size();
    }

    public static class InviteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickname, tvEmail, tvSex;
        private ImageView ivAvatar;
        private Button btnMakeFriend;

        public InviteViewHolder(View itemView) {
            super(itemView);
            tvNickname = (TextView) itemView.findViewById(R.id.item_user_profile_tv_nickname);
            tvEmail = (TextView) itemView.findViewById(R.id.item_user_profile_tv_email);
            tvSex = (TextView) itemView.findViewById(R.id.item_user_profile_tv_sex);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_user_profile_iv_avatar);
            btnMakeFriend = (Button) itemView.findViewById(R.id.item_user_profile_btn_make_friend);
        }
    }
}


