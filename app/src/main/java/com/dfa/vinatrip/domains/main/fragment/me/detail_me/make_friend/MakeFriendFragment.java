package com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.User;
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

@EFragment(R.layout.fragment_make_friend)
public class MakeFriendFragment extends BaseFragment<MakeFriendView, MakeFriendPresenter>
        implements MakeFriendView, AdapterUserListener {

    @ViewById(R.id.fragment_make_friend_rv_list_friends)
    protected RecyclerView rvListFriend;
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
        adapter = new ListUserAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvListFriend.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListFriend.getContext(), layoutManager.getOrientation());
        rvListFriend.addItemDecoration(decoration);
        rvListFriend.setAdapter(adapter);
        rvListFriend.setHasFixedSize(true);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.getListUser(page, 10);
            }
        };
        rvListFriend.addOnScrollListener(scrollListener);

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            presenter.getListUser(1, 10);
            srlReload.setRefreshing(false);
        });

        presenter.getListUser(1, 10);
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
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getListUserSuccess(List<User> userList) {
        if (userList.size() > 0) {
            this.userList.clear();
            this.userList.addAll(userList);
            adapter.setListUser(this.userList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBtnActionClick(int position, String command) {

    }
}