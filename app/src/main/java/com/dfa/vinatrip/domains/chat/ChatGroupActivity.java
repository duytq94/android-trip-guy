package com.dfa.vinatrip.domains.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
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

import com.beesightsoft.caf.exceptions.ApiThrowable;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.chat.adapter.ChatGroupAdapter;
import com.dfa.vinatrip.domains.chat.adapter.StickerAdapter;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.TypeMessage;
import com.dfa.vinatrip.models.TypeSticker;
import com.dfa.vinatrip.utils.AdapterChatListener;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.KeyboardListener;
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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
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
import static com.dfa.vinatrip.models.TypeMessage.sticker;
import static com.dfa.vinatrip.models.TypeMessage.text;
import static com.dfa.vinatrip.utils.Constants.A_USER_JOIN_ROOM;
import static com.dfa.vinatrip.utils.Constants.A_USER_LEAVE_ROOM;
import static com.dfa.vinatrip.utils.Constants.EMAIL;
import static com.dfa.vinatrip.utils.Constants.FOLDER_STORAGE_CHAT;
import static com.dfa.vinatrip.utils.Constants.JOIN_ROOM;
import static com.dfa.vinatrip.utils.Constants.MAX_LENGTH_MESSAGE;
import static com.dfa.vinatrip.utils.Constants.PAGE_SIZE;
import static com.dfa.vinatrip.utils.Constants.RECEIVE_MESSAGE;
import static com.dfa.vinatrip.utils.Constants.SEND_MESSAGE;
import static com.dfa.vinatrip.utils.Constants.URL_STORAGE;
import static com.sangcomz.fishbun.define.Define.ALBUM_REQUEST_CODE;

