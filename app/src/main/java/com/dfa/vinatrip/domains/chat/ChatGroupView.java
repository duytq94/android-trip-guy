package com.dfa.vinatrip.domains.chat;

import com.dfa.vinatrip.base.BaseMvpView;

import java.util.List;

/**
 * Created by duytq on 09/28/2017.
 */

public interface ChatGroupView extends BaseMvpView {
    void getHistorySuccess(List<BaseMessage> baseMessageList, int page);

    void getStatusSuccess(List<StatusUserChat> statusUserChatList);

    void getDataFail(Throwable throwable);
}
