package com.dfa.vinatrip.domains.main.province.province_detail.fragment.hotel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.province_detail.fragment.hotel.hotel_detail.HotelDetailActivity_;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by duonghd on 10/7/2017.
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province_hotel, parent, false);
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

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                HotelDetailActivity_.intent(context).hotel(hotelResponses.get(getAdapterPosition())).start();
            });
            ivHotelAvatar = (ImageView) itemView.findViewById(R.id.item_province_hotel_iv_avatar);
            tvHotelName = (TextView) itemView.findViewById(R.id.item_province_hotel_tv_name);
        }
    }
}
