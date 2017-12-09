package com.dfa.vinatrip.domains.province_detail.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.event.EventResponse;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by duonghd on 12/8/2017.
 */

public class RecyclerProvinceEventAdapter extends RecyclerView.Adapter<RecyclerProvinceEventAdapter.ViewHolder> {
    private Context context;
    private Province province;
    private List<EventResponse> eventResponses;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerProvinceEventAdapter(Context context, Province province, List<EventResponse> eventResponses) {
        this.context = context;
        this.province = province;
        this.eventResponses = eventResponses;
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_green)
                .showImageForEmptyUri(R.drawable.photo_not_available)
                .showImageOnFail(R.drawable.photo_not_available)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_province_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventResponse event = eventResponses.get(position);
        if (position != eventResponses.size() - 1) {
            holder.cvViewMain.setVisibility(View.VISIBLE);
            holder.cvViewAll.setVisibility(View.GONE);
            holder.tvEventTime.setText(event.getTime());
            holder.tvEventName.setText(event.getName());

            imageLoader.displayImage(event.getAvatar(), holder.ivEventAvatar, imageOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.rotateLoading.setVisibility(View.VISIBLE);
                    holder.rotateLoading.start();
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.rotateLoading.setVisibility(View.GONE);
                    holder.rotateLoading.stop();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.rotateLoading.setVisibility(View.GONE);
                    holder.rotateLoading.stop();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.rotateLoading.setVisibility(View.GONE);
                    holder.rotateLoading.stop();
                }
            });
        } else {
            holder.cvViewMain.setVisibility(View.GONE);
            holder.cvViewAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return eventResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cvViewMain;
        private CardView cvViewAll;
        private ImageView ivEventAvatar;
        private TextView tvEventName;
        private TextView tvEventTime;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            cvViewMain = (CardView) itemView.findViewById(R.id.item_recycler_province_event_cv_view_main);
            cvViewAll = (CardView) itemView.findViewById(R.id.item_recycler_province_event_cv_view_all);
            ivEventAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_province_event_iv_image);
            tvEventName = (TextView) itemView.findViewById(R.id.item_recycler_province_event_tv_name);
            tvEventTime = (TextView) itemView.findViewById(R.id.item_recycler_province_event_tv_time);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_province_event_rotate_loading);
        }
    }
}
