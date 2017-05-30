package com.dfa.vinatrip.domains.main.share.make_share;

import android.support.v7.app.AppCompatActivity;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.utils.TripGuyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_make_share)
public class MakeShareActivity extends AppCompatActivity {

    @AfterViews
    void onCreate() {
        TripGuyUtils.changeColorStatusBar(this);


    }
}
