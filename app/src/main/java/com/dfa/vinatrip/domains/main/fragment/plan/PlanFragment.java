package com.dfa.vinatrip.domains.main.fragment.plan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.auth.sign_in.SignInActivity_;
import com.dfa.vinatrip.domains.main.fragment.plan.detail_plan.DetailPlanActivity_;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.MakePlanActivity_;
import com.dfa.vinatrip.infrastructures.ActivityModule;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

@EFragment(R.layout.fragment_plan)
public class PlanFragment extends BaseFragment<PlanView, PlanPresenter>
        implements PlanView {

    @ViewById(R.id.fragment_plan_rv_plan)
    protected RecyclerView rvPlan;
    @ViewById(R.id.fragment_plan_srl_reload)
    protected SwipeRefreshLayout srlReload;
    @ViewById(R.id.fragment_plan_ll_plan_list_not_available)
    protected LinearLayout llPlanListNotAvailable;
    @ViewById(R.id.fragment_plan_rl_login)
    protected RelativeLayout rlLogin;
    @ViewById(R.id.fragment_plan_rl_not_login)
    protected RelativeLayout rlNotLogin;

    private List<Plan> planList;
    private PlanAdapter planAdapter;

    @App
    protected MainApplication application;
    @Inject
    protected PlanPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerPlanComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public PlanPresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        if (presenter.isLogin()) {
            presenter.getPlan();
            rlLogin.setVisibility(View.VISIBLE);
            rlNotLogin.setVisibility(View.GONE);
        } else {
            rlLogin.setVisibility(View.GONE);
            rlNotLogin.setVisibility(View.VISIBLE);
        }

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            if (presenter.isLogin()) {
                if (planList != null) {
                    planList.clear();
                }
                presenter.getPlan();
            }
            srlReload.setRefreshing(false);
        });
    }

    public void initView() {
        rlLogin.setVisibility(View.VISIBLE);
        rlNotLogin.setVisibility(View.GONE);
        planAdapter = new PlanAdapter(getActivity(), planList, presenter.getCurrentUser());

        planAdapter.setOnUpdateOrRemoveClick(new PlanAdapter.OnUpdateOrRemoveClick() {
            @Override
            public void onUpdate(int position) {
                Intent intent = new Intent(getActivity(), MakePlanActivity_.class);

                // Send Plan to MakePlanActivity to update info
                intent.putExtra("Plan", planList.get(position));
                getActivity().startActivity(intent);
            }

            @Override
            public void onRemove(final int position) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Xóa kế hoạch");
                alertDialog.setMessage("Bạn có chắc chắn muốn xóa kế hoạch này?");
                alertDialog.setIcon(R.drawable.ic_notification);
                alertDialog.setButton(
                        DialogInterface.BUTTON_POSITIVE,
                        "ĐỒNG Ý",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "HỦY",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                alertDialog.show();
            }

            @Override
            public void onClick(int position) {
                DetailPlanActivity_.intent(getActivity()).plan(planList.get(position)).start();
            }
        });

        rvPlan.setAdapter(planAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPlan.setLayoutManager(manager);
    }

    @Click(R.id.fragment_plan_fab_make_new_plan)
    void onFabMakeNewPlanClick() {
        MakePlanActivity_.intent(getActivity()).start();
    }

    @Click(R.id.fragment_plan_btn_sign_in)
    void onBtnSignInClick() {
        SignInActivity_.intent(getActivity()).start();
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
    public void getPlanSuccess(List<Plan> planList) {
        if (planList.size() > 0) {
            llPlanListNotAvailable.setVisibility(View.GONE);
            this.planList = planList;
            initView();
        } else {
            llPlanListNotAvailable.setVisibility(View.VISIBLE);
        }
    }
}
