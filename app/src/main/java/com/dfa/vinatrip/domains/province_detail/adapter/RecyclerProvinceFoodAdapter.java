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
import com.dfa.vinatrip.domains.province_detail.view_all.food.FoodSearchActivity_;
import com.dfa.vinatrip.domains.province_detail.view_all.food.food_detail.FoodDetailActivity_;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by duonghd on 10/13/2017.
 * duonghd1307@gmail.com
 */

public class RecyclerProvinceFoodAdapter extends RecyclerView.Adapter<RecyclerProvinceFoodAdapter.ViewHolder> {
    private Context context;
    private Province province;
    private List<FoodResponse> foodResponses;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerProvinceFoodAdapter(Context context, Province province, List<FoodResponse> foodResponses) {
        this.context = context;
        this.province = province;
        this.foodResponses = foodResponses;
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
    public RecyclerProvinceFoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_province_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodResponse food = foodResponses.get(position);
        if (position != foodResponses.size() - 1) {
            holder.llMain.setVisibility(View.VISIBLE);
            holder.cvViewAll.setVisibility(View.GONE);
            holder.tvFoodName.setText(food.getName());
            holder.srbFoodRate.setRating(food.getStar());
            holder.tvFoodReviews.setText(String.format("%s Reviews", food.getReview()));

            imageLoader.displayImage(food.getAvatar(), holder.ivFoodAvatar, imageOptions, new ImageLoadingListener() {
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
        return foodResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llMain;
        private CardView cvViewAll;
        private ImageView ivFoodAvatar;
        private TextView tvFoodName;
        private SimpleRatingBar srbFoodRate;
        private TextView tvFoodReviews;
        private TextView tvFoodDistance;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            llMain = (LinearLayout) itemView.findViewById(R.id.item_recycler_province_food_ll_main);
            cvViewAll = (CardView) itemView.findViewById(R.id.item_recycler_province_food_cv_view_all);
            ivFoodAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_province_food_iv_image);
            tvFoodName = (TextView) itemView.findViewById(R.id.item_recycler_province_food_tv_name);
            srbFoodRate = (SimpleRatingBar) itemView.findViewById(R.id.item_recycler_province_food_srb_rating);
            tvFoodReviews = (TextView) itemView.findViewById(R.id.item_recycler_province_food_tv_reviews);
            tvFoodDistance = (TextView) itemView.findViewById(R.id.item_recycler_province_food_tv_distance);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_province_food_rotate_loading);

            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() == foodResponses.size() - 1) {
                    FoodSearchActivity_.intent(context)
                            .province(province).start();
                } else {
                    FoodDetailActivity_.intent(context)
                            .foodResponse(foodResponses.get(getAdapterPosition())).start();
                }
            });
        }
    }
}
