package com.dfa.vinatrip.MainFunction.Plan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.dfa.vinatrip.DataService.DataService;
import com.dfa.vinatrip.Login.SignInActivity_;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.Plan.MakePlan.MakePlanActivity_;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.TripGuyUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_plan)
public class PlanFragment extends Fragment {

    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_plan_rv_plan)
    RecyclerView rvPlan;

    @ViewById(R.id.fragment_plan_srl_reload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_plan_ll_plan_list_not_available)
    LinearLayout llPlanListNotAvailable;

    @ViewById(R.id.fragment_plan_ll_login)
    LinearLayout llLogin;

    @ViewById(R.id.fragment_plan_ll_not_login)
    LinearLayout llNotLogin;

    private List<Plan> planList;
    private PlanAdapter planAdapter;
    private DatabaseReference databaseReference;
    private UserProfile currentUser;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    @AfterViews
    void onCreateView() {
        currentUser = dataService.getCurrentUser();

        planList = new ArrayList<>();

        if (TripGuyUtils.isNetworkConnected(getActivity())) {
            if (currentUser != null) {
                initView();
            } else {
                llLogin.setVisibility(View.GONE);
                llNotLogin.setVisibility(View.VISIBLE);
            }
        }

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    if (currentUser != null) {
                        planList.clear();
                        initView();
                    }
                    srlReload.setRefreshing(false);
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });
    }

    public void initView() {
        llLogin.setVisibility(View.VISIBLE);
        llNotLogin.setVisibility(View.GONE);
        planAdapter = new PlanAdapter(getActivity(), planList, currentUser);

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
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ĐỒNG Ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child("Plan").child(currentUser.getUid())
                                .child(planList.get(position).getId()).removeValue();
                        planList.remove(position);
                        planAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "HỦY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }
        });

        rvPlan.setAdapter(planAdapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (isAdded()) {
                    llPlanListNotAvailable.setVisibility(View.GONE);
                    rvPlan.setVisibility(View.VISIBLE);

                    Plan plan = dataSnapshot.getValue(Plan.class);
                    planList.add(plan);
                    planAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Plan plan = dataSnapshot.getValue(Plan.class);
                for (int i = 0; i < planList.size(); i++) {
                    if (planList.get(i).getId().equals(plan.getId())) {
                        planList.remove(i);
                        planList.add(plan);
                        break;
                    }
                }
                planAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (planList.size() == 0) {
                    llPlanListNotAvailable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    // add planList to dataService
                    if (planList.size() != 0) {
                        llPlanListNotAvailable.setVisibility(View.GONE);
                        rvPlan.setVisibility(View.VISIBLE);
                        dataService.setPlanList(planList);
                    } else {
                        llPlanListNotAvailable.setVisibility(View.VISIBLE);
                        rvPlan.setVisibility(View.GONE);
                    }
                    srlReload.setRefreshing(false);
                    planAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        loadPlan();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPlan.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(rvPlan.getContext(), manager.getOrientation());
        rvPlan.addItemDecoration(decoration);
    }

    @Click(R.id.fragment_plan_fab_make_new_plan)
    void onFabMakeNewPlanClick() {
        Intent intent = new Intent(getActivity(), MakePlanActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.fragment_plan_iv_info)
    void onIvInfoClick() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Lập kế hoạch chuyến đi");
        alertDialog.setMessage(getString(R.string.message_plan));
        alertDialog.setIcon(R.drawable.ic_symbol);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "XONG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    @Click(R.id.fragment_plan_btn_sign_in)
    void onBtnSignInClick() {
        Intent intent = new Intent(getActivity(), SignInActivity_.class);
        startActivity(intent);
    }

    public void loadPlan() {
        srlReload.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // If no Internet, this method will not run
        databaseReference.child("Plan").child(currentUser.getUid())
                .addChildEventListener(childEventListener);

        // This method to be called after all the onChildAdded() calls have happened
        databaseReference.child("Plan").child(currentUser.getUid())
                .addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }
}
