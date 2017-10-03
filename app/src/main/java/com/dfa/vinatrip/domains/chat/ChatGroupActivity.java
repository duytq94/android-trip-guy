package com.dfa.vinatrip.domains.chat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.main.plan.Plan;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.BaseMessage;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.dfa.vinatrip.widgets.ToplessRecyclerViewScrollListener;
import com.google.gson.Gson;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nhancv.nutc.NUtc;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.lucasr.twowayview.TwoWayView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.dfa.vinatrip.ApiUrls.SERVER_SOCKET_CHAT;
import static com.dfa.vinatrip.models.TypeMessage.text;
import static com.sangcomz.fishbun.define.Define.ALBUM_REQUEST_CODE;

@EActivity(R.layout.activity_chat_group)
public class ChatGroupActivity extends BaseActivity<ChatGroupView, ChatGroupPresenter>
        implements ChatGroupView {

    @Bean
    DataService dataService;

    @Extra
    protected Plan plan;

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_chat_group_rv_list)
    protected RecyclerView rvList;
    @ViewById(R.id.activity_chat_group_et_input)
    protected EditText etInput;
    @ViewById(R.id.activity_chat_group_ll_send)
    protected LinearLayout llSend;
    @ViewById(R.id.activity_chat_group_ll_add_photo)
    protected LinearLayout llAddPhoto;
    @ViewById(R.id.activity_chat_group_lv_photo_selected)
    protected TwoWayView lvPhotoSelected;

    private ArrayList<Uri> photoSelectedList;
    private QuickAdapter<Uri> photoSelectedAdapter;
    private ActionBar actionBar;
    private ImageLoader imageLoader;
    private boolean isSendPhoto;

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
            socket = IO.socket(SERVER_SOCKET_CHAT);
            socket.connect();

            socket.emit("join_room", dataService.getCurrentUser().getNickname(), plan.getId());

            imageLoader = ImageLoader.getInstance();

            baseMessageList = new ArrayList<>();
            photoSelectedList = new ArrayList<>();
            isSendPhoto = false;

            setupAppBar();
            setupChatAdapter();
            setupPhotoAdapter();

            presenter.getHistory(plan.getId(), 1, 10);

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

    public void setupAppBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(plan.getName());

            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setupChatAdapter() {
        adapter = new ChatGroupAdapter(dataService.getCurrentUser().getNickname(), baseMessageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
        rvList.setHasFixedSize(true);

        ToplessRecyclerViewScrollListener scrollListener = new ToplessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                presenter.getHistory(plan.getId(), page, 10);
            }
        };

        rvList.addOnScrollListener(scrollListener);
    }

    public void setupPhotoAdapter() {
        photoSelectedAdapter = new QuickAdapter<Uri>(this, R.layout.item_photo_selected) {
            @Override
            protected void convert(BaseAdapterHelper helper, Uri uri) {
                ImageView ivPhoto = helper.getView(R.id.item_photo_selected_iv_photo);
                RotateLoading rotateLoading = helper.getView(R.id.item_photo_selected_rotate_loading);

                imageLoader.displayImage(uri.toString(), ivPhoto);

                if (helper.getPosition() == 0 && isSendPhoto) {
                    rotateLoading.setVisibility(View.VISIBLE);
                    rotateLoading.start();
                } else {
                    rotateLoading.setVisibility(View.GONE);
                    rotateLoading.stop();
                }
            }
        };
        lvPhotoSelected.setAdapter(photoSelectedAdapter);
    }

    @Click(R.id.activity_chat_group_ll_send)
    public void onLlSendClick() {
        if (!etInput.getText().toString().trim().isEmpty()) {
            String content = etInput.getText().toString();
            long timestamp = NUtc.getUtcNow();

            socket.emit("send_message", content, timestamp, text);
            etInput.setText("");
            BaseMessage baseMessage = new BaseMessage(content, timestamp,
                    dataService.getCurrentUser().getNickname(), plan.getId(), text);
            baseMessageList.add(baseMessage);
            adapter.notifyDataSetChanged();
            rvList.scrollToPosition(baseMessageList.size() - 1);
        }
    }

    @Click(R.id.activity_chat_group_ll_add_photo)
    public void onLlAddPhotoClick() {
        FishBun.with(this)
                .MultiPageMode()
                .setMaxCount(4)
                .setMinCount(1)
                .setPickerSpanCount(4)
                .setActionBarColor(Color.parseColor("#228B22"), Color.parseColor("#156915"), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .setArrayPaths(photoSelectedList)
                .setAlbumSpanCount(1, 2)
                .setButtonInAlbumActivity(true)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(true)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp))
                .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_white_48dp))
                .setAllViewTitle("All")
                .setActionBarTitle("Chọn ảnh")
                .textOnNothingSelected("Hãy chọn ít nhất 1 ảnh")
                .startAlbum();
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
    public void getHistorySuccess(List<BaseMessage> baseMessageList, int page) {
        if (page < 1) {
            this.baseMessageList.clear();
            this.baseMessageList.addAll(baseMessageList);
        } else {
            this.baseMessageList.addAll(0, baseMessageList);
            adapter.notifyDataSetChanged();
            rvList.scrollToPosition(baseMessageList.size() - 1);
        }
    }

    @Override
    public void getDataFail(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (lvPhotoSelected.getVisibility() == View.VISIBLE) {
            lvPhotoSelected.setVisibility(View.GONE);
            isSendPhoto = false;
            photoSelectedList.clear();
        } else {
            socket.off(Socket.EVENT_DISCONNECT);
            socket.disconnect();
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ALBUM_REQUEST_CODE) {
                photoSelectedList.clear();
                photoSelectedList = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                photoSelectedAdapter.clear();
                photoSelectedAdapter.addAll(photoSelectedList);
                photoSelectedAdapter.notifyDataSetChanged();
                lvPhotoSelected.setVisibility(View.VISIBLE);
            }
        }
    }
}
