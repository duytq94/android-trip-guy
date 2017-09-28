package com.dfa.vinatrip.domains.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.BaseMessage;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import io.socket.client.IO;
import io.socket.client.Socket;

@EActivity(R.layout.activity_chat_group)
public class ChatGroupActivity extends BaseActivity<ChatGroupView, ChatGroupPresenter>
        implements ChatGroupView {

    @ViewById(R.id.activity_chat_group_rv_list)
    protected RecyclerView rvList;

    @ViewById(R.id.activity_chat_group_et_input)
    protected EditText etInput;

    @ViewById(R.id.activity_chat_group_ll_send)
    protected LinearLayout llSend;

    private Socket socket;

    @App
    protected MainApplication application;

    @Inject
    protected ChatGroupPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerChatGroupComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @NonNull
    @Override
    public ChatGroupPresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        try {
            socket = IO.socket("http://192.168.149.128:3000");

        } catch (URISyntaxException e) {
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void getHistorySuccess(List<BaseMessage> baseMessageList) {

    }

    @Override
    public void getDataFail(Throwable throwable) {

    }
}
