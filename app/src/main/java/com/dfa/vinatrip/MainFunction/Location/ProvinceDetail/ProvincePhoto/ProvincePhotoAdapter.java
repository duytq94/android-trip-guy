package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvincePhoto;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProvincePhotoAdapter extends
        RecyclerView.Adapter<ProvincePhotoAdapter.PhotoViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private SwipeRefreshLayout srlReload;
    private List<ProvincePhoto> provincePhotoList;

    public ProvincePhotoAdapter(Context context,
                                List<ProvincePhoto> provincePhotoList,
                                SwipeRefreshLayout srlReload) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.srlReload = srlReload;
        this.provincePhotoList = provincePhotoList;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_province_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        //bind data to viewholder
        // all provincePhoto will the same scale
        holder.ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(provincePhotoList.get(position).getUrl())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivAvatar,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                // turn icon waiting off when finish
                                srlReload.setRefreshing(false);
                            }

                            @Override
                            public void onError() {
                            }
                        });
    }

    @Override
    public int getItemCount() {
        return provincePhotoList.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAvatar;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_province_photo_iv_avatar);
        }
    }
}
