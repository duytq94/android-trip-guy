package com.dfa.vinatrip.domains.chat;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.TypeMessage;
import com.dfa.vinatrip.models.response.BaseMessage;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by duytq on 09/28/2017.
 */

public class ChatGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String currentUser;
    private List<BaseMessage> baseMessageList;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public ChatGroupAdapter(String currentUser) {
        setHasStableIds(true);
        this.currentUser = currentUser;
        this.imageLoader = ImageLoader.getInstance();
        this.imageOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.bg_orange)
//                .showImageForEmptyUri(R.drawable.bg_orange)
//                .showImageOnFail(R.drawable.bg_orange)
                .build();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MessageHolder messageHolder = (MessageHolder) holder;

        BaseMessage baseMessage = baseMessageList.get(position);
        boolean isLeft = checkLeft(baseMessage);

        if (isLeft) {
            messageHolder.llGroupLeft.setVisibility(View.VISIBLE);
            messageHolder.flGroupRight.setVisibility(View.GONE);

            if (baseMessage.getTypeMessage().equals(TypeMessage.text)) {
                messageHolder.tvMsgLeft.setVisibility(View.VISIBLE);
                messageHolder.psivPhotoLeft.setVisibility(View.GONE);

                messageHolder.tvMsgLeft.setText(baseMessage.getContent());
            } else {
                messageHolder.tvMsgLeft.setVisibility(View.GONE);
                messageHolder.psivPhotoLeft.setVisibility(View.VISIBLE);

                imageLoader.displayImage(baseMessage.getContent(), messageHolder.psivPhotoLeft, imageOptions,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {
                                messageHolder.rotateLoadingLeft.setVisibility(View.VISIBLE);
                                messageHolder.rotateLoadingLeft.start();
                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                messageHolder.rotateLoadingLeft.setVisibility(View.GONE);
                                messageHolder.rotateLoadingLeft.stop();
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                messageHolder.rotateLoadingLeft.setVisibility(View.GONE);
                                messageHolder.rotateLoadingLeft.stop();
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                messageHolder.rotateLoadingLeft.setVisibility(View.GONE);
                                messageHolder.rotateLoadingLeft.stop();
                            }
                        });
            }

        } else {
            messageHolder.llGroupLeft.setVisibility(View.GONE);
            messageHolder.flGroupRight.setVisibility(View.VISIBLE);

            if (baseMessage.getTypeMessage().equals(TypeMessage.text)) {
                messageHolder.tvMsgRight.setVisibility(View.VISIBLE);
                messageHolder.psivPhotoRight.setVisibility(View.GONE);

                messageHolder.tvMsgRight.setText(baseMessage.getContent());
            } else {
                messageHolder.tvMsgRight.setVisibility(View.GONE);
                messageHolder.psivPhotoRight.setVisibility(View.VISIBLE);

                imageLoader.displayImage(baseMessage.getContent(), messageHolder.psivPhotoRight, imageOptions,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {
                                messageHolder.rotateLoadingRight.setVisibility(View.VISIBLE);
                                messageHolder.rotateLoadingRight.start();
                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                messageHolder.rotateLoadingRight.setVisibility(View.GONE);
                                messageHolder.rotateLoadingRight.stop();
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                messageHolder.rotateLoadingRight.setVisibility(View.GONE);
                                messageHolder.rotateLoadingRight.stop();
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                messageHolder.rotateLoadingRight.setVisibility(View.GONE);
                                messageHolder.rotateLoadingRight.stop();
                            }
                        });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (baseMessageList != null) {
            return baseMessageList.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<BaseMessage> baseMessageList) {
        this.baseMessageList = baseMessageList;
    }

    public boolean checkLeft(BaseMessage baseMessage) {
        if (baseMessage.getFrom().equals(currentUser)) {
            return false;
        } else {
            return true;
        }
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        private LinearLayout llGroupLeft;
        private FrameLayout flGroupRight;
        private ImageView civLeft;
        private TextView tvMsgLeft, tvMsgRight;
        private PorterShapeImageView psivPhotoLeft, psivPhotoRight;
        private RotateLoading rotateLoadingRight, rotateLoadingLeft;

        public MessageHolder(View itemView) {
            super(itemView);
            // Left content
            llGroupLeft = itemView.findViewById(R.id.item_chat_ll_group_left);
            civLeft = (CircleImageView) itemView.findViewById(R.id.item_chat_civ_left);
            tvMsgLeft = itemView.findViewById(R.id.item_chat_tv_msg_left);
            psivPhotoLeft = itemView.findViewById(R.id.item_chat_psiv_photo_left);
            rotateLoadingLeft = itemView.findViewById(R.id.item_chat_rotate_loading_left);

            // Right content
            flGroupRight = itemView.findViewById(R.id.item_chat_ll_group_right);
            tvMsgRight = itemView.findViewById(R.id.item_chat_tv_msg_right);
            psivPhotoRight = itemView.findViewById(R.id.item_chat_psiv_photo_right);
            rotateLoadingRight = itemView.findViewById(R.id.item_chat_rotate_loading_right);
        }
    }

}