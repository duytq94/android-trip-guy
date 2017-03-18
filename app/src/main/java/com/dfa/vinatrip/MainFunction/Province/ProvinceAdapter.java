package com.dfa.vinatrip.MainFunction.Province;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

// Adapter RecyclerView for Province in ProvinceFragment
public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {
    private List<Province> provinceList;
    private LayoutInflater layoutInflater;
    private Context context;

    public ProvinceAdapter(Context context, List<Province> provinceList) {
        this.provinceList = provinceList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_province, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProvinceAdapter.ProvinceViewHolder holder, int position) {
        Province province = provinceList.get(position);

        //bind data to viewholder
        holder.tvName.setText(province.getName());
        holder.tvTitle.setText(province.getTitle());
        // all photo will the same scale
        holder.ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(province.getAvatar())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivAvatar,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                            }
                        });
    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }

    public static class ProvinceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvTitle;
        private ImageView ivAvatar;

        public ProvinceViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_province_tv_name);
            tvTitle = (TextView) itemView.findViewById(R.id.item_province_tv_title);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_province_iv_avatar);
        }
    }
}
