package com.dfa.vinatrip.domains.main.fragment.plan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beesightsoft.caf.exceptions.ApiThrowable;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.auth.sign_in.SignInActivity_;
import com.dfa.vinatrip.domains.main.fragment.plan.detail_plan.DetailPlanActivity_;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.MakePlanActivity_;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.utils.AppUtil;
import com.nhancv.nutc.NUtc;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.dfa.vinatrip.utils.Constants.KEY_NEW_PLAN;
import static com.dfa.vinatrip.utils.Constants.REQUEST_MAKE_PLAN;

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
    private PlanAdapter adapter;

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

    @NonNull
    @Override
    public PlanPresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        if (presenter.isLogin()) {
            planList = new ArrayList<>();
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
        adapter = new PlanAdapter(getActivity(), planList, presenter.getCurrentUser());

        adapter.setOnUpdateOrRemoveClick(new PlanAdapter.OnUpdateOrRemoveClick() {
            @Override
            public void onUpdate(int position) {
                if (AppUtil.stringDateToTimestamp(planList.get(position).getDateGo()) > NUtc.getUtcNow()) {
                    MakePlanActivity_.intent(getActivity()).plan(planList.get(position)).isUpdatePlan(true).start();
                } else {
                    Toast.makeText(getActivity(), "Chuyến đi này đã bắt đầu, bạn không thể chỉnh sửa",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRemove(final int position) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                if (presenter.getCurrentUser().getId() == planList.get(position).getIdUserMakePlan()) {
                    alertDialog.setTitle("Xóa kế hoạch");
                    alertDialog.setMessage("Bạn có chắc chắn muốn xóa kế hoạch này?");
                } else {
                    alertDialog.setTitle("Không tham gia");
                    alertDialog.setMessage("Bạn có chắc chắn không muốn tham gia kế hoạch này?");
                }
                alertDialog.setIcon(R.drawable.ic_notification);
                alertDialog.setButton(
                        DialogInterface.BUTTON_POSITIVE,
                        "ĐỒNG Ý",
                        (dialogInterface, i) -> {
                            if (presenter.getCurrentUser().getId() == planList.get(position).getIdUserMakePlan()) {
                                presenter.removePlan(position, planList.get(position).getId());
                            } else {
                                presenter.cancelPlan(position, planList.get(position).getId());
                            }
                        });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "HỦY",
                        (dialogInterface, i) -> {

                        });
                alertDialog.show();
            }

            @Override
            public void onClick(int position) {
                DetailPlanActivity_.intent(getActivity()).plan(planList.get(position)).start();
            }
        });

        rvPlan.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPlan.setLayoutManager(manager);
    }

    @Click(R.id.fragment_plan_fab_make_new_plan)
    public void onFabMakeNewPlanClick() {
        MakePlanActivity_.intent(this).startForResult(REQUEST_MAKE_PLAN);
    }

    @Click(R.id.fragment_plan_btn_sign_in)
    public void onBtnSignInClick() {
        SignInActivity_.intent(this).start();
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
    public void getPlanSuccess(List<Plan> planList) {
        if (planList.size() > 0) {
            rvPlan.setVisibility(View.VISIBLE);
            llPlanListNotAvailable.setVisibility(View.GONE);
            this.planList.addAll(planList);
            initView();
        } else {
            rvPlan.setVisibility(View.GONE);
            llPlanListNotAvailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void cancelPlanSuccess(String message, int position) {
        Toast.makeText(getActivity(), "Bạn không tham gia kế hoạch này nữa", Toast.LENGTH_SHORT).show();
        planList.remove(position);
        adapter.notifyDataSetChanged();
        if (planList.size() == 0) {
            llPlanListNotAvailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void removePlanSuccess(String message, int position) {
        Toast.makeText(getActivity(), "Xóa kế hoạch thành công", Toast.LENGTH_SHORT).show();
        planList.remove(position);
        adapter.notifyDataSetChanged();
        if (planList.size() == 0) {
            llPlanListNotAvailable.setVisibility(View.VISIBLE);
        }
    }

    @OnActivityResult(REQUEST_MAKE_PLAN)
    public void onMakePlanResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            rvPlan.setVisibility(View.VISIBLE);
            llPlanListNotAvailable.setVisibility(View.GONE);
            planList.add((Plan) data.getSerializableExtra(KEY_NEW_PLAN));
            initView();
        }
    }

    @Override
    public void loginOtherActivity() {
        init();
    }
}
