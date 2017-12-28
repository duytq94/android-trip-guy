package com.dfa.vinatrip.domains.province_detail.view_all.place.place_detail.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.chat.ShowFullPhotoActivity_;
import com.dfa.vinatrip.models.response.place.PlaceImage;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duonghd on 12/5/2017.
 * duonghd1307@gmail.com
 */

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ViewHolder> {
    private Context context;
    private List<PlaceImage> placeImages;
    private ArrayList<String> tempUrls;
    private DisplayImageOptions imageOptions;
    private ImageLoader imageLoader;

    public RecyclerImageAdapter(Context context, List<PlaceImage> placeImages) {
        this.context = context;
        this.placeImages = placeImages;
        this.tempUrls = new ArrayList<>();
        for (PlaceImage placeImage : placeImages) {
            tempUrls.add(placeImage.getUrl());
        }
        this.imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_green)
                .showImageForEmptyUri(R.drawable.photo_not_available)
                .showImageOnFail(R.drawable.photo_not_available)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_province_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        imageLoader.displayImage(placeImages.get(position).getUrl(), holder.ivImageView, imageOptions,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        holder.rotateLoading.setVisibility(View.VISIBLE);
                        holder.rotateLoading.start();
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        holder.rotateLoading.setVisibility(View.GONE);
                        holder.rotateLoading.stop();
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        holder.rotateLoading.setVisibility(View.GONE);
                        holder.rotateLoading.stop();
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        holder.rotateLoading.setVisibility(View.GONE);
                        holder.rotateLoading.stop();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return placeImages.size();
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
