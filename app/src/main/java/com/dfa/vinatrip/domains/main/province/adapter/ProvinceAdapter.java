package com.dfa.vinatrip.domains.main.province.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.province_detail.ProvinceDetailActivity_;
import com.dfa.vinatrip.models.response.Province;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {
    private List<Province> provinceList;
    private Context context;
    
    public ProvinceAdapter(Context context, List<Province> provinceList) {
        this.provinceList = provinceList;
        this.context = context;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Province province = provinceList.get(position);
        
        holder.tvName.setText(province.getName());
        holder.tvTitle.setText(province.getShortDescription());
        
        Picasso.with(context).load(province.getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivAvatar);
    }
    
    @Override
    public int getItemCount() {
        return provinceList.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvTitle;
        private ImageView ivAvatar;
        
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                ProvinceDetailActivity_.intent(context).province(provinceList.get(getAdapterPosition())).start();
            });
            tvName = (TextView) itemView.findViewById(R.id.item_province_tv_name);
            tvTitle = (TextView) itemView.findViewById(R.id.item_province_tv_title);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_province_iv_avatar);
        }
    }
}