package com.dfa.vinatrip.domains.main.me.detail_me.my_friend;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.me.ListFriendVerticalAdapter;
import com.dfa.vinatrip.domains.main.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.AppUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_my_friend)
public class MyFriendFragment extends Fragment {

    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_my_friend_rv_list_friends)
    RecyclerView rvListFriends;

    @ViewById(R.id.fragment_my_friend_ll_friend_not_available)
    LinearLayout llFriendNotAvailable;

    private List<UserFriend> listUserFriends;
    private ListFriendVerticalAdapter listFriendVerticalAdapter;

    @AfterViews
    void init() {
        listUserFriends = new ArrayList<>();

        dataService.setOnChangeUserFriendList(new DataService.OnChangeUserFriendList() {
            @Override
            public void onAddFriend() {
                listUserFriends.clear();
                listUserFriends.addAll(AppUtil.filterListFriends(dataService.getUserFriendList()));
                listFriendVerticalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemoveFriend() {
                listUserFriends.clear();
                listUserFriends.addAll(AppUtil.filterListFriends(dataService.getUserFriendList()));
                listFriendVerticalAdapter.notifyDataSetChanged();
                if (listUserFriends.size() == 0) {
                    llFriendNotAvailable.setVisibility(View.VISIBLE);
                } else {
                    llFriendNotAvailable.setVisibility(View.GONE);
                }
            }
        });

        listUserFriends.addAll(AppUtil.filterListFriends(dataService.getUserFriendList()));
        if (listUserFriends.size() == 0) {
            llFriendNotAvailable.setVisibility(View.VISIBLE);
        } else {
            llFriendNotAvailable.setVisibility(View.GONE);
        }

        listFriendVerticalAdapter = new ListFriendVerticalAdapter(getActivity(), listUserFriends);
        rvListFriends.setAdapter(listFriendVerticalAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListFriends.getContext(),
                                                                     manager.getOrientation());
        rvListFriends.addItemDecoration(decoration);
        rvListFriends.setLayoutManager(manager);
    }
}
