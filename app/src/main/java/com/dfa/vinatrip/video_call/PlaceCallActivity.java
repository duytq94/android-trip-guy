package com.dfa.vinatrip.video_call;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.dfa.vinatrip.utils.Constants;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.hawk.Hawk;
import com.sinch.android.rtc.calling.Call;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dfa.vinatrip.utils.Constants.REQUEST_PERMISSION_VIDEO_CALL;

@EActivity(R.layout.activity_place_call)
public class PlaceCallActivity extends BaseVideoCallActivity {

    @Extra
    protected Plan plan;

    protected User currentUser;

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_place_call_lv_user)
    protected ListView lvUser;

    private QuickAdapter<UserInPlan> adapter;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    @AfterViews
    public void init() {
        requestPermission();
    }

    public void setup() {
        currentUser = Hawk.get(Constants.KEY_USER_AUTH);

        setupAppBar();

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
                LinearLayout llRoot = helper.getView(R.id.item_user_online_ll_root);

                imageLoader.displayImage(item.getAvatar(), civAvatar, imageOptions);
                if (item.getUsername().equals(currentUser.getUsername())) {
                    llRoot.setVisibility(View.GONE);
                } else {
                    llRoot.setVisibility(View.VISIBLE);
                    tvNickname.setText(item.getUsername());
                }
                tvEmail.setText(item.getEmail());

                ivIndicator.setVisibility(View.GONE);
                tvType.setVisibility(View.GONE);

                llRoot.setOnClickListener(v -> {
                    callSomeone(item);
                });
            }
        };
        lvUser.setAdapter(adapter);

        Plan planClone = plan;
        filterUser(planClone);
        adapter.addAll(planClone.getInvitedFriendList());
        adapter.notifyDataSetChanged();
    }

    public void filterUser(Plan planClone) {
        // Remove current user
        for (int i = planClone.getInvitedFriendList().size() - 1; i >= 0; i--) {
            if (planClone.getInvitedFriendList().get(i).getEmail().equals(currentUser.getEmail())) {
                planClone.getInvitedFriendList().remove(i);
                break;
            }
        }
    }

    // invoked when the connection with SinchServer is established
    @Override
    protected void onServiceConnected() {
        // Hide loading here
    }

    //to kill the current session of SinchService
    @Click(R.id.activity_place_call_ll_stop)
    public void onBtnCallStopClick() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    //to place the call to the entered name
    public void callSomeone(UserInPlan remoteUser) {
        if (remoteUser.getEmail().isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu cho user này", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(remoteUser.getEmail());
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra("remoteUser", remoteUser);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

    public void setupAppBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Gọi thoại");

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

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_VIDEO_CALL);
            } else {
                setup();
            }
        } else {
            setup();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_VIDEO_CALL) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                setup();
            } else {
                Toast.makeText(this, "Bạn đã không cấp quyền, một số chức năng có thể không hoạt động", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
