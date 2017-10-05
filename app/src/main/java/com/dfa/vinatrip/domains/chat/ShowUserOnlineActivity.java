package com.dfa.vinatrip.domains.chat;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.me.detail_me.make_friend.UserFriend;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_show_user_online)
public class ShowUserOnlineActivity extends AppCompatActivity {

    @Extra
    protected ArrayList<UserFriend> userFriendList;

    @ViewById(R.id.activity_show_user_online_lv_user)
    protected ListView lvUser;

    private QuickAdapter<UserFriend> adapter;
    private ImageLoader imageLoader;

    @AfterViews
    public void init() {
        imageLoader = ImageLoader.getInstance();

        adapter = new QuickAdapter<UserFriend>(this, R.layout.item_user_online) {
            @Override
            protected void convert(BaseAdapterHelper helper, UserFriend item) {
                ImageView ivIndicator = helper.getView(R.id.item_user_online_iv_indicator);
                CircleImageView civAvatar = helper.getView(R.id.item_user_online_vertical_civ_avatar);
                TextView tvNickname = helper.getView(R.id.item_user_online_vertical_tv_nickname);
                TextView tvEmail = helper.getView(R.id.item_user_online_vertical_tv_email);

                imageLoader.displayImage(item.getAvatar(), civAvatar);
                tvNickname.setText(item.getNickname());
                tvEmail.setText(item.getEmail());
                if (item.getIsOnline()) {
                    ivIndicator.setVisibility(View.VISIBLE);
                } else {
                    ivIndicator.setVisibility(View.GONE);
                }
            }
        };
        lvUser.setAdapter(adapter);

        adapter.addAll(userFriendList);
        adapter.notifyDataSetChanged();
    }
}
