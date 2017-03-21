package com.dfa.vinatrip.MainFunction.Plan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.DataService.DataService;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.Plan.DetailPlan.DetailPlanActivity_;
import com.dfa.vinatrip.MainFunction.Plan.MakePlan.MakePlanActivity_;
import com.dfa.vinatrip.MainFunction.RecyclerItemClickListener;
import com.dfa.vinatrip.R;
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

    private List<Plan> planList;
    private PlanAdapter planAdapter;
    private DatabaseReference databaseReference;
    private UserProfile currentUser;

    @AfterViews
    void onCreateView() {
        planList = new ArrayList<>();

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    planList.clear();
                    loadPlan();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        if (dataService.getCurrentUser() != null && CheckNetwork.isNetworkConnected(getActivity())) {
            currentUser = dataService.getCurrentUser();
            planAdapter = new PlanAdapter(getActivity(), planList, currentUser);
            rvPlan.setAdapter(planAdapter);
            loadPlan();
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            rvPlan.setLayoutManager(staggeredGridLayoutManager);

            rvPlan.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvPlan,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getActivity(), DetailPlanActivity_.class);

                            // Send Plan to DetailPlanActivity
                            intent.putExtra("Plan", planList.get(position));
                            startActivity(intent);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));
        }
    }

    @Click(R.id.fragment_plan_fab_make_new_plan)
    void onFabMakeNewPlanClick() {
        if (dataService.getCurrentUser() != null) {
            Intent intent = new Intent(getActivity(), MakePlanActivity_.class);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Bạn phải đăng nhập mới sử dụng được tính năng này", Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.fragment_my_friend_iv_info)
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

    public void loadPlan() {
        srlReload.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // If no Internet, this method will not run
        databaseReference.child("Plan").child(currentUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        llPlanListNotAvailable.setVisibility(View.GONE);
                        rvPlan.setVisibility(View.VISIBLE);

                        Plan plan = dataSnapshot.getValue(Plan.class);
                        planList.add(plan);
                        planAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // This method to be called after all the onChildAdded() calls have happened
        databaseReference.child("Plan").child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
