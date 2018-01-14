package com.dfa.vinatrip.domains.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.ExitDialog;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.custom_view.NonSwipeViewPager;
import com.dfa.vinatrip.domains.main.adapter.MainPagerAdapter;
import com.dfa.vinatrip.domains.main.fragment.deal.DealFragment_;
import com.dfa.vinatrip.domains.main.fragment.me.MeFragment_;
import com.dfa.vinatrip.domains.main.fragment.plan.PlanFragment_;
import com.dfa.vinatrip.domains.main.fragment.province.ProvinceFragment_;
import com.dfa.vinatrip.domains.main.fragment.trend.TrendFragment_;
import com.dfa.vinatrip.utils.StopShiftModeBottomNavView;
import com.dfa.vinatrip.video_call.BaseVideoCallActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.dfa.vinatrip.utils.Constants.REQUEST_PERMISSION_VIDEO_CALL;
import static com.dfa.vinatrip.utils.Constants.SIGN_OUT_VIDEO_CALL;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseVideoCallActivity {

    @ViewById(R.id.activity_main_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_main_nsvp_viewpager)
    protected NonSwipeViewPager vpFragment;
    @ViewById(R.id.activity_main_bnv_menu)
    protected BottomNavigationView bnvMenu;

    private AlertDialog dialogInfo;
    private ExitDialog exitDialog;

    @AfterViews
    public void init() {
        requestPermission();

        exitDialog = new ExitDialog(this,
                closeListener -> exitDialog.dismiss(),
                sendListener -> finish());

        nToolbar.setup(this, "TripGuy");
        nToolbar.showAppIcon();
        nToolbar.showToolbarColor();
        nToolbar.showRightIcon();

        List<Fragment> arrayFragment = new ArrayList<>();
        arrayFragment.add(ProvinceFragment_.builder().build());
        arrayFragment.add(TrendFragment_.builder().build());
        arrayFragment.add(PlanFragment_.builder().build());
        arrayFragment.add(DealFragment_.builder().build());
        arrayFragment.add(MeFragment_.builder().build());

        vpFragment.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), arrayFragment));
        vpFragment.setOffscreenPageLimit(arrayFragment.size());

        dialogInfo = new AlertDialog.Builder(this).create();
        dialogInfo.setIcon(R.drawable.ic_symbol);
        dialogInfo.setButton(DialogInterface.BUTTON_POSITIVE, "XONG", (dialogInterface, i) -> {
            dialogInfo.dismiss();
        });

        nToolbar.setOnRightClickListener(v -> {
            switch (vpFragment.getCurrentItem()) {
                case 0:
                    dialogInfo.setTitle("Tìm địa điểm");
                    dialogInfo.setMessage(getString(R.string.message_province));
                    break;
                case 1:
                    dialogInfo.setTitle("Nơi nào hot?");
                    dialogInfo.setMessage(getString(R.string.message_trend));
                    break;
                case 2:
                    dialogInfo.setTitle("Lập kế hoạch");
                    dialogInfo.setMessage(getString(R.string.message_plan));
                    break;
                case 3:
                    dialogInfo.setTitle("Tìm deal và book vé");
                    dialogInfo.setMessage(getString(R.string.message_deal));
                    break;
                case 4:
                    dialogInfo.setTitle("Quản lý cá nhân");
                    dialogInfo.setMessage(getString(R.string.message_personal));
                    break;
            }
            dialogInfo.show();
        });

        StopShiftModeBottomNavView.disableShiftMode(bnvMenu);
        bnvMenu.setOnNavigationItemSelectedListener
                (item -> {
                    switch (item.getItemId()) {
                        case R.id.iconLocation:
                            vpFragment.setCurrentItem(0, false);
                            break;
                        case R.id.iconTrend:
                            vpFragment.setCurrentItem(1, false);
                            break;
                        case R.id.iconPlan:
                            vpFragment.setCurrentItem(2, false);
                            break;
                        case R.id.iconDeal:
                            vpFragment.setCurrentItem(3, false);
                            break;
                        case R.id.iconMe:
                            vpFragment.setCurrentItem(4, false);
                            break;
                    }
                    // Must true so item in bottom bar can transform
                    return true;
                });
    }

    @Override
    public void onBackPressed() {
        exitDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_VIDEO_CALL);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_VIDEO_CALL) {
            if (grantResults.length != 3 || grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bạn đã không cấp quyền, một số chức năng có thể không hoạt động", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(String event) {
        if (getSinchServiceInterface() != null && event.equals(SIGN_OUT_VIDEO_CALL)) {
            getSinchServiceInterface().stopClient();
        }
    }
}

