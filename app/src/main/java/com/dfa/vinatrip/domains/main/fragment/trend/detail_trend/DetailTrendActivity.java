package com.dfa.vinatrip.domains.main.fragment.trend.detail_trend;

import android.support.v7.app.AppCompatActivity;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.place.Trend;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_detail_trend)
public class DetailTrendActivity extends AppCompatActivity {

    @Extra
    protected Trend trend;
}
