package com.dfa.vinatrip.domains.main.share;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.login.SignInActivity_;
import com.dfa.vinatrip.domains.main.share.make_share.MakeShareActivity_;
import com.dfa.vinatrip.services.DataService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_share)
public class ShareFragment extends Fragment {

    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_share_rv_share)
    RecyclerView rvPlan;

    @ViewById(R.id.fragment_share_srl_reload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_share_ll_share_list_not_available)
    LinearLayout llPlanListNotAvailable;

    @ViewById(R.id.fragment_share_rl_login)
    RelativeLayout rlLogin;

    @ViewById(R.id.fragment_share_rl_not_login)
    RelativeLayout rlNotLogin;

    @AfterViews
    void init() {

    }

    @Click(R.id.fragment_share_fab_make_new_share)
    void onFabMakeNewShareClick() {
        MakeShareActivity_.intent(getActivity()).start();
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
}
