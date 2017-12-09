package com.dfa.vinatrip.domains.province_detail.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.chat.ShowFullPhotoActivity_;
import com.dfa.vinatrip.models.response.province_image.ProvinceImageResponse;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duonghd on 11/3/2017.
 */

public class RecyclerProvinceImageAdapter extends RecyclerView.Adapter<RecyclerProvinceImageAdapter.ViewHolder> {
    private Context context;
    private List<ProvinceImageResponse> imageResponses;
    private ArrayList<String> tempUrls;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerProvinceImageAdapter(Context context, List<ProvinceImageResponse> imageResponses) {
        this.context = context;
        this.imageResponses = imageResponses;
        this.tempUrls = new ArrayList<>();
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

    public void setListTempUrl(){
        for (ProvinceImageResponse provinceImageResponse : imageResponses) {
            tempUrls.add(provinceImageResponse.getUrl());
        }
    }

    @Override
    public RecyclerProvinceImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_province_image, parent, false);
        return new RecyclerProvinceImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerProvinceImageAdapter.ViewHolder holder, int position) {
        ProvinceImageResponse image = imageResponses.get(position);

        imageLoader.displayImage(image.getUrl(), holder.ivImageView, imageOptions, new ImageLoadingListener() {
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
    }

    @Override
    public int getItemCount() {
        return imageResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImageView;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImageView = (ImageView) itemView.findViewById(R.id.item_recycler_province_image_iv_photos);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_province_image_rotate_loading);

            itemView.setOnClickListener(v -> ShowFullPhotoActivity_.intent(context)
                    .listUrl(tempUrls).position(getAdapterPosition()).start());
        }
    }
}
