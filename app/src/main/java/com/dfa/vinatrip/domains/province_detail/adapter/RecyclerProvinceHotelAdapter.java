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
import com.dfa.vinatrip.domains.province_detail.view_all.hotel.HotelSearchActivity_;
import com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail.HotelDetailActivity_;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by duonghd on 10/13/2017.
 */

public class RecyclerProvinceHotelAdapter extends RecyclerView.Adapter<RecyclerProvinceHotelAdapter.ViewHolder> {
    private Context context;
    private Province province;
    private List<HotelResponse> hotelResponses;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerProvinceHotelAdapter(Context context, Province province, List<HotelResponse> hotelResponses) {
        this.context = context;
        this.province = province;
        this.hotelResponses = hotelResponses;
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
                R.layout.item_recycler_province_hotel, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HotelResponse hotel = hotelResponses.get(position);
        if (position != hotelResponses.size() - 1) {
            holder.llMain.setVisibility(View.VISIBLE);
            holder.cvViewAll.setVisibility(View.GONE);
            holder.tvHotelName.setText(hotel.getName());

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
        } else {
            holder.llMain.setVisibility(View.GONE);
            holder.cvViewAll.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public int getItemCount() {
        return hotelResponses.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llMain;
        private CardView cvViewAll;
        private ImageView ivHotelAvatar;
        private TextView tvHotelName;
        private SimpleRatingBar srbHotelRate;
        private TextView tvHotelReviews;
        private TextView tvHotelDistance;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            llMain = (LinearLayout) itemView.findViewById(R.id.item_recycler_province_hotel_ll_main);
            cvViewAll = (CardView) itemView.findViewById(R.id.item_recycler_province_hotel_cv_view_all);
            ivHotelAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_province_hotel_iv_image);
            tvHotelName = (TextView) itemView.findViewById(R.id.item_recycler_province_hotel_tv_name);
            srbHotelRate = (SimpleRatingBar) itemView.findViewById(R.id.item_recycler_province_hotel_srb_rating);
            tvHotelReviews = (TextView) itemView.findViewById(R.id.item_recycler_province_hotel_tv_reviews);
            tvHotelDistance = (TextView) itemView.findViewById(R.id.item_recycler_province_hotel_tv_distance);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_province_hotel_rotate_loading);

            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() == hotelResponses.size() - 1) {
                    HotelSearchActivity_.intent(context)
                            .province(province).start();
                } else {
                    HotelDetailActivity_.intent(context)
                            .hotelResponse(hotelResponses.get(getAdapterPosition())).start();
                }
            });
        }
    }
}
