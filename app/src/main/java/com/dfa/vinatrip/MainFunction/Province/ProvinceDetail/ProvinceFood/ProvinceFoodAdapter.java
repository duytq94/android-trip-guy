package com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceFood;

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

public class ProvinceFoodAdapter extends RecyclerView.Adapter<ProvinceFoodAdapter.FoodViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<ProvinceFood> provinceFoodList;

    public ProvinceFoodAdapter(Context context, List<ProvinceFood> provinceFoodList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.provinceFoodList = provinceFoodList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_province_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        ProvinceFood provinceFood = provinceFoodList.get(position);

        //bind data to viewholder
        holder.tvName.setText(provinceFood.getName());
        holder.tvTimeOpen.setText(String.format("%s%s", "   ", provinceFood.getTimeOpen()));
        holder.tvPrice.setText(String.format("%s%s", "   ", provinceFood.getPrice()));

        // all provinceFood will the same scale
        holder.ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(provinceFood.getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return provinceFoodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvTimeOpen, tvPrice;
        private ImageView ivAvatar;

        public FoodViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_province_food_tv_name);
            tvTimeOpen = (TextView) itemView.findViewById(R.id.item_province_food_tv_time_open);
            tvPrice = (TextView) itemView.findViewById(R.id.item_province_food_tv_price);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_province_food_iv_avatar);
        }
    }
}



