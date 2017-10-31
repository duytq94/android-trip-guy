package com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.User;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

@EFragment(R.layout.fragment_make_friend)
public class MakeFriendFragment extends BaseFragment<MakeFriendView, MakeFriendPresenter>
        implements MakeFriendView {

    @ViewById(R.id.fragment_make_friend_rv_list_friends)
    RecyclerView rvListFriend;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvListFriend.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListFriend.getContext(), layoutManager.getOrientation());
        rvListFriend.addItemDecoration(decoration);

        presenter.getListUser();
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

    }

    @Override
    public void getListUserSuccess(List<User> userList) {
        adapter = new ListUserAdapter(getActivity(), userList);
        rvListFriend.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getListUserFail(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}