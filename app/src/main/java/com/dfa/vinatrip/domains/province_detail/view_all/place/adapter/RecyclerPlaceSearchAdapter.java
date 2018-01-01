package com.dfa.vinatrip.domains.province_detail.view_all.place.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.domains.province_detail.view_all.place.PlaceSearchActivity;
import com.dfa.vinatrip.domains.province_detail.view_all.place.place_detail.PlaceDetailActivity_;
import com.dfa.vinatrip.models.response.place.PlaceResponse;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duonghd on 10/8/2017.
 * duonghd1307@gmail.com
 */

public class RecyclerPlaceSearchAdapter extends RecyclerView.Adapter<RecyclerPlaceSearchAdapter.ViewHolder> {
    private Context context;
    private List<PlaceResponse> placeResponses;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerPlaceSearchAdapter(Context context) {
        this.context = context;
        this.placeResponses = new ArrayList<>();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_place_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaceResponse place = placeResponses.get(position);

        holder.tvPlaceName.setText(place.getName());
        holder.srbPlaceRate.setRating(place.getStar());
        holder.tvPlaceReview.setText(String.format("%s đánh giá", place.getReview()));
        holder.tvPlaceAddress.setText(place.getAddress());

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
    }

    @Override
    public int getItemCount() {
        return placeResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPlaceAvatar;
        private TextView tvPlaceName;
        private TextView tvPlaceAddress;
        private SimpleRatingBar srbPlaceRate;
        private TextView tvPlaceReview;
        private TextView tvPlaceDistance;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPlaceAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_place_search_iv_avatar);
            tvPlaceName = (TextView) itemView.findViewById(R.id.item_recycler_place_search_tv_name);
            tvPlaceAddress = (TextView) itemView.findViewById(R.id.item_recycler_place_search_tv_address);
            srbPlaceRate = (SimpleRatingBar) itemView.findViewById(R.id.item_recycler_place_search_srb_rate);
            tvPlaceReview = (TextView) itemView.findViewById(R.id.item_recycler_place_search_tv_number_of_feedback);
            tvPlaceDistance = (TextView) itemView.findViewById(R.id.item_recycler_place_search_tv_distance);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_place_search_rotate_loading);

            itemView.setOnClickListener(v -> {
                PlaceDetailActivity_.intent(context).placeResponse(placeResponses.get(getAdapterPosition())).start();
                ((PlaceSearchActivity) context).finish();
            });
        }
    }

    /*
    * filter
    **/
    public void setFilter(List<PlaceResponse> placeFilter) {
        placeResponses.clear();
        placeResponses.addAll(placeFilter);
        notifyDataSetChanged();
    }
}