package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_friend;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beesightsoft.caf.exceptions.ApiThrowable;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.user.FriendStatus;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.utils.AdapterUserListener;
import com.dfa.vinatrip.widgets.EndlessRecyclerViewScrollListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.dfa.vinatrip.utils.Constants.PAGE_SIZE;

@EFragment(R.layout.fragment_my_friend)
public class MyFriendFragment extends BaseFragment<MyFriendView, MyFriendPresenter>
        implements MyFriendView, AdapterUserListener {

    @ViewById(R.id.fragment_my_friend_rv_list_friend)
    protected RecyclerView rvListFriend;
    @ViewById(R.id.fragment_my_friend_ll_friend_not_available)
    protected LinearLayout llFriendNotAvailable;
    @ViewById(R.id.fragment_my_friend_srl_reload)
    protected SwipeRefreshLayout srlReload;

    private List<User> friendList;
    private ListFriendAdapter adapter;

    @App
    protected MainApplication application;
    @Inject
    protected MyFriendPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerMyFriendComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public MyFriendPresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        friendList = new ArrayList<>();
        adapter = new ListFriendAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListFriend.getContext(), layoutManager.getOrientation());
        rvListFriend.addItemDecoration(decoration);
        rvListFriend.setLayoutManager(layoutManager);

        rvListFriend.setAdapter(adapter);
        rvListFriend.setHasFixedSize(true);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount >= PAGE_SIZE) {
                    presenter.getListFriend(page, PAGE_SIZE);
                }
            }
        };
        rvListFriend.addOnScrollListener(scrollListener);

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            presenter.getListFriend(1, PAGE_SIZE);
            srlReload.setRefreshing(false);
        });

        presenter.getListFriend(1, PAGE_SIZE);
    }

    @Override
    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {
        ApiThrowable apiThrowable = (ApiThrowable) throwable;
        Toast.makeText(getContext(), apiThrowable.firstErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getListFriendSuccess(List<User> friendList, int page) {
        if (friendList.size() > 0) {
            llFriendNotAvailable.setVisibility(View.GONE);
            if (page == 1) {
                this.friendList.clear();
            }
            this.friendList.addAll(friendList);
            adapter.setListFriend(this.friendList);
            adapter.notifyDataSetChanged();
        } else {
            if (page == 1) {
                llFriendNotAvailable.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void unfriendSuccess(FriendStatus friendStatus, int position) {
        if (friendStatus != null) {
            friendList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Hủy kết bạn thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBtnActionClick(int position, String command) {
        presenter.unfriend(position, friendList.get(position).getId());
    }
}