@EActivity(R.layout.activity_chat_group)
public class ChatGroupActivity extends BaseActivity<ChatGroupView, ChatGroupPresenter>
        implements ChatGroupView, AdapterChatListener, StickerAdapter.StickerListener, KeyboardListener.KeyboardVisibilityListener {

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
    @ViewById(R.id.activity_chat_group_rv_sticker)
    protected RecyclerView rvSticker;
    @ViewById(R.id.activity_chat_group_ll_sticker)
    protected LinearLayout llSticker;
    @ViewById(R.id.activity_chat_group_ll_input)
    protected LinearLayout llInput;
    @ViewById(R.id.activity_chat_group_ll_no_message)
    protected LinearLayout llNoMessage;

    private ArrayList<Uri> photoSelectedList;
    private QuickAdapter<Uri> photoSelectedAdapter;
    private ImageLoader imageLoader;
    private boolean isSendPhoto;

    private ChatGroupAdapter chatGroupAdapter;
    private StickerAdapter stickerAdapter;
    private List<BaseMessage> baseMessageList;
    private Socket socket;
    private Gson gson;
    private List<Integer> listStickerEmotion;
    private List<Integer> listStickerMimi;
    private List<Integer> listStickerRilakkuma;

    private Map<String, String> mapAvatar;
    private Map<String, String> mapNickname;
    private Map<String, Integer> mapSticker;

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

            socket.emit(JOIN_ROOM, presenter.getCurrentUser().getEmail(), plan.getId());

            imageLoader = ImageLoader.getInstance();

            gson = new Gson();
            KeyboardListener.setEventListener(this, this);

            baseMessageList = new ArrayList<>();
            photoSelectedList = new ArrayList<>();
            isSendPhoto = false;

            setupHashMap();
            setupAppBar();
            setupChatAdapter();
            setupPhotoAdapter();
            setupStickerAdapter();

            presenter.getHistory(plan.getId(), 1, PAGE_SIZE);

            if (plan.isExpired()) {
                llInput.setVisibility(View.GONE);
            } else {
                llInput.setVisibility(View.VISIBLE);

                socket.on(RECEIVE_MESSAGE, args -> {
                    BaseMessage baseMessage = gson.fromJson(args[0].toString(), BaseMessage.class);
                    baseMessageList.add(baseMessage);
                    runOnUiThread(() -> {
                        chatGroupAdapter.notifyDataSetChanged();
                        rvList.scrollToPosition(baseMessageList.size() - 1);
                    });
                });

                socket.on(A_USER_JOIN_ROOM, args -> {
                    for (int i = 0; i < plan.getInvitedFriendList().size(); i++) {
                        UserInPlan userInPlan = plan.getInvitedFriendList().get(i);
                        JSONObject jsonObject = (JSONObject) args[0];
                        String email = "";
                        try {
                            email = jsonObject.getString(EMAIL);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (userInPlan.getEmail().equals(email)) {
                            userInPlan.setIsOnline(1);
                            break;
                        }
                    }
                });

                socket.on(A_USER_LEAVE_ROOM, args -> {
                    for (int i = 0; i < plan.getInvitedFriendList().size(); i++) {
                        UserInPlan userInPlan = plan.getInvitedFriendList().get(i);
                        JSONObject jsonObject = (JSONObject) args[0];
                        String email = "";
                        try {
                            email = jsonObject.getString(EMAIL);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (userInPlan.getEmail().equals(email)) {
                            userInPlan.setIsOnline(0);
                            break;
                        }
                    }
                });
            }
        } catch (URISyntaxException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setupHashMap() {
        mapAvatar = new HashMap<>();
        mapNickname = new HashMap<>();

        for (int i = 0; i < plan.getInvitedFriendList().size(); i++) {
            mapAvatar.put(plan.getInvitedFriendList().get(i).getEmail(), plan.getInvitedFriendList().get(i).getAvatar());
            mapNickname.put(plan.getInvitedFriendList().get(i).getEmail(), plan.getInvitedFriendList().get(i).getUsername());
        }

        mapSticker = new HashMap<>();
        mapSticker.put("emotion1", R.drawable.emotion1);
        mapSticker.put("emotion2", R.drawable.emotion2);
        mapSticker.put("emotion3", R.drawable.emotion3);
        mapSticker.put("emotion4", R.drawable.emotion4);
        mapSticker.put("emotion5", R.drawable.emotion5);
        mapSticker.put("emotion6", R.drawable.emotion6);
        mapSticker.put("emotion7", R.drawable.emotion7);
        mapSticker.put("emotion8", R.drawable.emotion8);
        mapSticker.put("emotion9", R.drawable.emotion9);
        mapSticker.put("emotion10", R.drawable.emotion10);
        mapSticker.put("emotion11", R.drawable.emotion11);
        mapSticker.put("emotion12", R.drawable.emotion12);
        mapSticker.put("emotion13", R.drawable.emotion13);
        mapSticker.put("mimi1", R.drawable.mimi1);
        mapSticker.put("mimi2", R.drawable.mimi2);
        mapSticker.put("mimi3", R.drawable.mimi3);
        mapSticker.put("mimi4", R.drawable.mimi4);
        mapSticker.put("mimi5", R.drawable.mimi5);
        mapSticker.put("mimi6", R.drawable.mimi6);
        mapSticker.put("mimi7", R.drawable.mimi7);
        mapSticker.put("mimi8", R.drawable.mimi8);
        mapSticker.put("mimi9", R.drawable.mimi9);
        mapSticker.put("rilakkuma1", R.drawable.rilakkuma1);
        mapSticker.put("rilakkuma2", R.drawable.rilakkuma2);
        mapSticker.put("rilakkuma3", R.drawable.rilakkuma3);
        mapSticker.put("rilakkuma4", R.drawable.rilakkuma4);
        mapSticker.put("rilakkuma5", R.drawable.rilakkuma5);
        mapSticker.put("rilakkuma6", R.drawable.rilakkuma6);
        mapSticker.put("rilakkuma7", R.drawable.rilakkuma7);
        mapSticker.put("rilakkuma8", R.drawable.rilakkuma8);
        mapSticker.put("rilakkuma9", R.drawable.rilakkuma9);
        mapSticker.put("rilakkuma10", R.drawable.rilakkuma10);
        mapSticker.put("rilakkuma11", R.drawable.rilakkuma11);
        mapSticker.put("rilakkuma12", R.drawable.rilakkuma12);


        listStickerEmotion = new ArrayList<>();
        listStickerEmotion.add(R.drawable.emotion1);
        listStickerEmotion.add(R.drawable.emotion2);
        listStickerEmotion.add(R.drawable.emotion3);
        listStickerEmotion.add(R.drawable.emotion4);
        listStickerEmotion.add(R.drawable.emotion5);
        listStickerEmotion.add(R.drawable.emotion6);
        listStickerEmotion.add(R.drawable.emotion7);
        listStickerEmotion.add(R.drawable.emotion8);
        listStickerEmotion.add(R.drawable.emotion9);
        listStickerEmotion.add(R.drawable.emotion10);
        listStickerEmotion.add(R.drawable.emotion11);
        listStickerEmotion.add(R.drawable.emotion12);
        listStickerEmotion.add(R.drawable.emotion13);

        listStickerMimi = new ArrayList<>();
        listStickerMimi.add(R.drawable.mimi1);
        listStickerMimi.add(R.drawable.mimi2);
        listStickerMimi.add(R.drawable.mimi3);
        listStickerMimi.add(R.drawable.mimi4);
        listStickerMimi.add(R.drawable.mimi5);
        listStickerMimi.add(R.drawable.mimi6);
        listStickerMimi.add(R.drawable.mimi7);
        listStickerMimi.add(R.drawable.mimi8);
        listStickerMimi.add(R.drawable.mimi9);

        listStickerRilakkuma = new ArrayList<>();
        listStickerRilakkuma.add(R.drawable.rilakkuma1);
        listStickerRilakkuma.add(R.drawable.rilakkuma2);
        listStickerRilakkuma.add(R.drawable.rilakkuma3);
        listStickerRilakkuma.add(R.drawable.rilakkuma4);
        listStickerRilakkuma.add(R.drawable.rilakkuma5);
        listStickerRilakkuma.add(R.drawable.rilakkuma6);
        listStickerRilakkuma.add(R.drawable.rilakkuma7);
        listStickerRilakkuma.add(R.drawable.rilakkuma8);
        listStickerRilakkuma.add(R.drawable.rilakkuma9);
        listStickerRilakkuma.add(R.drawable.rilakkuma10);
        listStickerRilakkuma.add(R.drawable.rilakkuma11);
        listStickerRilakkuma.add(R.drawable.rilakkuma12);
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
        chatGroupAdapter = new ChatGroupAdapter(presenter.getCurrentUser().getEmail(), baseMessageList, mapAvatar,
                mapNickname, mapSticker, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(chatGroupAdapter);
        rvList.setHasFixedSize(true);

        ToplessRecyclerViewScrollListener scrollListener = new ToplessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (totalItemsCount >= PAGE_SIZE) {
                    presenter.getHistory(plan.getId(), page, PAGE_SIZE);
                }
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

    public void setupStickerAdapter() {
        stickerAdapter = new StickerAdapter(this);
        stickerAdapter.setList(listStickerEmotion, TypeSticker.emotion.name());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        rvSticker.setLayoutManager(layoutManager);
        rvSticker.setAdapter(stickerAdapter);
        rvSticker.setHasFixedSize(true);
        stickerAdapter.notifyDataSetChanged();
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
                    .getReferenceFromUrl(URL_STORAGE)
                    .child(FOLDER_STORAGE_CHAT)
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

        if (llNoMessage.getVisibility() == View.VISIBLE) {
            llNoMessage.setVisibility(View.GONE);
        }

        switch (typeMessage) {
            case text:
                socket.emit(SEND_MESSAGE, content, timestamp, text);
                etInput.setText("");
                baseMessage = new BaseMessage(content, timestamp,
                        presenter.getCurrentUser().getEmail(), plan.getId(), text);
                baseMessageList.add(baseMessage);
                chatGroupAdapter.notifyDataSetChanged();
                rvList.scrollToPosition(baseMessageList.size() - 1);
                break;

            case image:
                socket.emit(SEND_MESSAGE, content, timestamp, image);
                etInput.setText("");
                baseMessage = new BaseMessage(content, timestamp,
                        presenter.getCurrentUser().getEmail(), plan.getId(), image);
                baseMessageList.add(baseMessage);
                chatGroupAdapter.notifyDataSetChanged();
                rvList.scrollToPosition(baseMessageList.size() - 1);
                break;

            case sticker:
                socket.emit(SEND_MESSAGE, content, timestamp, sticker);
                baseMessage = new BaseMessage(content, timestamp,
                        presenter.getCurrentUser().getEmail(), plan.getId(), sticker);
                baseMessageList.add(baseMessage);
                chatGroupAdapter.notifyDataSetChanged();
                rvList.scrollToPosition(baseMessageList.size() - 1);
                break;

            default:
                Toast.makeText(this, "Can not send message", Toast.LENGTH_SHORT).show();
                break;
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
        ApiThrowable apiThrowable = (ApiThrowable) throwable;
        Toast.makeText(this, apiThrowable.firstErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getHistorySuccess(List<BaseMessage> baseMessageList, int page) {
        if (page < 2) {
            if (baseMessageList.size() > 0) {
                this.baseMessageList.clear();
                this.baseMessageList.addAll(baseMessageList);
                llNoMessage.setVisibility(View.GONE);
            } else {
                llNoMessage.setVisibility(View.VISIBLE);
            }
        } else {
            this.baseMessageList.addAll(0, baseMessageList);
        }
        chatGroupAdapter.notifyDataSetChanged();
        rvList.scrollToPosition(baseMessageList.size() - 1);
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
            ShowUserOnlineActivity_.intent(this)
                    .userInPlanList((ArrayList<UserInPlan>) plan.getInvitedFriendList())
                    .currentUser(presenter.getCurrentUser())
                    .start();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (lvPhotoSelected.getVisibility() == View.VISIBLE || llSticker.getVisibility() == View.VISIBLE) {
            llSticker.setVisibility(View.GONE);
            lvPhotoSelected.setVisibility(View.GONE);
            isSendPhoto = false;
            photoSelectedList.clear();
        } else {
            socket.off(Socket.EVENT_DISCONNECT);
            socket.disconnect();
            finish();
        }
    }

    @OnActivityResult(ALBUM_REQUEST_CODE)
    public void onResultPhoto(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            photoSelectedList.clear();
            photoSelectedList = data.getParcelableArrayListExtra(Define.INTENT_PATH);
            photoSelectedAdapter.clear();
            photoSelectedAdapter.addAll(photoSelectedList);
            photoSelectedAdapter.notifyDataSetChanged();
            lvPhotoSelected.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPhotoClick(String url) {
        ShowFullPhotoActivity_.intent(this).url(url).start();
    }

    @Override
    public void onStickerClick(String position) {
        sendMessage(position, sticker);
    }

    @Click(R.id.activity_chat_group_tv_emotion)
    public void onIvEmotionClick() {
        stickerAdapter.setList(listStickerEmotion, TypeSticker.emotion.name());
        stickerAdapter.notifyDataSetChanged();
    }

    @Click(R.id.activity_chat_group_tv_mimi)
    public void onIvMimiClick() {
        stickerAdapter.setList(listStickerMimi, TypeSticker.mimi.name());
        stickerAdapter.notifyDataSetChanged();
    }

    @Click(R.id.activity_chat_group_tv_rilakkuma)
    public void onTvRilakkumaClick() {
        stickerAdapter.setList(listStickerRilakkuma, TypeSticker.rilakkuma.name());
        stickerAdapter.notifyDataSetChanged();
    }

    @Click(R.id.activity_chat_group_ll_add_photo)
    public void onLlAddPhotoClick() {
        if (lvPhotoSelected.getVisibility() == View.GONE) {
            if (llSticker.getVisibility() == View.VISIBLE) {
                llSticker.setVisibility(View.GONE);
            }
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
                    .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_white))
                    .setAllViewTitle("Tất cả ảnh")
                    .setActionBarTitle("Chọn ảnh")
                    .textOnNothingSelected("Hãy chọn ít nhất 1 ảnh")
                    .startAlbum();
        }
    }

    @Click(R.id.activity_chat_group_ll_add_sticker)
    public void onLlAddStickerClick() {
        if (llSticker.getVisibility() == View.GONE) {
            if (lvPhotoSelected.getVisibility() == View.VISIBLE) {
                lvPhotoSelected.setVisibility(View.GONE);
                photoSelectedList.clear();
            }
            etInput.clearFocus();
            AppUtil.hideKeyBoard(this);
            llSticker.setVisibility(View.VISIBLE);
        } else {
            llSticker.setVisibility(View.GONE);
        }
    }

    @Click(R.id.activity_chat_group_ll_send)
    public void onLlSendClick() {
        String content = etInput.getText().toString().trim();
        if (!content.isEmpty() && content.length() < MAX_LENGTH_MESSAGE) {
            sendMessage(content, text);
        } else {
            if (lvPhotoSelected.getVisibility() == View.GONE) {
                Toast.makeText(this, "Số lượng ký tự không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        }
        if (lvPhotoSelected.getVisibility() == View.VISIBLE && !isSendPhoto) {
            isSendPhoto = true;
            prepareUpload(photoSelectedList.get(0));
            photoSelectedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onKeyboardVisibilityChanged(boolean isOpen) {
        if (isOpen) {
            if (llSticker.getVisibility() == View.VISIBLE) {
                llSticker.setVisibility(View.GONE);
            }
        }
    }
}
