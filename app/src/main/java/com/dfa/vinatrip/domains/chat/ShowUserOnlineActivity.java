package com.dfa.vinatrip.domains.chat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.UserFriend;
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

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_show_user_online_lv_user)
    protected ListView lvUser;
    @ViewById(R.id.activity_show_user_online_tv_count_current)
    protected TextView tvCountCurrent;
    @ViewById(R.id.activity_show_user_online_tv_count_sum)
    protected TextView tvCountSum;

    private int countCurrent;
    private QuickAdapter<UserFriend> adapter;
    private ImageLoader imageLoader;

    @AfterViews
    public void init() {
        setupAppBar();

        imageLoader = ImageLoader.getInstance();

        adapter = new QuickAdapter<UserFriend>(this, R.layout.item_user_online) {
            @Override
            protected void convert(BaseAdapterHelper helper, UserFriend item) {
                ImageView ivIndicator = helper.getView(R.id.item_user_online_iv_indicator);
                CircleImageView civAvatar = helper.getView(R.id.item_user_online_vertical_civ_avatar);
                TextView tvNickname = helper.getView(R.id.item_user_online_vertical_tv_nickname);
                TextView tvEmail = helper.getView(R.id.item_user_online_vertical_tv_email);
                TextView tvType = helper.getView(R.id.item_user_online_tv_type);

                imageLoader.displayImage(item.getAvatar(), civAvatar);
                tvNickname.setText(item.getNickname());
                tvEmail.setText(item.getEmail());
                if (item.getIsOnline()) {
                    ivIndicator.setVisibility(View.VISIBLE);
                    tvType.setText("Online");
                    countCurrent++;
                } else {
                    ivIndicator.setVisibility(View.GONE);
                    tvType.setText("Offline");
                }
            }
        };
        lvUser.setAdapter(adapter);

        adapter.addAll(userFriendList);
        adapter.notifyDataSetChanged();

        tvCountSum.setText(String.valueOf(userFriendList.size()));
        tvCountCurrent.setText(String.valueOf(countCurrent));
    }

    public void setupAppBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Thông tin nhóm");

            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }
}
