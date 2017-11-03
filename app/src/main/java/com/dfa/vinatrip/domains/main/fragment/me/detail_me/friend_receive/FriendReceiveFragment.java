package com.dfa.vinatrip.domains.main.fragment.me.detail_me.friend_receive;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.utils.AdapterUserListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@EFragment(R.layout.fragment_friend_receive)
public class FriendReceiveFragment extends BaseFragment<FriendReceiveView, FriendReceivePresenter>
        implements FriendReceiveView, AdapterUserListener {

    @ViewById(R.id.fragment_friend_receive_rv_list_friend_receive)
    protected RecyclerView rvListFriendReceive;
    @ViewById(R.id.fragment_friend_receive_ll_friend_receive_not_available)
    protected LinearLayout llFriendReceiveNotAvailable;

    private List<User> friendReceiveList;
    private ListFriendReceiveAdapter adapter;

    @App
    protected MainApplication application;
    @Inject
    protected FriendReceivePresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerFriendReceiveComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public FriendReceivePresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        friendReceiveList = new ArrayList<>();
        adapter = new ListFriendReceiveAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListFriendReceive.getContext(), layoutManager.getOrientation());
        rvListFriendReceive.addItemDecoration(decoration);
        rvListFriendReceive.setLayoutManager(layoutManager);

        presenter.getListFriendReceive(1, 10);
    }

    @Override
    public void onBtnActionClick(int position, String command) {

    }

    @Override
    public void showLoading() {
//        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getListFriendReceiveSuccess(List<User> friendReceiveList) {
        if (friendReceiveList.size() == 0) {
            llFriendReceiveNotAvailable.setVisibility(View.VISIBLE);
        } else {
            llFriendReceiveNotAvailable.setVisibility(View.GONE);
            this.friendReceiveList.addAll(friendReceiveList);
            adapter.setListUser(this.friendReceiveList);
            adapter.notifyDataSetChanged();
        }
    }
}
