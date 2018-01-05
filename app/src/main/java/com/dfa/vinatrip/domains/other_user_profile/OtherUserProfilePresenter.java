package com.dfa.vinatrip.domains.other_user_profile;

import com.dfa.vinatrip.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duonghd on 1/5/2018.
 * duonghd1307@gmail.com
 */

public class OtherUserProfilePresenter extends BasePresenter<OtherUserProfileView> {

    @Inject
    public OtherUserProfilePresenter(EventBus eventBus) {
        super(eventBus);
    }
}
