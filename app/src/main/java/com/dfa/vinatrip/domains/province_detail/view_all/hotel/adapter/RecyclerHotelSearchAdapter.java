package com.dfa.vinatrip.domains.province_detail.view_all.hotel.adapter;

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
import com.dfa.vinatrip.domains.province_detail.view_all.hotel.HotelSearchActivity;
import com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail.HotelDetailActivity_;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
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

public class RecyclerHotelSearchAdapter extends RecyclerView.Adapter<RecyclerHotelSearchAdapter.ViewHolder> {
    private Context context;
    private List<HotelResponse> hotelResponses;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerHotelSearchAdapter(Context context) {
        this.context = context;
        this.hotelResponses = new ArrayList<>();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_hotel_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HotelResponse hotel = hotelResponses.get(position);

        holder.tvHotelName.setText(hotel.getName());
        holder.srbHotelRate.setRating(hotel.getStar());
        holder.tvHotelReview.setText(String.format("%s đánh giá", hotel.getReview()));
        holder.tvHotelAddress.setText(hotel.getAddress());

        imageLoader.displayImage(hotel.getAvatar(), holder.ivHotelAvatar, imageOptions, new ImageLoadingListener() {
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
        return hotelResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivHotelAvatar;
        private TextView tvHotelName;
        private TextView tvHotelAddress;
        private SimpleRatingBar srbHotelRate;
        private TextView tvHotelReview;
        private TextView tvHotelDistance;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            ivHotelAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_hotel_search_iv_avatar);
            tvHotelName = (TextView) itemView.findViewById(R.id.item_recycler_hotel_search_tv_name);
            tvHotelAddress = (TextView) itemView.findViewById(R.id.item_recycler_hotel_search_tv_address);
            srbHotelRate = (SimpleRatingBar) itemView.findViewById(R.id.item_recycler_hotel_search_srb_rate);
            tvHotelReview = (TextView) itemView.findViewById(R.id.item_recycler_hotel_search_tv_number_of_feedback);
            tvHotelDistance = (TextView) itemView.findViewById(R.id.item_recycler_hotel_search_tv_distance);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_hotel_search_rotate_loading);

            itemView.setOnClickListener(v -> {
                HotelDetailActivity_.intent(context).hotelResponse(hotelResponses.get(getAdapterPosition())).start();
                ((HotelSearchActivity) context).finish();
            });
        }
    }

    /*
    * filter
    **/
    public void setFilter(List<HotelResponse> hotelFilter) {
        hotelResponses.clear();
        hotelResponses.addAll(hotelFilter);
        notifyDataSetChanged();
    }
}