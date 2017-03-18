package com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.SplashScreen.DataService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_make_friend)
public class MakeFriendFragment extends Fragment {

    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_make_friend_rv_list_friends)
    RecyclerView rvListFriends;

    @ViewById(R.id.fragment_make_friend_srl_reload)
    SwipeRefreshLayout srlReload;

    private UserProfileAdapter userProfileAdapter;
    private DatabaseReference referenceFriend = FirebaseDatabase.getInstance().getReference();
    private List<UserFriend> userFriendList;
    private List<UserProfile> userProfileList;
    private UserProfile currentUser;

    @AfterViews
    void onCreateView() {
        userProfileList = new ArrayList<>();
        userProfileList.addAll(dataService.getUserProfileList());

        currentUser = dataService.getCurrentUser();

        userFriendList = new ArrayList<>();
        userProfileAdapter = new UserProfileAdapter(getActivity(), userProfileList,
                userFriendList, referenceFriend, currentUser);

        if (CheckNetwork.isNetworkConnected(getActivity())) loadUserFriend();

        srlReload.setColorSchemeResources(R.color.colorMain);

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    srlReload.setRefreshing(true);
                    loadUserFriend();
                }
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvListFriends.setLayoutManager(staggeredGridLayoutManager);
    }

    public void loadUserFriend() {
        // If no Internet, this method will not run
        referenceFriend.child("UserFriend").child(currentUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserFriend userFriend = dataSnapshot.getValue(UserFriend.class);
                        userFriendList.add(userFriend);
                        userProfileAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        UserFriend userFriend = dataSnapshot.getValue(UserFriend.class);

                        // Don't add the current user to list
                        if (!userFriend.getFriendId().equals(currentUser.getUid())) {
                            for (int i = 0; i < userFriendList.size(); i++) {
                                if (userFriendList.get(i).getFriendId().equals(userFriend.getFriendId())) {
                                    userFriendList.remove(i);
                                    userFriendList.add(userFriend);
                                    if (userFriend.getState().equals("friend")) {
                                        dataService.addToUserFriendList(userFriend);
                                    }
                                    break;
                                }
                            }
                        }
                        userProfileAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        UserFriend userFriend = dataSnapshot.getValue(UserFriend.class);

                        // Don't add the current user to list
                        if (!userFriend.getFriendId().equals(currentUser.getUid())) {
                            for (int i = 0; i < userFriendList.size(); i++) {
                                if (userFriendList.get(i).getFriendId().equals(userFriend.getFriendId())) {
                                    dataService.removeFromUserFriendList(userFriendList.get(i).getFriendId());
                                    userFriendList.remove(i);
                                    break;
                                }
                            }
                        }
                        userProfileAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // This method to be called after all the onChildAdded() calls have happened
        referenceFriend.child("UserFriend").child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        rvListFriends.setAdapter(userProfileAdapter);
                        srlReload.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}