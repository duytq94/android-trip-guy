package com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.hotel.HotelImage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by duonghd on 12/5/2017.
 */

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ViewHolder> {
    private Context context;
    private List<HotelImage> hotelImages;
    
    public RecyclerImageAdapter(Context context, List<HotelImage> hotelImages) {
        this.context = context;
        this.hotelImages = hotelImages;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_province_detail_it_photo, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        
        Picasso.with(context).load(hotelImages.get(position).getUrl())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivImageView);
    }
    
    @Override
    public int getItemCount() {
        return hotelImages.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImageView;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ivImageView = (ImageView) itemView.findViewById(R.id.item_province_detail_it_photo_iv_photos);
        }
    }
}
