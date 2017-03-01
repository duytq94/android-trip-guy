package com.dfa.vinatrip.MainFunction.Me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.BuildConfig;
import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.Login.SignInActivity;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.UserProfileDetailActivity;
import com.dfa.vinatrip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class MeFragment extends Fragment {

    static final int NOTIFY_UPDATE_REQUEST = 1;

    private ImageView ivAvatar, ivBlurAvatar;
    private TextView tvNickname, tvCity, tvBirthday, tvMakeFriend,
            tvIntroduceYourSelf, tvAppInfo, tvEmail, tvSex;
    private LinearLayout llInfo, llMyFriends, llSettings;
    private LinearLayout llSignIn, llSignOut, llUpdateProfile;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private UserProfile userProfile;
    private List<UserProfile> listUserProfiles;
    private SwipeRefreshLayout srlReload;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Must create View first
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        findViewByIds(view);
        showAppInfo();

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

        return view;
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

        tvAppInfo.setText("TripGuy - giúp bạn có chuyến đi trọng vẹn nhất!\n");
        tvAppInfo.append("Version name: " + versionName + "\n");
        tvAppInfo.append("Version code: " + versionCode + "\n");
    }

    public void setContentViews() {

        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

        tvAppInfo.setText("TripGuy - giúp bạn có chuyến đi trọng vẹn nhất!\n");
        tvAppInfo.append("Version name: " + versionName + "\n");
        tvAppInfo.append("Version code: " + versionCode + "\n");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            srlReload.setRefreshing(true);
            listUserProfiles = new ArrayList<>();

            // Wait until list user profile load done
            tvMakeFriend.setEnabled(false);
            llUpdateProfile.setEnabled(false);

            loadUserProfile();
        } else {
            srlReload.setRefreshing(false);
        }
    }

    public void onClickListener() {
        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser == null) {
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Bạn đã đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    firebaseAuth.signOut();

                    // Restart app
                    Intent restart = getActivity().getPackageManager()
                            .getLaunchIntentForPackage(getActivity().getPackageName());
                    restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(restart);
                } else {
                    Toast.makeText(getActivity(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    Intent intentUpdate = new Intent(getActivity(), UserProfileDetailActivity.class);

                    // Send UserProfile to UserProfileDetailActivity
                    intentUpdate.putExtra("UserProfile", userProfile);

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
                if (firebaseUser != null) {
                    Intent intentUpdate = new Intent(getActivity(), UserProfileDetailActivity.class);

                    // Send UserProfile to UserProfileDetailActivity
                    intentUpdate.putExtra("UserProfile", userProfile);

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

    public void findViewByIds(View view) {
        tvNickname = (TextView) view.findViewById(R.id.fragment_me_tv_nickname);
        tvCity = (TextView) view.findViewById(R.id.fragment_me_tv_city);
        tvAppInfo = (TextView) view.findViewById(R.id.fragment_me_tv_app_info);
        tvBirthday = (TextView) view.findViewById(R.id.fragment_me_tv_birthday);
        tvEmail = (TextView) view.findViewById(R.id.fragment_me_tv_email);
        tvIntroduceYourSelf = (TextView) view.findViewById(R.id.fragment_me_tv_introduce_your_self);
        tvSex = (TextView) view.findViewById(R.id.fragment_me_tv_sex);
        tvMakeFriend = (TextView) view.findViewById(R.id.fragment_me_tv_make_friend);
        ivAvatar = (ImageView) view.findViewById(R.id.fragment_me_iv_avatar);
        ivBlurAvatar = (ImageView) view.findViewById(R.id.fragment_me_iv_blur_avatar);
        llSignIn = (LinearLayout) view.findViewById(R.id.fragment_me_ll_sign_in);
        llSignOut = (LinearLayout) view.findViewById(R.id.fragment_me_ll_sign_out);
        llInfo = (LinearLayout) view.findViewById(R.id.fragment_me_ll_info);
        llMyFriends = (LinearLayout) view.findViewById(R.id.fragment_me_ll_my_friends);
        llSettings = (LinearLayout) view.findViewById(R.id.fragment_me_ll_settings);
        llUpdateProfile = (LinearLayout) view.findViewById(R.id.fragment_me_ll_update_profile);
        srlReload = (SwipeRefreshLayout) view.findViewById(R.id.fragment_me_srlReload);
    }

    @Override
    public void onActivityResult(int requestCode, int userProfileCode, Intent data) {
        super.onActivityResult(requestCode, userProfileCode, data);
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

    public void loadUserProfile() {
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // If no Internet, this method will not run
        databaseReference.child("UserProfile").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String avatar, city, introduceYourSelf, nickname, birthday, uid, sex, email;

                city = dataSnapshot.child("city").getValue().toString();
                introduceYourSelf = dataSnapshot.child("introduceYourSelf").getValue().toString();
                avatar = dataSnapshot.child("avatar").getValue().toString();
                nickname = dataSnapshot.child("nickname").getValue().toString();
                birthday = dataSnapshot.child("birthday").getValue().toString();
                sex = dataSnapshot.child("sex").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                uid = dataSnapshot.getKey();

                UserProfile result = new UserProfile(nickname, avatar, introduceYourSelf,
                        city, birthday, uid, sex, email);

                listUserProfiles.add(result);

                if (result.uid.equals(firebaseUser.getUid())) {
                    if (!result.getNickname().equals("")) {
                        tvNickname.setText(result.getNickname());
                    }
                    if (!result.getCity().equals("")) {
                        tvCity.setText(result.getCity());
                    }
                    if (!result.getAvatar().isEmpty()) {
                        Picasso.with(getActivity())
                                .load(result.getAvatar())
                                .into(target);
                    }
                    tvIntroduceYourSelf.setText(result.getIntroduceYourSelf());
                    tvBirthday.setText(result.getBirthday());
                    tvEmail.setText(result.getEmail());
                    tvSex.setText(result.getSex());

                    userProfile = result;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        // This method to be called after all the onChildAdded() calls have happened
        databaseReference.child("UserProfile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        srlReload.setRefreshing(false);
                        tvMakeFriend.setEnabled(true);
                        llUpdateProfile.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    // Note that to use bitmap, have to create variable target out of .into()
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(20)
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
