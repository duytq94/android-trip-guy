package com.dfa.vinatrip.domains.main.fragment.province.province_detail.view_all.hotel.adapter;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.domains.main.fragment.province.province_detail.view_all.hotel.hotel_detail.HotelDetailActivity_;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duonghd on 10/7/2017.
 */

public class RecyclerHotelListAdapter extends RecyclerView.Adapter<RecyclerHotelListAdapter.ViewHolder> {
    private Context context;
    private List<HotelResponse> hotelResponses;
    
    public RecyclerHotelListAdapter(Context context) {
        this.context = context;
        this.hotelResponses = new ArrayList<>();
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_hotel, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HotelResponse hotel = hotelResponses.get(position);
        
        holder.tvHotelName.setText(hotel.getName());
        holder.tvHotelAddress.setText(hotel.getLocation());
        
        Picasso.with(context).load(hotel.getAvatar())
                .error(R.drawable.photo_not_available)
                .placeholder(R.drawable.photo_not_available)
                .into(holder.ivHotelAvatar);
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
        
        public ViewHolder(View itemView) {
            super(itemView);
            ivHotelAvatar = (ImageView) itemView.findViewById(R.id.item_list_hotel_iv_avatar);
            tvHotelName = (TextView) itemView.findViewById(R.id.item_list_hotel_tv_name);
            tvHotelAddress = (TextView) itemView.findViewById(R.id.item_list_hotel_tv_address);
            srbHotelRate = (SimpleRatingBar) itemView.findViewById(R.id.item_list_hotel_srb_rate);
            tvHotelReview = (TextView) itemView.findViewById(R.id.item_list_hotel_tv_review);
            tvHotelDistance = (TextView) itemView.findViewById(R.id.item_list_hotel_tv_distance);
            
            itemView.setOnClickListener(v -> {
                HotelDetailActivity_.intent(context).hotel(hotelResponses.get(getAdapterPosition())).start();
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
