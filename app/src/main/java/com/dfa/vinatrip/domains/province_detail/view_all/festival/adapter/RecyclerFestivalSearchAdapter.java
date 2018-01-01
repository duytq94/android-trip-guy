package com.dfa.vinatrip.domains.province_detail.view_all.festival.adapter;

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
import com.dfa.vinatrip.domains.province_detail.view_all.festival.FestivalSearchActivity;
import com.dfa.vinatrip.domains.province_detail.view_all.festival.festival_detail.FestivalDetailActivity_;
import com.dfa.vinatrip.models.response.festival.FestivalResponse;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duonghd on 12/30/2017.
 * duonghd1307@gmail.com
 */

public class RecyclerFestivalSearchAdapter extends RecyclerView.Adapter<RecyclerFestivalSearchAdapter.ViewHolder> {
    private Context context;
    private List<FestivalResponse> festivalResponses;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerFestivalSearchAdapter(Context context) {
        this.context = context;
        this.festivalResponses = new ArrayList<>();
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
    public RecyclerFestivalSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_festival_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerFestivalSearchAdapter.ViewHolder holder, int position) {
        FestivalResponse festival = festivalResponses.get(position);

        holder.tvFestivalName.setText(festival.getName());
        holder.srbFestivalRate.setRating(festival.getStar());
        holder.tvFestivalReviews.setText(String.format("%s đánh giá", festival.getReview()));
        holder.tvFestivalAddress.setText(festival.getAddress());

        imageLoader.displayImage(festival.getAvatar(), holder.ivFestivalAvatar, imageOptions, new ImageLoadingListener() {
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
        return festivalResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFestivalAvatar;
        private TextView tvFestivalName;
        private TextView tvFestivalAddress;
        private SimpleRatingBar srbFestivalRate;
        private TextView tvFestivalReviews;
        private TextView tvFestivalDistance;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFestivalAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_festival_search_iv_avatar);
            tvFestivalName = (TextView) itemView.findViewById(R.id.item_recycler_festival_search_tv_name);
            tvFestivalAddress = (TextView) itemView.findViewById(R.id.item_recycler_festival_search_tv_address);
            srbFestivalRate = (SimpleRatingBar) itemView.findViewById(R.id.item_recycler_festival_search_srb_rate);
            tvFestivalReviews = (TextView) itemView.findViewById(R.id.item_recycler_festival_search_tv_number_of_feedback);
            tvFestivalDistance = (TextView) itemView.findViewById(R.id.item_recycler_festival_search_tv_distance);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_festival_search_rotate_loading);

            itemView.setOnClickListener(v -> {
                FestivalDetailActivity_.intent(context).festivalResponse(festivalResponses.get(getAdapterPosition())).start();
                ((FestivalSearchActivity) context).finish();
            });
        }
    }

    /*
    * filter
    **/
    public void setFilter(List<FestivalResponse> festivalFilter) {
        festivalResponses.clear();
        festivalResponses.addAll(festivalFilter);
        notifyDataSetChanged();
    }
}