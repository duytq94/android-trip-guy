package com.dfa.vinatrip.domains.main.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfa.vinatrip.BuildConfig;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.auth.sign_in.SignInActivity_;
import com.dfa.vinatrip.domains.main.me.detail_me.UserProfileDetailActivity_;
import com.dfa.vinatrip.domains.main.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.province.each_item_detail_province.rating.UserRating;
import com.dfa.vinatrip.domains.main.splash.SplashScreenActivity_;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.TripGuyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

import static com.dfa.vinatrip.utils.TripGuyUtils.REQUEST_UPDATE_INFO;

@EFragment(R.layout.fragment_me)
public class MeFragment extends Fragment {

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

    @ViewById(R.id.fragment_me_rl_login)
    RelativeLayout rlLogin;

    @ViewById(R.id.fragment_me_rl_not_login)
    RelativeLayout rlNotLogin;

    @ViewById(R.id.fragment_me_tv_rating_not_available)
    TextView tvRatingNotAvailable;

    @ViewById(R.id.fragment_me_ll_my_rating1)
    LinearLayout llMyRating1;
    @ViewById(R.id.fragment_me_civ_location1)
    CircleImageView civLocation1;
    @ViewById(R.id.fragment_me_tv_name_location1)
    TextView tvNameLocation1;
    @ViewById(R.id.fragment_me_tv_content1)
    TextView tvContent1;

    @ViewById(R.id.fragment_me_ll_my_rating2)
    LinearLayout llMyRating2;
    @ViewById(R.id.fragment_me_civ_location2)
    CircleImageView civLocation2;
    @ViewById(R.id.fragment_me_tv_name_location2)
    TextView tvNameLocation2;
    @ViewById(R.id.fragment_me_tv_content2)
    TextView tvContent2;

    @ViewById(R.id.fragment_me_ll_my_friend1)
    LinearLayout llMyFriend1;
    @ViewById(R.id.fragment_me_civ_friend_avatar1)
    CircleImageView civAvatar1;
    @ViewById(R.id.fragment_me_tv_friend_nickname1)
    TextView tvNickname1;
    @ViewById(R.id.fragment_me_tv_friend_email1)
    TextView tvEmail1;

    @ViewById(R.id.fragment_me_ll_my_friend2)
    LinearLayout llMyFriend2;
    @ViewById(R.id.fragment_me_civ_friend_avatar2)
    CircleImageView civAvatar2;
    @ViewById(R.id.fragment_me_tv_friend_nickname2)
    TextView tvNickname2;
    @ViewById(R.id.fragment_me_tv_friend_email2)
    TextView tvEmail2;

    private List<UserFriend> listUserFriends;
    private List<UserRating> myRatingList;

