package com.dfa.vinatrip.MainFunction.Plan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dfa.vinatrip.BuildConfig;
import com.dfa.vinatrip.R;

public class PlanFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        String buildType = BuildConfig.BUILD_TYPE;
        String flavor = BuildConfig.FLAVOR;

        TextView textView = (TextView) view.findViewById(R.id.fragment_plan_tv_info);
        textView.setText(versionName + "\n" + versionCode + "\n" + buildType + "\n" + flavor);

        return view;
    }
}
