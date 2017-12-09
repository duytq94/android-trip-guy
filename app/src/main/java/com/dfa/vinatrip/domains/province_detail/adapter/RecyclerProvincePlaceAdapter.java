package com.dfa.vinatrip.domains.province_detail.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.models.response.place.PlaceResponse;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by duonghd on 11/3/2017.
 */

public class RecyclerProvincePlaceAdapter extends RecyclerView.Adapter<RecyclerProvincePlaceAdapter.ViewHolder> {
    private Context context;
    private List<PlaceResponse> placeResponses;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerProvincePlaceAdapter(Context context, List<PlaceResponse> placeResponses) {
        this.context = context;
        this.placeResponses = placeResponses;
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
                R.layout.item_recycler_province_place, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaceResponse place = placeResponses.get(position);
        if (position != placeResponses.size() - 1) {
            holder.llMain.setVisibility(View.VISIBLE);
            holder.cvViewAll.setVisibility(View.GONE);
            
            holder.tvPlaceName.setText(place.getName());

            imageLoader.displayImage(place.getAvatar(), holder.ivPlaceAvatar, imageOptions, new ImageLoadingListener() {
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
            holder.llMain.setVisibility(View.GONE);
            holder.cvViewAll.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public int getItemCount() {
        return placeResponses.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llMain;
        private CardView cvViewAll;
        private ImageView ivPlaceAvatar;
        private TextView tvPlaceName;
        private SimpleRatingBar srbPlaceRate;
        private TextView tvPlaceReviews;
        private TextView tvPlaceDistance;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            llMain = (LinearLayout) itemView.findViewById(R.id.item_recycler_province_place_ll_main);
            cvViewAll = (CardView) itemView.findViewById(R.id.item_recycler_province_place_cv_view_all);
            ivPlaceAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_province_place_iv_image);
            tvPlaceName = (TextView) itemView.findViewById(R.id.item_recycler_province_place_tv_name);
            srbPlaceRate = (SimpleRatingBar) itemView.findViewById(R.id.item_recycler_province_place_srb_rating);
            tvPlaceReviews = (TextView) itemView.findViewById(R.id.item_recycler_province_place_tv_reviews);
            tvPlaceDistance = (TextView) itemView.findViewById(R.id.item_recycler_province_place_tv_distance);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_province_place_rotate_loading);
        }
    }
}
