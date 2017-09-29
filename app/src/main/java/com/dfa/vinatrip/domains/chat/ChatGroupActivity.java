package com.dfa.vinatrip.domains.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.BaseMessage;
import com.dfa.vinatrip.services.DataService;
import com.google.gson.Gson;
import com.nhancv.nutc.NUtc;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.dfa.vinatrip.models.TypeMessage.text;

@EActivity(R.layout.activity_chat_group)
public class ChatGroupActivity extends BaseActivity<ChatGroupView, ChatGroupPresenter>
        implements ChatGroupView {

    @Bean
    DataService dataService;

    @ViewById(R.id.activity_chat_group_rv_list)
    protected RecyclerView rvList;

    @ViewById(R.id.activity_chat_group_et_input)
    protected EditText etInput;

    @ViewById(R.id.activity_chat_group_ll_send)
    protected LinearLayout llSend;

    private ChatGroupAdapter adapter;
    private List<BaseMessage> baseMessageList;
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
            socket.connect();
            socket.emit("join_room", dataService.getCurrentUser().getNickname());

            baseMessageList = new ArrayList<>();
            adapter = new ChatGroupAdapter(dataService.getCurrentUser().getNickname(), baseMessageList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvList.setLayoutManager(layoutManager);
            rvList.setAdapter(adapter);

            presenter.getHistory(123, 1, 10);

            socket.on("receive_message", args -> runOnUiThread(() -> {
                BaseMessage baseMessage =
                        new Gson().fromJson(args[0].toString(), BaseMessage.class);
                baseMessageList.add(baseMessage);
                adapter.notifyDataSetChanged();
                rvList.scrollToPosition(baseMessageList.size() - 1);
            }));

        } catch (URISyntaxException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.activity_chat_group_ll_send)
    public void onLlSendClick() {
        if (!etInput.getText().toString().trim().isEmpty()) {
            String content = etInput.getText().toString();
            long timestamp = NUtc.getUtcNow();

            socket.emit("send_message", content, "123", timestamp, text);
            etInput.setText("");
            BaseMessage baseMessage = new BaseMessage(content, timestamp,
                    dataService.getCurrentUser().getNickname(), "123", text);
            baseMessageList.add(baseMessage);
            adapter.notifyDataSetChanged();
            rvList.scrollToPosition(baseMessageList.size() - 1);
        }
    }

    @Override
    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void getHistorySuccess(List<BaseMessage> baseMessageList) {
        this.baseMessageList.addAll(baseMessageList);
        adapter.notifyDataSetChanged();
        rvList.scrollToPosition(baseMessageList.size() - 1);
    }

    @Override
    public void getDataFail(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        socket.off("disconnect");
        socket.disconnect();
        finish();
    }
}
