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
import com.dfa.vinatrip.models.response.place.PlaceResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by duonghd on 11/3/2017.
 */

public class RecyclerPlaceAdapter extends RecyclerView.Adapter<RecyclerPlaceAdapter.ViewHolder> {
    private Context context;
    private List<PlaceResponse> placeResponses;
    
    public RecyclerPlaceAdapter(Context context, List<PlaceResponse> placeResponses) {
        this.context = context;
        this.placeResponses = placeResponses;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_province_detail_it_place, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaceResponse place = placeResponses.get(position);
        if (position != placeResponses.size() - 1) {
            holder.llMain.setVisibility(View.VISIBLE);
            holder.cvViewAll.setVisibility(View.GONE);
            
            holder.tvPlaceName.setText(place.getName());
            Picasso.with(context).load(place.getAvatar())
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.photo_not_available)
                    .into(holder.ivPlaceAvatar);
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
        
        public ViewHolder(View itemView) {
            super(itemView);
            llMain = (LinearLayout) itemView.findViewById(R.id.item_province_detail_it_place_ll_main);
            cvViewAll = (CardView) itemView.findViewById(R.id.item_province_detail_it_place_cv_view_all);
            ivPlaceAvatar = (ImageView) itemView.findViewById(R.id.item_province_detail_it_place_iv_image);
            tvPlaceName = (TextView) itemView.findViewById(R.id.item_province_detail_it_place_tv_name);
            srbPlaceRate = (SimpleRatingBar) itemView.findViewById(R.id.item_province_detail_it_place_srb_rating);
            tvPlaceReviews = (TextView) itemView.findViewById(R.id.item_province_detail_it_place_tv_reviews);
            tvPlaceDistance = (TextView) itemView.findViewById(R.id.item_province_detail_it_place_tv_distance);
        }
    }
}
