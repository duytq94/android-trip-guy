package com.dfa.vinatrip.domains.main.province;

import com.dfa.vinatrip.base.BaseMvpView;

import java.util.List;

/**
 * Created by duonghd on 10/6/2017.
 */

public interface ProvinceView extends BaseMvpView {
    
    void getProvinceSuccess(List<com.dfa.vinatrip.models.response.Province> provinces);
}
