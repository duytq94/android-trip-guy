package com.dfa.vinatrip.domains.main.fragment.province.province_detail.adapter;

import android.content.Context;
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
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by duonghd on 10/13/2017.
 */

public class RecyclerFoodAdapter extends RecyclerView.Adapter<RecyclerFoodAdapter.ViewHolder> {
    private Context context;
    private List<FoodResponse> foodResponses;
    
    public RecyclerFoodAdapter(Context context, List<FoodResponse> foodResponses) {
        this.context = context;
        this.foodResponses = foodResponses;
    }
    
    @Override
    public RecyclerFoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_province_detail_it_food, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodResponse food = foodResponses.get(position);
        if (position != foodResponses.size() - 1) {
            holder.llMain.setVisibility(View.VISIBLE);
            holder.cvViewAll.setVisibility(View.GONE);
            holder.tvFoodName.setText(food.getName());
            Picasso.with(context).load(food.getAvatar())
                    .error(R.drawable.photo_not_available)
                    .into(holder.ivFoodAvatar);
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
        
        public ViewHolder(View itemView) {
            super(itemView);
            llMain = (LinearLayout) itemView.findViewById(R.id.item_province_detail_it_food_ll_main);
            cvViewAll = (CardView) itemView.findViewById(R.id.item_province_detail_it_food_cv_view_all);
            ivFoodAvatar = (ImageView) itemView.findViewById(R.id.item_province_detail_it_food_iv_image);
            tvFoodName = (TextView) itemView.findViewById(R.id.item_province_detail_it_food_tv_name);
            srbFoodRate = (SimpleRatingBar) itemView.findViewById(R.id.item_province_detail_it_food_srb_rating);
            tvFoodReviews = (TextView) itemView.findViewById(R.id.item_province_detail_it_food_tv_reviews);
            tvFoodDistance = (TextView) itemView.findViewById(R.id.item_province_detail_it_food_tv_distance);
        }
    }
}