package com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.beesightsoft.caf.exceptions.ApiThrowable;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.models.response.user.FriendStatus;
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

import static com.dfa.vinatrip.utils.Constants.CANCEL_REQUEST;
import static com.dfa.vinatrip.utils.Constants.MAKE_REQUEST;
import static com.dfa.vinatrip.utils.Constants.PAGE_SIZE;

@EFragment(R.layout.fragment_make_friend)
public class MakeFriendFragment extends BaseFragment<MakeFriendView, MakeFriendPresenter>
        implements MakeFriendView, AdapterUserListener {

    @ViewById(R.id.fragment_make_friend_rv_list_user)
    protected RecyclerView rvListUser;
    @ViewById(R.id.fragment_make_friend_srl_reload)
    protected SwipeRefreshLayout srlReload;

    private ListUserAdapter adapter;
    private List<User> userList;

    @App
    protected MainApplication application;
    @Inject
    protected MakeFriendPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerMakeFriendComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public MakeFriendPresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        userList = new ArrayList<>();
        adapter = new ListUserAdapter(this, presenter.getCurrentUser());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListUser.getContext(), layoutManager.getOrientation());
        rvListUser.setLayoutManager(layoutManager);
        rvListUser.addItemDecoration(decoration);

        rvListUser.setAdapter(adapter);
        rvListUser.setHasFixedSize(true);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount >= PAGE_SIZE) {
                    presenter.getListUser(page, PAGE_SIZE);
                }
            }
        };
        rvListUser.addOnScrollListener(scrollListener);

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            presenter.getListUser(1, PAGE_SIZE);
            srlReload.setRefreshing(false);
        });

        presenter.getListUser(1, PAGE_SIZE);
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
    public void getListUserSuccess(List<User> userList, int page) {
        if (userList.size() > 0) {
            if (page == 1) {
                this.userList.clear();
            }
            this.userList.addAll(userList);
            filterUser();
            adapter.setListUser(this.userList);
            adapter.notifyDataSetChanged();
        }
    }

    public void filterUser() {
        // Remove the user send request to current user and the user be friend
        for (int i = userList.size() - 1; i >= 0; i--) {
            User user = userList.get(i);
            if (user.getFriendStatus() == null ||
                    (user.getFriendStatus().getStatus() == 1 &&
                            user.getFriendStatus().getIdUserRequest() == presenter.getCurrentUser().getId())) {

            } else {
                userList.remove(i);
            }

        }
    }

    @Override
    public void addFriendRequestSuccess(int position, FriendStatus friendStatus) {
        if (friendStatus != null) {
            userList.get(position).setFriendStatus(friendStatus);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Gửi lời mời kết bạn thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cancelFriendRequestSuccess(int position, FriendStatus friendStatus) {
        if (friendStatus != null) {
            userList.get(position).setFriendStatus(null);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Hủy kết bạn thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBtnActionClick(int position, String command) {
        switch (command) {
            case MAKE_REQUEST:
                presenter.addFriendRequest(position, userList.get(position).getId());
                break;

            case CANCEL_REQUEST:
                presenter.cancelFriendRequest(position, userList.get(position).getFriendStatus().getId());
                break;
        }
    }
}