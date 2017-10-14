package com.dfa.vinatrip.domains.main.fragment.province.province_detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by duonghd on 10/13/2017.
 */

public class RecyclerHotelAdapter extends RecyclerView.Adapter<RecyclerHotelAdapter.ViewHolder> {
    private Context context;
    private List<HotelResponse> hotelResponses;
    
    public RecyclerHotelAdapter(Context context, List<HotelResponse> hotelResponses) {
        this.context = context;
        this.hotelResponses = hotelResponses;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_province_detail_it_hotel, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HotelResponse hotel = hotelResponses.get(position);
        
        holder.tvHotelName.setText(hotel.getName());
        Picasso.with(context).load(hotel.getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivHotelAvatar);
    }
    
    @Override
    public int getItemCount() {
        return hotelResponses.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivHotelAvatar;
        private TextView tvHotelName;
        private SimpleRatingBar srbHotelRate;
        private TextView tvHotelReviews;
        private TextView tvHotelDistance;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ivHotelAvatar = (ImageView) itemView.findViewById(R.id.item_province_detail_it_hotel_iv_image);
            tvHotelName = (TextView) itemView.findViewById(R.id.item_province_detail_it_hotel_tv_name);
            srbHotelRate = (SimpleRatingBar) itemView.findViewById(R.id.item_province_detail_it_hotel_srb_rating);
            tvHotelReviews = (TextView) itemView.findViewById(R.id.item_province_detail_it_hotel_tv_reviews);
            tvHotelDistance = (TextView) itemView.findViewById(R.id.item_province_detail_it_hotel_tv_distance);
        }
    }
}
