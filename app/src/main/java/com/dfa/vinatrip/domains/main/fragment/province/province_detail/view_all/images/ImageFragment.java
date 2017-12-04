package com.dfa.vinatrip.domains.main.fragment.province.province_detail.view_all.images;

import android.support.v4.app.Fragment;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.Province;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

/**
 * Created by duonghd on 10/6/2017.
 */

@EFragment(R.layout.fragment_province_detail_images)
public class ImageFragment extends Fragment {
    @FragmentArg
    protected Province province;
    
    private int page = 1;
    private int per_page = 10;
    
    @AfterViews
    void init() {
        
    }
}
