package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_friend;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.utils.AdapterUserListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.dfa.vinatrip.utils.Constants.CANCEL_FRIEND;

public class ListFriendAdapter extends RecyclerView.Adapter<ListFriendAdapter.ProfileViewHolder> {

    private List<User> friendList;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;
    private AdapterUserListener adapterUserListener;

    public ListFriendAdapter(AdapterUserListener adapterUserListener) {
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
        this.adapterUserListener = adapterUserListener;
    }

    public void setListFriend(List<User> friendList) {
        this.friendList = friendList;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        User user = friendList.get(position);

        holder.llRoot.setVisibility(View.VISIBLE);
        holder.tvNickname.setText(user.getUsername());
        holder.tvEmail.setText(user.getEmail());
        if (user.getAvatar() != null) {
            imageLoader.displayImage(user.getAvatar(), holder.ivAvatar, imageOptions);
        }
        holder.tvSex.setText(user.getStringSex());
        holder.btnAction.setText(R.string.friend);
        holder.btnAction.setTag(CANCEL_FRIEND);
        holder.btnAction.setOnClickListener(v -> {
            adapterUserListener.onBtnActionClick(position, holder.btnAction.getTag().toString());
        });
    }

    @Override
    public int getItemCount() {
        if (friendList != null) {
            return friendList.size();
        }
        return 0;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNickname, tvEmail, tvSex;
        private ImageView ivAvatar;
        private Button btnAction;
        private LinearLayout llRoot;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            tvNickname = (TextView) itemView.findViewById(R.id.item_user_profile_tv_nickname);
            tvEmail = (TextView) itemView.findViewById(R.id.item_user_profile_tv_email);
            tvSex = (TextView) itemView.findViewById(R.id.item_user_profile_tv_sex);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_user_profile_iv_avatar);
            btnAction = (Button) itemView.findViewById(R.id.item_user_profile_btn_action);
            llRoot = (LinearLayout) itemView.findViewById(R.id.item_user_profile_ll_root);
        }
    }
}