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
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.UserProfileDetailActivity_;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

@EFragment(R.layout.fragment_me)
public class MeFragment extends Fragment {

    static final int NOTIFY_UPDATE_REQUEST = 1;

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

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private UserProfile currentUser;
    private List<UserProfile> listUserProfiles;
    private List<UserFriend> listUserFriends;
    private FriendAdapter friendAdapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

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
            listUserFriends = new ArrayList<>();

            // Wait until list user profile load done
            tvMakeFriend.setEnabled(false);
            llUpdateProfile.setEnabled(false);

            loadUserProfile();
            loadUserFriend();

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
                if (firebaseUser == null) {
                    startActivity(new Intent(getActivity(), SignInActivity_.class));
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
                if (firebaseUser != null) {
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

                    currentUser = result;
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
                        llUpdateProfile.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void loadUserFriend() {
        final DatabaseReference referenceFriend = firebaseDatabase.getReference();

        // If no Internet, this method will not run
        referenceFriend.child("UserFriend").child(firebaseUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String friendId, nickname, avatar, email, state;

                        friendId = dataSnapshot.child("friendId").getValue().toString();
                        nickname = dataSnapshot.child("nickname").getValue().toString();
                        avatar = dataSnapshot.child("avatar").getValue().toString();
                        email = dataSnapshot.child("email").getValue().toString();
                        state = dataSnapshot.child("state").getValue().toString();

                        UserFriend userFriend =
                                new UserFriend(friendId, nickname, avatar, email, state);

                        // Don't add the current user and the friend not agree yet to list
                        if (!userFriend.getFriendId().equals(firebaseUser.getUid()) &&
                                userFriend.getState().equals("friend")) {
                            listUserFriends.add(userFriend);
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
        referenceFriend.child("UserFriend").child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvMakeFriend.setEnabled(true);
                        friendAdapter = new FriendAdapter(getActivity(), listUserFriends,
                                tvFriendNotAvailable, srlReload);
                        rvListFriends.setAdapter(friendAdapter);
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
