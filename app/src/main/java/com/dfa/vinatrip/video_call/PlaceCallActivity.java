package com.dfa.vinatrip.video_call;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.dfa.vinatrip.models.response.user.User;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinch.android.rtc.calling.Call;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_place_call)
public class PlaceCallActivity extends BaseVideoCallActivity {

    @Extra
    protected Plan plan;
    @Extra
    protected User currentUser;

    @ViewById(R.id.activity_place_call_video_call_lv_user)
    protected ListView lvUser;

    private QuickAdapter<UserInPlan> adapter;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    @AfterViews
    public void init() {
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_avatar)
                .showImageForEmptyUri(R.drawable.ic_avatar)
                .showImageOnFail(R.drawable.ic_avatar)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();

        adapter = new QuickAdapter<UserInPlan>(this, R.layout.item_user_online) {
            @Override
            protected void convert(BaseAdapterHelper helper, UserInPlan item) {
                ImageView ivIndicator = helper.getView(R.id.item_user_online_iv_indicator);
                CircleImageView civAvatar = helper.getView(R.id.item_user_online_vertical_civ_avatar);
                TextView tvNickname = helper.getView(R.id.item_user_online_vertical_tv_nickname);
                TextView tvEmail = helper.getView(R.id.item_user_online_vertical_tv_email);
                TextView tvType = helper.getView(R.id.item_user_online_tv_type);

                imageLoader.displayImage(item.getAvatar(), civAvatar, imageOptions);
                if (item.getUsername().equals(currentUser.getUsername())) {
                    tvNickname.setText("Tôi");
                } else {
                    tvNickname.setText(item.getUsername());
                }
                tvEmail.setText(item.getEmail());
                if (item.getIsOnline() == 1 || item.getEmail().equals(currentUser.getEmail())) {
                    ivIndicator.setVisibility(View.VISIBLE);
                    tvType.setText("Online");
                } else {
                    ivIndicator.setVisibility(View.GONE);
                    tvType.setText("Offline");
                }
                LinearLayout llRoot = helper.getView(R.id.item_user_online_ll_root);
                llRoot.setOnClickListener(v -> {
                    callButtonClicked(item.getEmail());
                });
            }
        };
        lvUser.setAdapter(adapter);

        adapter.addAll(plan.getInvitedFriendList());
        adapter.notifyDataSetChanged();
    }

    // invoked when the connection with SinchServer is established
    @Override
    protected void onServiceConnected() {
        // Hide loading here
    }

    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }

    //to kill the current session of SinchService
    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    //to place the call to the entered name
    private void callButtonClicked(String username) {
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(username);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }
}
