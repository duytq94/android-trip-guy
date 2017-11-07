package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.dfa.vinatrip.models.response.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.InviteViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<User> friendList;
    private List<UserInPlan> invitedFriendList;

    public InviteFriendAdapter(Context context, List<User> friendList,
                               List<UserInPlan> invitedFriendList, Plan currentPlan) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.friendList = friendList;
        this.invitedFriendList = invitedFriendList;
    }

    @Override
    public InviteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_user_profile, parent, false);
        return new InviteFriendAdapter.InviteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InviteViewHolder holder, int position) {
        final User friend = friendList.get(position);

        if (friend.getUsername() != null) {
            holder.tvNickname.setText(friend.getUsername());
        }
        holder.tvEmail.setText(friend.getEmail());
        holder.tvSex.setText(friend.getStringSex());

        holder.btnAction.setText("Mời");
        holder.btnAction.setTag("Mời");
        holder.btnAction.setBackgroundResource(R.drawable.btn_neutral);
        holder.btnAction.setOnClickListener(v -> {
            switch (holder.btnAction.getTag().toString()) {
                case "Mời":
                    holder.btnAction.setText(R.string.invited);
                    holder.btnAction.setTag("Đã mời");
                    holder.btnAction.setBackgroundResource(R.drawable.btn_positive);
                    invitedFriendList.add(new UserInPlan(friend.getId(), friend.getEmail(),
                            friend.getUsername(), friend.getAvatar()));
                    break;

                case "Đã mời":
                    holder.btnAction.setText("Mời");
                    holder.btnAction.setTag("Mời");
                    holder.btnAction.setBackgroundResource(R.drawable.btn_neutral);
                    for (int i = 0; i < invitedFriendList.size(); i++) {
                        if (invitedFriendList.get(i).getId() == friend.getId()) {
                            invitedFriendList.remove(i);
                            break;
                        }
                    }
                    break;
            }
        });

        if (friend.getAvatar() == null) {
            Picasso.with(context).load(R.drawable.ic_avatar).into(holder.ivAvatar);
        } else {
            Picasso.with(context).load(friend.getAvatar())
                    .error(R.drawable.ic_avatar)
                    .into(holder.ivAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class InviteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickname, tvEmail, tvSex;
        private ImageView ivAvatar;
        private Button btnAction;

        public InviteViewHolder(View itemView) {
            super(itemView);
            tvNickname = (TextView) itemView.findViewById(R.id.item_user_profile_tv_nickname);
            tvEmail = (TextView) itemView.findViewById(R.id.item_user_profile_tv_email);
            tvSex = (TextView) itemView.findViewById(R.id.item_user_profile_tv_sex);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_user_profile_iv_avatar);
            btnAction = (Button) itemView.findViewById(R.id.item_user_profile_btn_action);
        }
    }
}


