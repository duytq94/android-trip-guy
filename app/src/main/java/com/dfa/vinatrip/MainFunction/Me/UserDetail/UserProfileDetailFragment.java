package com.dfa.vinatrip.MainFunction.Me.UserDetail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.MakeFriendFragment;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.MakeFriendFragment_;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.UpdateProfile.UpdateUserProfileFragment;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.UpdateProfile.UpdateUserProfileFragment_;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.ViewPagerSwipeFragmentAdapter;
import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.List;

@EFragment(R.layout.fragment_user_profile_detail)
public class UserProfileDetailFragment extends Fragment {

    @ViewById(R.id.fragment_user_profile_detail_vp_user_profile_detail)
    ViewPager vpMeDetail;

    @ViewById(R.id.fragment_user_profile_detail_tl_menu)
    TabLayout tlMenu;

    private UserProfile userProfile;
    private List<UserProfile> listUserProfiles;
    private String fromView;

    private UpdateUserProfileFragment updateUserProfileFragment;
    private MakeFriendFragment makeFriendFragment;

    @AfterViews
    void onCreateView() {
        // Get UserProfile from UserProfileDetailActivity
        Bundle bdUserProfile;
        bdUserProfile = getArguments().getBundle("bdUserProfile");
        userProfile = (UserProfile) bdUserProfile.getSerializable("UserProfile");

        // Get ListUserProfile from UserProfileDetailActivity
        Bundle bdListUserProfiles;
        bdListUserProfiles = getArguments().getBundle("bdListUserProfiles");
        listUserProfiles = (List<UserProfile>) bdListUserProfiles.getSerializable("ListUserProfiles");

        // Get the FromView from UserProfileDetailActivity
        Bundle bdFromView;
        bdFromView = getArguments().getBundle("bdFromView");
        fromView = bdFromView.getString("FromView");

        setupViewPager(vpMeDetail, userProfile);

        tlMenu.setupWithViewPager(vpMeDetail);

        switch (fromView) {
            case "llUpdateProfile":
                vpMeDetail.setCurrentItem(0);
                break;
            case "tvMakeFriend":
                vpMeDetail.setCurrentItem(1);
                break;
        }
    }

    public void setupViewPager(ViewPager vpMeDetail, UserProfile userProfile) {
        ViewPagerSwipeFragmentAdapter adapter =
                new ViewPagerSwipeFragmentAdapter(getChildFragmentManager());

        // Send UserProfile to UpdateUserProfileFragment
        Bundle bdUserProfile = new Bundle();
        bdUserProfile.putParcelable("UserProfile", userProfile);
        updateUserProfileFragment = new UpdateUserProfileFragment_();
        updateUserProfileFragment.setArguments(bdUserProfile);

        // Send UserProfile and ListUserProfiles to MakeFriendFragment
        Bundle bdListUserProfiles = new Bundle();
        bdListUserProfiles.putSerializable("ListUserProfiles", (Serializable) listUserProfiles);
        Bundle bd = new Bundle();
        bd.putBundle("bdUserProfile", bdUserProfile);
        bd.putBundle("bdListUserProfiles", bdListUserProfiles);
        makeFriendFragment = new MakeFriendFragment_();
        makeFriendFragment.setArguments(bd);

        adapter.addFragment(updateUserProfileFragment, "CHỈNH SỬA");
        adapter.addFragment(makeFriendFragment, "THÊM BẠN");

        vpMeDetail.setAdapter(adapter);
    }
}
