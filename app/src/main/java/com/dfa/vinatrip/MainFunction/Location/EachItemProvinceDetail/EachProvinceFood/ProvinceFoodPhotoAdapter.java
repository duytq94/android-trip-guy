package com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.EachProvinceFood;

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

public class ProvinceFoodPhotoAdapter extends
        RecyclerView.Adapter<ProvinceFoodPhotoAdapter.PhotoViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private SwipeRefreshLayout srlReload;
    private List<String> listUrlPhotos;

    public ProvinceFoodPhotoAdapter(Context context,
                                    List<String> listUrlPhotos,
                                    SwipeRefreshLayout srlReload) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.srlReload = srlReload;
        this.listUrlPhotos = listUrlPhotos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_province_detail_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        // Bind data to viewholder
        // All provincePhoto will the same scale
        holder.ivPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(listUrlPhotos.get(position))
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivPhoto,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                // Turn icon waiting off when finish
                                srlReload.setRefreshing(false);
                            }

                            @Override
                            public void onError() {
                            }
                        });
    }

    @Override
    public int getItemCount() {
        return listUrlPhotos.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.item_province_detail_photo_iv_photos);
        }
    }
}