package com.dfa.vinatrip.domains.main.province.detail_province.province_description;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.Province;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_province_description)
public class ProvinceDescriptionFragment extends Fragment {

    @ViewById(R.id.fragment_overview_tv_description)
    TextView tvDescription;

    private Province province;

    @AfterViews
    void onCreateView() {
        // Get Province from ProvinceDetailFragment
        province = getArguments().getParcelable("Province");

        if (province != null) {
            tvDescription.setText(Html.fromHtml(province.getDescription()));
        }
    }
}
