package com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_make_friend)
public class MakeFriendFragment extends Fragment {

    @ViewById(R.id.fragment_make_friend_rv_list_friends)
    RecyclerView rvListFriends;

    @ViewById(R.id.fragment_make_friend_srl_reload)
    SwipeRefreshLayout srlReload;

    private UserProfileAdapter userProfileAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private List<UserFriend> listUserFriends;
    private List<UserProfile> listUserProfiles;
    private UserProfile currentUser;

    @AfterViews
    void onCreateView() {
        // Get ListUserProfile from UserProfileDetailFragment
        Bundle bdListUserProfiles;
        bdListUserProfiles = getArguments().getBundle("bdListUserProfiles");
        listUserProfiles = (List<UserProfile>) bdListUserProfiles.getSerializable("ListUserProfiles");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get profile of the current user
        for (int i = 0; i < listUserProfiles.size(); i++) {
            if (listUserProfiles.get(i).getUid().equals(firebaseUser.getUid())) {
                currentUser = listUserProfiles.get(i);
                break;
            }
        }

        listUserFriends = new ArrayList<>();
        if (CheckNetwork.isNetworkConnected(getActivity())) loadUserFriend();

        srlReload.setColorSchemeResources(R.color.colorMain);

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    loadUserFriend();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvListFriends.setLayoutManager(staggeredGridLayoutManager);
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
                        email = dataSnapshot.child("state").getValue().toString();
                        state = dataSnapshot.child("state").getValue().toString();

                        UserFriend userFriend =
                                new UserFriend(friendId, nickname, avatar, email, state);

                        // Don't add the current user to list
                        if (!userFriend.getFriendId().equals(firebaseUser.getUid())) {
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
                        userProfileAdapter = new UserProfileAdapter(getActivity(), listUserProfiles,
                                listUserFriends, referenceFriend, currentUser, srlReload);
                        rvListFriends.setAdapter(userProfileAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}