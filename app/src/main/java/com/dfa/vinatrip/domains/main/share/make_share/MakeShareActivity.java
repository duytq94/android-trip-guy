package com.dfa.vinatrip.domains.main.share.make_share;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.search.SearchActivity_;
import com.dfa.vinatrip.utils.TripGuyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import es.dmoral.toasty.Toasty;

@EActivity(R.layout.activity_make_share)
public class MakeShareActivity extends AppCompatActivity {
    public static final int REQUEST_PROVINCE = 1;

    @ViewById(R.id.activity_make_share_tv_province)
    TextView tvProvince;

    @AfterViews
    void onCreate() {
        TripGuyUtils.changeColorStatusBar(this);


    }

    @Click(R.id.activity_make_share_ll_province)
    void onLlProvinceClick() {
        SearchActivity_.intent(this).fromView("MakeShareActivity").startForResult(REQUEST_PROVINCE);
    }

    @OnActivityResult(REQUEST_PROVINCE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            tvProvince.setText(data.getStringExtra("nameProvince"));
        }
    }
}
