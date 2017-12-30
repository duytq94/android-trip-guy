package com.dfa.vinatrip.domains.province_detail.view_all.festival;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.festival.FestivalResponse;

import java.util.List;

/**
 * Created by duonghd on 12/29/2017.
 * duonghd1307@gmail.com
 */

public interface FestivalSearchView extends BaseMvpView {
    void getFestivalsSuccess(List<FestivalResponse> festivalResponses);
}
