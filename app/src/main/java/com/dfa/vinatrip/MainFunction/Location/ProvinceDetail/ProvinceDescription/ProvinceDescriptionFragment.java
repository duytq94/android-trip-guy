package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDescription;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.R;

public class ProvinceDescriptionFragment extends Fragment {
    private TextView tvDescription;
    private Province province;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province_description, container, false);
        tvDescription = (TextView) view.findViewById(R.id.fragment_overview_tv_description);

        // Get Province from ProvinceDetailFragment
        province = (Province) getArguments().getSerializable("Province");

        if (province != null) {
            tvDescription.setText(Html.fromHtml(province.getDescription()));
        }

        return view;
    }
}
