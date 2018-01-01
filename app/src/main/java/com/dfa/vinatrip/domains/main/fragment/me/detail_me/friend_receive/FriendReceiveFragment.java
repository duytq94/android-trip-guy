package com.dfa.vinatrip.domains.main.fragment.me.detail_me.friend_receive;

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

@EFragment(R.layout.fragment_friend_receive)
public class FriendReceiveFragment extends BaseFragment<FriendReceiveView, FriendReceivePresenter>
        implements FriendReceiveView, AdapterUserListener {

    @ViewById(R.id.fragment_friend_receive_rv_list_friend_receive)
    protected RecyclerView rvListFriendReceive;
    @ViewById(R.id.fragment_friend_receive_ll_friend_receive_not_available)
    protected LinearLayout llFriendReceiveNotAvailable;
    @ViewById(R.id.fragment_friend_receive_srl_reload)
    protected SwipeRefreshLayout srlReload;

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

        rvListFriendReceive.setAdapter(adapter);
        rvListFriendReceive.setHasFixedSize(true);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount >= PAGE_SIZE) {
                    presenter.getListFriendReceive(page, PAGE_SIZE);
                }
            }
        };
        rvListFriendReceive.addOnScrollListener(scrollListener);

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            presenter.getListFriendReceive(1, PAGE_SIZE);
            srlReload.setRefreshing(false);
        });

        presenter.getListFriendReceive(1, PAGE_SIZE);
    }

    @Override
    public void onBtnActionClick(int position, String command) {
        presenter.acceptFriendRequest(position, friendReceiveList.get(position).getFriendStatus().getId());
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
    public void getListFriendReceiveSuccess(List<User> friendReceiveList, int page) {
        if (friendReceiveList.size() > 0) {
            llFriendReceiveNotAvailable.setVisibility(View.GONE);
            if (page == 1) {
                this.friendReceiveList.clear();
            }
            this.friendReceiveList.addAll(friendReceiveList);
            adapter.setFriendReceiveList(this.friendReceiveList);
            adapter.notifyDataSetChanged();
        } else {
            if (page == 1) {
                llFriendReceiveNotAvailable.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void acceptFriendRequestSuccess(int position, FriendStatus friendStatus) {
        if (friendStatus != null) {
            friendReceiveList.remove(position);
            adapter.notifyDataSetChanged();
        }
    }
}
