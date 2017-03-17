package com.dfa.vinatrip.MainFunction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Location.LocationFragment;
import com.dfa.vinatrip.MainFunction.Location.LocationFragment_;
import com.dfa.vinatrip.MainFunction.Me.MeFragment;
import com.dfa.vinatrip.MainFunction.Me.MeFragment_;
import com.dfa.vinatrip.MainFunction.MyFriend.MyFriendFragment;
import com.dfa.vinatrip.MainFunction.MyFriend.MyFriendFragment_;
import com.dfa.vinatrip.MainFunction.Plan.PlanFragment;
import com.dfa.vinatrip.MainFunction.Plan.PlanFragment_;
import com.dfa.vinatrip.MainFunction.Share.ShareFragment;
import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private LocationFragment locationFragment;
    private PlanFragment planFragment;
    private ShareFragment shareFragment;
    private MyFriendFragment myFriendFragment;
    private MeFragment meFragment;
    private boolean doubleBackPress = false;
    private int selectedItemId;
    private Snackbar snackbar;

    @ViewById(R.id.activity_main_bnv_menu)
    BottomNavigationView bnvMenu;

    @AfterViews
    void onCreate() {
        changeColorStatusBar();

        // When more than 3 icons, ShiftMode happen, use this to back to normal
        StopShiftModeBottomNavView.disableShiftMode(bnvMenu);

        bnvMenu.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragment(item);
                        // Must true so item in bottom bar can transform
                        return true;
                    }
                });

        addNewFragments();

        // load fragment_location first
        selectFragment(bnvMenu.getMenu().getItem(0));

        if (!CheckNetwork.isNetworkConnected(MainActivity.this)) {
            snackbar = Snackbar.make(findViewById(R.id.activity_main),
                    "Không có kết nối Internet", Snackbar.LENGTH_LONG);
            View viewSnackbar = snackbar.getView();
            TextView tvSnackbar =
                    (TextView) viewSnackbar.findViewById(android.support.design.R.id.snackbar_text);
            tvSnackbar.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void selectFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iconLocation:
                getSupportFragmentManager().beginTransaction().show(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(myFriendFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(meFragment).commit();
                break;
            case R.id.iconPlan:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().show(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(myFriendFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(meFragment).commit();
                break;
            case R.id.iconShare:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().show(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(myFriendFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(meFragment).commit();
                break;
            case R.id.iconMemory:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().show(myFriendFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(meFragment).commit();
                break;
            case R.id.iconMe:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(myFriendFragment).commit();
                getSupportFragmentManager().beginTransaction().show(meFragment).commit();
                break;
        }
        selectedItemId = item.getItemId();
    }

    public void addNewFragments() {
        locationFragment = new LocationFragment_();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, locationFragment, "locationFragment")
                .commit();

        planFragment = new PlanFragment_();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, planFragment, "planFragment")
                .commit();

        shareFragment = new ShareFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, shareFragment, "shareFragment")
                .commit();

        myFriendFragment = new MyFriendFragment_();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, myFriendFragment, "myFriendFragment")
                .commit();

        meFragment = new MeFragment_();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, meFragment, "meFragment")
                .commit();
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = bnvMenu.getMenu().getItem(0);
        if (selectedItemId != homeItem.getItemId()) {
            selectFragment(homeItem);
            // if not, it will not back to home icon when press
            for (int i = 0; i < bnvMenu.getMenu().size(); i++) {
                MenuItem menuItem = bnvMenu.getMenu().getItem(i);
                menuItem.setChecked(menuItem.getItemId() == homeItem.getItemId());
            }
        } else {
            if (doubleBackPress) {
                super.onBackPressed();
            } else {
                doubleBackPress = true;
                Toast.makeText(MainActivity.this, "Nhấn BACK thêm một lần nữa để thoát", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPress = false;
                    }
                    // After 2 seconds, user have to press back twice again
                }, 2000);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}

