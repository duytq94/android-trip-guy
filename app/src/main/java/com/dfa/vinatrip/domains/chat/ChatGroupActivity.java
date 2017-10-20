package com.dfa.vinatrip.domains.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.TypeMessage;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.AdapterChatListener;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.dfa.vinatrip.widgets.ToplessRecyclerViewScrollListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.dfa.vinatrip.ApiUrls.SERVER_SOCKET_CHAT;
import static com.dfa.vinatrip.models.TypeMessage.image;
import static com.dfa.vinatrip.models.TypeMessage.text;
import static com.sangcomz.fishbun.define.Define.ALBUM_REQUEST_CODE;

@EActivity(R.layout.activity_chat_group)
public class ChatGroupActivity extends BaseActivity<ChatGroupView, ChatGroupPresenter>
        implements ChatGroupView, AdapterChatListener {

    @Bean
    DataService dataService;

    @Extra
    protected Plan plan;

    @Extra
    protected ArrayList<UserFriend> userFriendList;

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
    private ImageLoader imageLoader;
    private boolean isSendPhoto;

    private ChatGroupAdapter adapter;
    private List<BaseMessage> baseMessageList;
    private Socket socket;
    private Gson gson;

    private Map<String, String> mapAvatar;
    private Map<String, String> mapNickname;

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

            socket.emit("join_room", dataService.getCurrentUser().getEmail(), plan.getId());

            imageLoader = ImageLoader.getInstance();
            gson = new Gson();

            baseMessageList = new ArrayList<>();
            photoSelectedList = new ArrayList<>();
            isSendPhoto = false;

            setupHashMap();
            setupAppBar();
            setupChatAdapter();
            setupPhotoAdapter();

            presenter.getHistory(plan.getId(), 1, 10);
            presenter.getStatus(plan.getId());

            socket.on("receive_message", args -> {
                BaseMessage baseMessage = gson.fromJson(args[0].toString(), BaseMessage.class);
                baseMessageList.add(baseMessage);
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    rvList.scrollToPosition(baseMessageList.size() - 1);
                });
            });

            socket.on("a_user_join_room", args -> {
                for (int i = 0; i < userFriendList.size(); i++) {
                    UserFriend userFriend = userFriendList.get(i);
                    JSONObject jsonObject = (JSONObject) args[0];
                    String username = "";
                    try {
                        username = jsonObject.getString("username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (userFriend.getEmail().equals(username)) {
                        userFriend.setIsOnline(true);
                        break;
                    }
                }
            });

            socket.on("a_user_leave_room", args -> {
                for (int i = 0; i < userFriendList.size(); i++) {
                    UserFriend userFriend = userFriendList.get(i);
                    JSONObject jsonObject = (JSONObject) args[0];
                    String username = "";
                    try {
                        username = jsonObject.getString("username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (userFriend.getEmail().equals(username)) {
                        userFriend.setIsOnline(false);
                        break;
                    }
                }
            });

        } catch (URISyntaxException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setupHashMap() {
        mapAvatar = new HashMap<>();
        mapNickname = new HashMap<>();

        for (int i = 0; i < userFriendList.size(); i++) {
            mapAvatar.put(userFriendList.get(i).getEmail(), userFriendList.get(i).getAvatar());
            mapNickname.put(userFriendList.get(i).getEmail(), userFriendList.get(i).getNickname());
        }
    }

    public void setupAppBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Thảo luận");

            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setupChatAdapter() {
        adapter = new ChatGroupAdapter(dataService.getCurrentUser().getEmail(), baseMessageList, mapAvatar, mapNickname, this);

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
            sendMessage(etInput.getText().toString(), text);
        }
        if (lvPhotoSelected.getVisibility() == View.VISIBLE && !isSendPhoto) {
            isSendPhoto = true;
            prepareUpload(photoSelectedList.get(0));
            photoSelectedAdapter.notifyDataSetChanged();
        }
    }

    public void prepareUpload(Uri uriPhoto) {
        try {
            Bitmap originBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriPhoto);
            Bitmap scaleBitmap = AppUtil.scaleDown(originBitmap, 1080, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            scaleBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayPhoto = baos.toByteArray();

            // Get the path and name photo be upload
            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReferenceFromUrl("gs://tripguy-10864.appspot.com")
                    .child("Chat")
                    .child(System.currentTimeMillis() + ".jpg");

            UploadTask uploadTask = storageReference.putBytes(byteArrayPhoto);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                @SuppressWarnings("VisibleForTests")
                Uri linkDownload = taskSnapshot.getDownloadUrl();
                sendMessage(String.valueOf(linkDownload), image);

                if (photoSelectedList.size() > 0) {
                    photoSelectedList.remove(0);
                    photoSelectedAdapter.clear();
                    photoSelectedAdapter.addAll(photoSelectedList);
                    photoSelectedAdapter.notifyDataSetChanged();
                }
                if (photoSelectedList.size() > 0) {
                    prepareUpload(photoSelectedList.get(0));
                } else {
                    lvPhotoSelected.setVisibility(View.GONE);
                    isSendPhoto = false;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String content, TypeMessage typeMessage) {
        long timestamp = NUtc.getUtcNow();
        BaseMessage baseMessage;

        switch (typeMessage) {
            case text:
                socket.emit("send_message", content, timestamp, text);
                etInput.setText("");
                baseMessage = new BaseMessage(content, timestamp,
                        dataService.getCurrentUser().getEmail(), plan.getId(), text);
                baseMessageList.add(baseMessage);
                adapter.notifyDataSetChanged();
                rvList.scrollToPosition(baseMessageList.size() - 1);
                break;

            case image:
                socket.emit("send_message", content, timestamp, image);
                etInput.setText("");
                baseMessage = new BaseMessage(content, timestamp,
                        dataService.getCurrentUser().getEmail(), plan.getId(), image);
                baseMessageList.add(baseMessage);
                adapter.notifyDataSetChanged();
                rvList.scrollToPosition(baseMessageList.size() - 1);
                break;

            default:
                Toast.makeText(this, "Can not send message", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Click(R.id.activity_chat_group_ll_add_photo)
    public void onLlAddPhotoClick() {
        if (lvPhotoSelected.getVisibility() == View.GONE) {
            photoSelectedList.clear();
            etInput.setText("");
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
    public void apiError(Throwable throwable) {

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
    public void getStatusSuccess(List<StatusUserChat> statusUserChatList) {
        for (int i = 0; i < statusUserChatList.size(); i++) {
            StatusUserChat statusUserChat = statusUserChatList.get(i);
            for (int j = 0; j < userFriendList.size(); j++) {
                UserFriend userFriend = userFriendList.get(j);
                if (userFriend.getEmail().equals(statusUserChat.getEmail())) {
                    userFriend.setIsOnline(statusUserChat.getIsOnline());
                    break;
                }
            }
        }
    }

    @Override
    public void getDataFail(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.iconInfo) {
            ShowUserOnlineActivity_.intent(this).userFriendList(userFriendList).start();
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

    @Override
    public void onPhotoClick(String url) {
        ShowFullPhotoActivity_.intent(this).url(url).start();
    }
}
