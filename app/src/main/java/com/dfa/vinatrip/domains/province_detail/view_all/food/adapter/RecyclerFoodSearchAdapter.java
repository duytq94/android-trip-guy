package com.dfa.vinatrip.domains.province_detail.view_all.food.adapter;

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
import com.dfa.vinatrip.domains.province_detail.view_all.food.FoodSearchActivity;
import com.dfa.vinatrip.domains.province_detail.view_all.food.food_detail.FoodDetailActivity_;
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duonghd on 12/28/2017.
 * duonghd1307@gmail.com
 */

public class RecyclerFoodSearchAdapter extends RecyclerView.Adapter<RecyclerFoodSearchAdapter.ViewHolder> {
    private Context context;
    private List<FoodResponse> foodResponses;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerFoodSearchAdapter(Context context) {
        this.context = context;
        this.foodResponses = new ArrayList<>();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_food_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodResponse food = foodResponses.get(position);

        holder.tvFoodName.setText(food.getName());
        holder.tvFoodAddress.setText(food.getAddress());

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
    }

    @Override
    public int getItemCount() {
        return foodResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFoodAvatar;
        private TextView tvFoodName;
        private TextView tvFoodAddress;
        private SimpleRatingBar srbFoodRate;
        private TextView tvFoodReview;
        private TextView tvFoodDistance;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFoodAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_food_search_iv_avatar);
            tvFoodName = (TextView) itemView.findViewById(R.id.item_recycler_food_search_tv_name);
            tvFoodAddress = (TextView) itemView.findViewById(R.id.item_recycler_food_search_tv_address);
            srbFoodRate = (SimpleRatingBar) itemView.findViewById(R.id.item_recycler_food_search_srb_rate);
            tvFoodReview = (TextView) itemView.findViewById(R.id.item_recycler_food_search_tv_number_of_feedback);
            tvFoodDistance = (TextView) itemView.findViewById(R.id.item_recycler_food_search_tv_distance);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_food_search_rotate_loading);

            itemView.setOnClickListener(v -> {
                FoodDetailActivity_.intent(context).foodResponse(foodResponses.get(getAdapterPosition())).start();
                ((FoodSearchActivity) context).finish();
            });
        }
    }

    /*
* filter
**/
    public void setFilter(List<FoodResponse> foodFilter) {
        foodResponses.clear();
        foodResponses.addAll(foodFilter);
        notifyDataSetChanged();
    }
}