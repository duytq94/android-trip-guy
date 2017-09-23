package com.dfa.vinatrip.domains.main.share;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.auth.SignInActivity_;
import com.dfa.vinatrip.domains.main.share.detail_share.DetailShareActivity_;
import com.dfa.vinatrip.domains.main.share.make_share.MakeShareActivity_;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.RecyclerItemClickListener;
import com.dfa.vinatrip.utils.TripGuyUtils;
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

import es.dmoral.toasty.Toasty;

@EFragment(R.layout.fragment_share)
public class ShareFragment extends Fragment {

    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_share_rv_share)
    RecyclerView rvShare;

    @ViewById(R.id.fragment_share_srl_reload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_share_rl_login)
    RelativeLayout rlLogin;

    @ViewById(R.id.fragment_share_rl_not_login)
    RelativeLayout rlNotLogin;


    private ShareAdapter shareAdapter;
    private List<Share> shareList;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    @AfterViews
    void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    shareList.clear();
                    loadShare();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });
        shareList = new ArrayList<>();
        shareAdapter = new ShareAdapter(getActivity(), shareList);
        rvShare.setAdapter(shareAdapter);

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvShare.setLayoutManager(layoutManager);

        rvShare.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), rvShare,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        DetailShareActivity_.intent(getActivity()).share(shareList.get(position)).start();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Share share = dataSnapshot.getValue(Share.class);
                shareList.add(share);
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
        };

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // update shareList again
                if (shareList.size() != 0) {
                    dataService.setShareList(shareList);
                    shareAdapter.notifyDataSetChanged();
                }
                srlReload.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        loadShare();
    }

    @Click(R.id.fragment_share_fab_make_new_share)
    void onFabMakeNewShareClick() {
        if (dataService.getCurrentUser() == null) {
            Toasty.error(getActivity(), "Bạn phải đăng nhập", Toast.LENGTH_SHORT).show();
        } else {
            MakeShareActivity_.intent(getActivity()).start();
        }
    }

    @Click(R.id.fragment_share_btn_sign_in)
    void onBtnSignInClick() {
        SignInActivity_.intent(getActivity()).start();
    }

    @Click(R.id.fragment_share_iv_info)
    void onIvInfoClick() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Chia sẻ chuyến đi");
        alertDialog.setMessage(getString(R.string.message_share));
        alertDialog.setIcon(R.drawable.ic_symbol);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "XONG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    public void loadShare() {
        srlReload.setRefreshing(true);

        databaseReference.child("Share").addChildEventListener(childEventListener);
        // This method to be called after all the onChildAdded() calls have happened
        databaseReference.child("Province").addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }
}
