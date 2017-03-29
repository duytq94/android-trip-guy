package com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceDestination;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProvinceDestinationAdapter extends RecyclerView.Adapter<ProvinceDestinationAdapter.DestinationViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<ProvinceDestination> provinceDestinationList;

    public ProvinceDestinationAdapter(Context context, List<ProvinceDestination> provinceDestinationList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.provinceDestinationList = provinceDestinationList;
    }

    @Override
    public DestinationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_province_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DestinationViewHolder holder, int position) {
        ProvinceDestination provinceDestination = provinceDestinationList.get(position);

        //bind data to viewholder
        holder.tvName.setText(provinceDestination.getName());
        holder.tvTimeSpend.setText(String.format("%s%s", "   ", provinceDestination.getTimeSpend()));
        holder.tvTypeOfTourism.setText(String.format("%s%s", "   ", provinceDestination.getTypeOfTourism()));

        // all provinceDestination will the same scale
        holder.ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(provinceDestination.getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return provinceDestinationList.size();
    }

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvTimeSpend, tvTypeOfTourism;
        private ImageView ivAvatar;

        public DestinationViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_province_destination_tv_name);
            tvTimeSpend = (TextView) itemView.findViewById(R.id.item_province_destination_tv_time_spend);
            tvTypeOfTourism = (TextView) itemView.findViewById(R.id.item_province_destination_tv_type_of_tourism);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_province_destination_iv_avatar);
        }
    }
}

