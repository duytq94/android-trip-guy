package com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.me.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_make_friend)
public class MakeFriendFragment extends Fragment {

    @ViewById(R.id.fragment_make_friend_rv_list_friends)
    RecyclerView rvListFriend;

    private ListUserProfileAdapter listUserProfileAdapter;
    private DatabaseReference referenceFriend = FirebaseDatabase.getInstance().getReference();
    private List<UserFriend> userFriendList;
    private List<UserProfile> userProfileList;
    private UserProfile currentUser;

    @AfterViews
    void onCreateView() {
        userProfileList = new ArrayList<>();
//        userProfileList.addAll(dataService.getUserProfileList());

//        currentUser = dataService.getCurrentUser();

        userFriendList = new ArrayList<>();
        listUserProfileAdapter = new ListUserProfileAdapter(getActivity(), userProfileList,
                userFriendList, referenceFriend, currentUser);

//        loadUserFriend();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvListFriend.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListFriend.getContext(),
                manager.getOrientation());
        rvListFriend.addItemDecoration(decoration);
    }

}