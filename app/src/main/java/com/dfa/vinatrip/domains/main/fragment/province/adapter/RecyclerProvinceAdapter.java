package com.dfa.vinatrip.domains.main.fragment.province.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.province_detail.ProvinceDetailActivity_;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class RecyclerProvinceAdapter extends RecyclerView.Adapter<RecyclerProvinceAdapter.ViewHolder> {
    private List<Province> provinceList;
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerProvinceAdapter(Context context, List<Province> provinceList) {
        this.provinceList = provinceList;
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_green)
                .showImageForEmptyUri(R.drawable.photo_not_available)
                .showImageOnFail(R.drawable.photo_not_available)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
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

        imageLoader.displayImage(province.getAvatar(), holder.ivAvatar, imageOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.rotateLoading.setVisibility(View.VISIBLE);
                holder.rotateLoading.start();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.rotateLoading.setVisibility(View.GONE);
                holder.rotateLoading.stop();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.rotateLoading.setVisibility(View.GONE);
                holder.rotateLoading.stop();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.rotateLoading.setVisibility(View.GONE);
                holder.rotateLoading.stop();
            }
        });
    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvTitle;
        private ImageView ivAvatar;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                ProvinceDetailActivity_.intent(context).province(provinceList.get(getAdapterPosition())).start();
            });
            tvName = (TextView) itemView.findViewById(R.id.item_province_tv_name);
            tvTitle = (TextView) itemView.findViewById(R.id.item_province_tv_title);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_province_iv_avatar);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_province_rotate_loading);
        }
    }
}