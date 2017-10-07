package com.dfa.vinatrip.domains.main.province.province_detail.fragment.place.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.place.PlaceResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by duonghd on 10/8/2017.
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaceResponse place = placeResponses.get(position);

        holder.tvPlaceName.setText(place.getName());

        Picasso.with(context).load(place.getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivPlaceAvatar);
    }

    @Override
    public int getItemCount() {
        return placeResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPlaceAvatar;
        private TextView tvPlaceName;

        public ViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(v -> {
//                HotelDetailActivity_.intent(context).hotel(placeResponses.get(getAdapterPosition())).start();
//            });
            ivPlaceAvatar = (ImageView) itemView.findViewById(R.id.item_province_place_iv_avatar);
            tvPlaceName = (TextView) itemView.findViewById(R.id.item_province_place_tv_name);
        }
    }
}