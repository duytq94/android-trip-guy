package com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceHotel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProvinceHotelAdapter extends RecyclerView.Adapter<ProvinceHotelAdapter.HotelViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<ProvinceHotel> provinceHotelList;

    public ProvinceHotelAdapter(Context context, List<ProvinceHotel> provinceHotelList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.provinceHotelList = provinceHotelList;
    }

    @Override
    public HotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_province_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotelViewHolder holder, int position) {
        ProvinceHotel provinceHotel = provinceHotelList.get(position);

        //bind data to viewholder
        holder.tvName.setText(provinceHotel.getName());

        String price = "$" + provinceHotel.getPrice();
        holder.tvPrice.setText(price);

        // show symbol star
        String rate = "Hotel rating is not available";
        switch (Integer.parseInt(provinceHotel.getRate())) {
            case 1:
                rate = "&#9733;";
                break;
            case 2:
                rate = "&#9733; &#9733;";
                break;
            case 3:
                rate = "&#9733; &#9733; &#9733;";
                break;
            case 4:
                rate = "&#9733; &#9733; &#9733; &#9733;";
                break;
            case 5:
                rate = "&#9733; &#9733; &#9733; &#9733; &#9733;";
                break;
        }
        holder.tvRate.setText(Html.fromHtml(rate));

        // all provinceHotel will the same scale
        holder.ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(provinceHotel.getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return provinceHotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvRate, tvPrice;
        private ImageView ivAvatar;

        public HotelViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_province_hotel_tv_name);
            tvRate = (TextView) itemView.findViewById(R.id.item_province_hotel_tv_rate);
            tvPrice = (TextView) itemView.findViewById(R.id.item_province_hotel_tv_price);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_province_hotel_iv_avatar);
        }
    }
}