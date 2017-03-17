package com.dfa.vinatrip.MainFunction.Me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.BuildConfig;
import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.Login.SignInActivity_;
import com.dfa.vinatrip.MainFunction.MainActivity_;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.UserProfileDetailActivity_;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.SplashScreen.DataService;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

@EFragment(R.layout.fragment_me)
public class MeFragment extends Fragment {

    static final int NOTIFY_UPDATE_REQUEST = 1;

    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_me_tv_nickname)
    TextView tvNickname;

    @ViewById(R.id.fragment_me_tv_city)
    TextView tvCity;

    @ViewById(R.id.fragment_me_tv_app_info)
    TextView tvAppInfo;

    @ViewById(R.id.fragment_me_tv_birthday)
    TextView tvBirthday;

    @ViewById(R.id.fragment_me_tv_introduce_your_self)
    TextView tvIntroduceYourSelf;

    @ViewById(R.id.fragment_me_tv_sex)
    TextView tvSex;

    @ViewById(R.id.fragment_me_tv_friend_not_available)
    TextView tvFriendNotAvailable;

    @ViewById(R.id.fragment_me_tv_make_friend)
    TextView tvMakeFriend;

    @ViewById(R.id.fragment_me_tv_email)
    TextView tvEmail;

    @ViewById(R.id.fragment_me_iv_avatar)
    ImageView ivAvatar;

    @ViewById(R.id.fragment_me_iv_blur_avatar)
    ImageView ivBlurAvatar;

    @ViewById(R.id.fragment_me_ll_sign_in)
    LinearLayout llSignIn;

    @ViewById(R.id.fragment_me_ll_sign_out)
    LinearLayout llSignOut;

    @ViewById(R.id.fragment_me_ll_info)
    LinearLayout llInfo;

    @ViewById(R.id.fragment_me_ll_my_friends)
    LinearLayout llMyFriends;

    @ViewById(R.id.fragment_me_ll_settings)
    LinearLayout llSettings;

    @ViewById(R.id.fragment_me_ll_update_profile)
    LinearLayout llUpdateProfile;

    @ViewById(R.id.fragment_me_srlReload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_me_rv_list_friends)
    RecyclerView rvListFriends;

    private UserProfile currentUser;
    private List<UserProfile> listUserProfiles;
    private List<UserFriend> listUserFriends;
    private FriendAdapter friendAdapter;

    @AfterViews
    void onCreateView() {
        showAppInfo();
        srlReload.setColorSchemeResources(R.color.colorMain);

        if (CheckNetwork.isNetworkConnected(getActivity())) {
            onClickListener();
            setContentViews();
            hideViews(false);
        } else {
            hideViews(true);
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    onClickListener();
                    setContentViews();
                    hideViews(false);
                } else srlReload.setRefreshing(false);
            }
        });
    }

    public void hideViews(Boolean aBoolean) {
        if (aBoolean) {
            llInfo.setVisibility(View.GONE);
            llMyFriends.setVisibility(View.GONE);
            llSettings.setVisibility(View.GONE);
        } else {
            llInfo.setVisibility(View.VISIBLE);
            llMyFriends.setVisibility(View.VISIBLE);
            llSettings.setVisibility(View.VISIBLE);
        }
    }

    public void showAppInfo() {
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

        tvAppInfo.setText("TripGuy - giúp bạn có chuyến đi trọn vẹn nhất!\n");
        tvAppInfo.append("Version name: " + versionName + "\n");
        tvAppInfo.append("Version code: " + versionCode + "\n");
    }

    public void setContentViews() {
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

        tvAppInfo.setText("TripGuy - giúp bạn có chuyến đi trọng vẹn nhất!\n");
        tvAppInfo.append("Version name: " + versionName + "\n");
        tvAppInfo.append("Version code: " + versionCode + "\n");

        currentUser = dataService.getCurrentUser();
        if (currentUser != null) {
            listUserProfiles = new ArrayList<>();
            listUserFriends = new ArrayList<>();

            // Wait until list user profile load done
            tvMakeFriend.setEnabled(false);


            listUserProfiles.addAll(dataService.getUserProfileList());
            if (!currentUser.getNickname().equals("")) {
                tvNickname.setText(currentUser.getNickname());
            }
            if (!currentUser.getCity().equals("")) {
                tvCity.setText(currentUser.getCity());
            }
            if (!currentUser.getAvatar().isEmpty()) {
                Picasso.with(getActivity())
                        .load(currentUser.getAvatar())
                        .into(target);
            }
            tvIntroduceYourSelf.setText(currentUser.getIntroduceYourSelf());
            tvBirthday.setText(currentUser.getBirthday());
            tvEmail.setText(currentUser.getEmail());
            tvSex.setText(currentUser.getSex());

            dataService.setOnChangeUserFriendList(new DataService.OnChangeUserFriendList() {
                @Override
                public void onAddItem() {
                    listUserFriends.clear();
                    listUserFriends.addAll(dataService.getUserFriendList());
                    tvMakeFriend.setEnabled(true);
                    friendAdapter = new FriendAdapter(getActivity(), listUserFriends,
                            tvFriendNotAvailable, srlReload);
                    rvListFriends.setAdapter(friendAdapter);
                }

                @Override
                public void onRemoveItem() {
                    listUserFriends.clear();
                    listUserFriends.addAll(dataService.getUserFriendList());
                    tvMakeFriend.setEnabled(true);
                    friendAdapter = new FriendAdapter(getActivity(), listUserFriends,
                            tvFriendNotAvailable, srlReload);
                    rvListFriends.setAdapter(friendAdapter);
                }
            });

            listUserFriends.addAll(dataService.getUserFriendList());
            tvMakeFriend.setEnabled(true);
            friendAdapter = new FriendAdapter(getActivity(), listUserFriends,
                    tvFriendNotAvailable, srlReload);
            rvListFriends.setAdapter(friendAdapter);

            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            rvListFriends.setLayoutManager(staggeredGridLayoutManager);
        } else {
            srlReload.setRefreshing(false);
        }
    }

    public void onClickListener() {
        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    startActivity(new Intent(getActivity(), SignInActivity_.class));
                } else {
                    Toast.makeText(getActivity(), "Bạn đã đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    FirebaseAuth.getInstance().signOut();
                    dataService.clearData();
                    startActivity(new Intent(getActivity(), MainActivity_.class));
                } else {
                    Toast.makeText(getActivity(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    Intent intentUpdate = new Intent(getActivity(), UserProfileDetailActivity_.class);

                    // Send UserProfile to UserProfileDetailActivity
                    intentUpdate.putExtra("UserProfile", currentUser);

                    // Send ListUserProfiles to UserProfileDetailActivity
                    intentUpdate.putExtra("ListUserProfiles", (Serializable) listUserProfiles);

                    // Send notify to inform that llUpdateProfile be clicked
                    String fromView = "llUpdateProfile";
                    intentUpdate.putExtra("FromView", fromView);

                    // Make UserProfileDetailActivity notify when it finish
                    startActivityForResult(intentUpdate, NOTIFY_UPDATE_REQUEST);
                } else {
                    Toast.makeText(getActivity(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvMakeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    Intent intentUpdate = new Intent(getActivity(), UserProfileDetailActivity_.class);

                    // Send UserProfile to UserProfileDetailActivity
                    intentUpdate.putExtra("UserProfile", currentUser);

                    // Send ListUserProfiles to UserProfileDetailActivity
                    intentUpdate.putExtra("ListUserProfiles", (Serializable) listUserProfiles);

                    // Send notify to inform that tvMakeFriend be clicked
                    String fromView = "tvMakeFriend";
                    intentUpdate.putExtra("FromView", fromView);

                    // Make UserProfileDetailActivity notify when it finish
                    startActivityForResult(intentUpdate, NOTIFY_UPDATE_REQUEST);
                } else {
                    Toast.makeText(getActivity(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int currentUserCode, Intent data) {
        super.onActivityResult(requestCode, currentUserCode, data);
        // Reload data
        if (requestCode == NOTIFY_UPDATE_REQUEST) {
            if (CheckNetwork.isNetworkConnected(getActivity())) {
                setContentViews();
                hideViews(false);
            } else {
                hideViews(true);
            }
        }
    }

    // Note that to use bitmap, have to create variable target out of .into()
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(10)
                    .from(bitmap).into(ivBlurAvatar);
            ivAvatar.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}
