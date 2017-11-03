package com.dfa.vinatrip.domains.location;

import com.dfa.vinatrip.base.BaseMvpView;

import java.util.List;

/**
 * Created by duytq on 10/8/2017.
 */

public interface LocationGroupView extends BaseMvpView {
    void getLastLocationSuccess(List<UserLocation> userLocationList);
}
