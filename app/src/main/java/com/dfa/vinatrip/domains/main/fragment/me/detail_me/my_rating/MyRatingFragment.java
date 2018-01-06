package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_rating;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beesightsoft.caf.exceptions.ApiThrowable;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_rating.adapter.RecyclerMyFeedbackAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@EFragment(R.layout.fragment_my_rating)
public class MyRatingFragment extends BaseFragment<MyRatingView, MyRatingPresenter>
        implements MyRatingView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected MyRatingPresenter presenter;

    @ViewById(R.id.fragment_my_rating_srl_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @ViewById(R.id.fragment_my_rating_rv_list_ratings)
    protected RecyclerView rcvMyRating;
    @ViewById(R.id.fragment_my_rating_ll_rating_not_available)
    protected LinearLayout llRatingNotAvaiable;

    private List<FeedbackResponse> feedbackResponses;
    private RecyclerMyFeedbackAdapter myFeedbackAdapter;

    @AfterInject
    void initInject() {
        DaggerMyRatingComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @AfterViews
    void init() {
        feedbackResponses = new ArrayList<>();
        myFeedbackAdapter = new RecyclerMyFeedbackAdapter(getContext(), feedbackResponses);
        rcvMyRating.setHasFixedSize(true);
        rcvMyRating.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvMyRating.setAdapter(myFeedbackAdapter);

        presenter.getMyFeedback();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.getMyFeedback();
            swipeRefreshLayout.setRefreshing(false);
        });
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

    @NonNull
    @Override
    public MyRatingPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void getMyFeedbackSuccess(List<FeedbackResponse> feedbackResponses) {
        this.feedbackResponses.clear();
        this.feedbackResponses.addAll(feedbackResponses);
        this.myFeedbackAdapter.notifyDataSetChanged();
        if (this.feedbackResponses.size() != 0) {
            rcvMyRating.setVisibility(View.VISIBLE);
            llRatingNotAvaiable.setVisibility(View.GONE);
        } else {
            rcvMyRating.setVisibility(View.GONE);
            llRatingNotAvaiable.setVisibility(View.VISIBLE);
        }
    }
}
