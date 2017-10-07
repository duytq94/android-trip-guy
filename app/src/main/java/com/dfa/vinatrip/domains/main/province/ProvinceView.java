package com.dfa.vinatrip.domains.main.province;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.Province;

import java.util.List;

/**
 * Created by duonghd on 10/6/2017.
 */

public interface ProvinceView extends BaseMvpView {

    void getProvinceSuccess(List<Province> provinces);
}
