package com.dfa.vinatrip.domains.main.me.detail_me.make_friend;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.AppUtil;
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
    RecyclerView rvListFriend;

    private ListUserProfileAdapter listUserProfileAdapter;
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
        listUserProfileAdapter = new ListUserProfileAdapter(getActivity(), userProfileList,
                                                            userFriendList, referenceFriend, currentUser);

        if (AppUtil.isNetworkConnected(getActivity())) loadUserFriend();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvListFriend.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListFriend.getContext(),
                                                                     manager.getOrientation());
        rvListFriend.addItemDecoration(decoration);
    }

    public void loadUserFriend() {
        // If no Internet, this method will not run
        referenceFriend.child("UserFriend").child(currentUser.getUid())
                       .addChildEventListener(new ChildEventListener() {
                           @Override
                           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                               UserFriend userFriend = dataSnapshot.getValue(UserFriend.class);
                               userFriendList.add(userFriend);
                               listUserProfileAdapter.notifyDataSetChanged();
                           }

                           @Override
                           public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                               UserFriend userFriend = dataSnapshot.getValue(UserFriend.class);

                               // Don't add the current user to list
                               if (!userFriend.getFriendId().equals(currentUser.getUid())) {
                                   for (int i = 0; i < userFriendList.size(); i++) {
                                       if (userFriendList.get(i).getFriendId().equals(userFriend.getFriendId())) {
                                           userFriendList.set(i, userFriend);
                                           if (userFriend.getState().equals("friend")) {
                                               dataService.addToUserFriendList(userFriend);
                                           }
                                           break;
                                       }
                                   }
                               }
                               listUserProfileAdapter.notifyDataSetChanged();
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
                               listUserProfileAdapter.notifyDataSetChanged();
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
                               if (isAdded()) {
                                   rvListFriend.setAdapter(listUserProfileAdapter);
                               }
                           }

                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });
    }
}