    @AfterViews
    void init() {
        dataService.getCurrentUser();

        showAppInfo();
        srlReload.setColorSchemeResources(R.color.colorMain);

        if (TripGuyUtils.isNetworkConnected(getActivity())) {
            if (dataService.getCurrentUser() != null) {
                initView();
            } else {
                rlLogin.setVisibility(View.GONE);
                rlNotLogin.setVisibility(View.VISIBLE);
            }
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    if (dataService.getCurrentUser() != null) {
                        initView();
                    }
                    srlReload.setRefreshing(false);
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });
    }

    public void showAppInfo() {
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

        tvAppInfo.setText("TripGuy - giúp bạn có chuyến đi trọn vẹn nhất!\n");
        tvAppInfo.append("Version name: " + versionName + "\n");
        tvAppInfo.append("Version code: " + versionCode + "\n");
    }

    public void initView() {
        rlLogin.setVisibility(View.VISIBLE);
        rlNotLogin.setVisibility(View.GONE);

        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

        tvAppInfo.setText("TripGuy - giúp bạn có chuyến đi trọng vẹn nhất!\n");
        tvAppInfo.append("Version name: " + versionName + "\n");
        tvAppInfo.append("Version code: " + versionCode + "\n");

        listUserFriends = new ArrayList<>();
        myRatingList = new ArrayList<>();

        initLlMyFriend();
        dataService.setOnChangeUserFriendList(new DataService.OnChangeUserFriendList() {
            @Override
            public void onAddFriend() {
                initLlMyFriend();
            }

            @Override
            public void onRemoveFriend() {
                initLlMyFriend();
            }
        });

        initLlMyRating();
        dataService.setOnChangeMyRatingList(new DataService.OnChangeMyRatingList() {
            @Override
            public void onUpdateRating() {
                initLlMyRating();
            }

            @Override
            public void onAddRating() {
                initLlMyRating();
            }
        });

        initFlInfor();
        dataService.setOnChangeCurrentUser(new DataService.OnChangeCurrentUser() {
            @Override
            public void onUpdateInfor() {
                initFlInfor();
            }
        });

        tvIntroduceYourSelf.setText(dataService.getCurrentUser().getIntroduceYourSelf());
        tvBirthday.setText(dataService.getCurrentUser().getBirthday());
        tvEmail.setText(dataService.getCurrentUser().getEmail());
        tvSex.setText(dataService.getCurrentUser().getSex());
    }

    public void initLlMyFriend() {
        listUserFriends.clear();
        listUserFriends.addAll(TripGuyUtils.filterListFriends(dataService.getUserFriendList()));
        UserFriend userFriend;
        switch (listUserFriends.size()) {
            case 0:
                tvFriendNotAvailable.setVisibility(View.VISIBLE);
                llMyFriend1.setVisibility(View.GONE);
                llMyFriend2.setVisibility(View.GONE);
                break;
            case 1:
                tvFriendNotAvailable.setVisibility(View.GONE);
                userFriend = listUserFriends.get(0);
                llMyFriend1.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(userFriend.getAvatar()).into(civAvatar1);
                tvNickname1.setText(userFriend.getNickname());
                tvEmail1.setText(userFriend.getEmail());
                llMyFriend2.setVisibility(View.GONE);
                break;
            default:
                tvFriendNotAvailable.setVisibility(View.GONE);

                userFriend = listUserFriends.get(0);
                llMyFriend1.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(userFriend.getAvatar()).into(civAvatar1);
                tvNickname1.setText(userFriend.getNickname());
                tvEmail1.setText(userFriend.getEmail());

                userFriend = listUserFriends.get(1);
                llMyFriend2.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(userFriend.getAvatar()).into(civAvatar2);
                tvNickname2.setText(userFriend.getNickname());
                tvEmail2.setText(userFriend.getEmail());

                break;
        }
    }

    public void initLlMyRating() {
        myRatingList.clear();
        myRatingList.addAll(dataService.getMyRatingList());
        UserRating myRating;
        switch (myRatingList.size()) {
            case 0:
                tvRatingNotAvailable.setVisibility(View.VISIBLE);
                llMyRating1.setVisibility(View.GONE);
                llMyRating2.setVisibility(View.GONE);
                break;
            case 1:
                tvRatingNotAvailable.setVisibility(View.GONE);
                myRating = myRatingList.get(0);
                llMyRating1.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(myRating.getLocationPhoto()).into(civLocation1);
                tvNameLocation1.setText(myRating.getLocationName());
                tvContent1.setText(myRating.getContent());
                llMyRating2.setVisibility(View.GONE);
                break;
            default:
                tvFriendNotAvailable.setVisibility(View.GONE);

                tvRatingNotAvailable.setVisibility(View.GONE);
                myRating = myRatingList.get(0);
                llMyRating1.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(myRating.getLocationPhoto()).into(civLocation1);
                tvNameLocation1.setText(myRating.getLocationName());
                tvContent1.setText(myRating.getContent());

                tvRatingNotAvailable.setVisibility(View.GONE);
                myRating = myRatingList.get(1);
                llMyRating2.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(myRating.getLocationPhoto()).into(civLocation2);
                tvNameLocation2.setText(myRating.getLocationName());
                tvContent2.setText(myRating.getContent());

                break;
        }
    }

    public void initFlInfor() {
        if (!dataService.getCurrentUser().getNickname().equals("")) {
            tvNickname.setText(dataService.getCurrentUser().getNickname());
        }
        if (!dataService.getCurrentUser().getCity().equals("")) {
            tvCity.setText(dataService.getCurrentUser().getCity());
        }
        if (!dataService.getCurrentUser().getAvatar().isEmpty()) {
            Picasso.with(getActivity())
                   .load(dataService.getCurrentUser().getAvatar())
                   .into(target);
        }
    }

    @Click(R.id.fragment_me_ll_sign_out)
    void onLlSignOutClick() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Đăng xuất");
        alertDialog.setMessage("Bạn có chắc chắn muốn đăng xuất tài khoản?");
        alertDialog.setIcon(R.drawable.ic_notification);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ĐỒNG Ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                dataService.clearData();
                startActivity(new Intent(getActivity(), SplashScreenActivity_.class));
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "KHÔNG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    @Click(R.id.fragment_me_ll_update_profile)
    void onLlUpdateProfileClick() {
        Intent intentUpdate = new Intent(getActivity(), UserProfileDetailActivity_.class);

        // Send notify to inform that llUpdateProfile be clicked
        String fromView = "llUpdateProfile";
        intentUpdate.putExtra("FromView", fromView);

        // Make UserProfileDetailActivity notify when it finish
        startActivityForResult(intentUpdate, REQUEST_UPDATE_INFO);
    }

    @Click(R.id.fragment_me_tv_make_friend)
    void onTvMakeFriendClick() {
        Intent intentUpdate = new Intent(getActivity(), UserProfileDetailActivity_.class);

        // Send notify to inform that tvMakeFriend be clicked
        String fromView = "tvMakeFriend";
        intentUpdate.putExtra("FromView", fromView);

        // Make UserProfileDetailActivity notify when it finish
        startActivityForResult(intentUpdate, REQUEST_UPDATE_INFO);
    }

    @Click(R.id.fragment_me_tv_view_more_my_friend)
    void onTvViewMoreMyFriendClick() {
        Intent intentUpdate = new Intent(getActivity(), UserProfileDetailActivity_.class);

        // Send notify to inform that tvViewMore be clicked
        String fromView = "tvViewMoreMyFriend";
        intentUpdate.putExtra("FromView", fromView);
        startActivityForResult(intentUpdate, REQUEST_UPDATE_INFO);
    }

    @Click(R.id.fragment_me_tv_view_more_my_rating)
    void onTvViewMoreMyRatingClick() {
        Intent intentUpdate = new Intent(getActivity(), UserProfileDetailActivity_.class);

        // Send notify to inform that tvViewMore be clicked
        String fromView = "tvViewMoreMyRating";
        intentUpdate.putExtra("FromView", fromView);
        startActivityForResult(intentUpdate, REQUEST_UPDATE_INFO);
    }

    @Click(R.id.fragment_me_btn_sign_in)
    void onBtnSignInClick() {
        SignInActivity_.intent(this).start();
    }

    @Override
    public void onActivityResult(int requestCode, int currentUserCode, Intent data) {
        super.onActivityResult(requestCode, currentUserCode, data);
        // Reload view
        if (requestCode == REQUEST_UPDATE_INFO) {
            if (TripGuyUtils.isNetworkConnected(getActivity())) {
                initView();
            }
        }
    }

    // Note that to use bitmap, have to create variable target out of .into()
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if (isAdded()) {
                Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(10)
                      .from(bitmap).into(ivBlurAvatar);
                ivAvatar.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

}
