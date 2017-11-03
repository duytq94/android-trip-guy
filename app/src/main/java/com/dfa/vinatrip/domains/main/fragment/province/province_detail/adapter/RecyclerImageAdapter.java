package com.dfa.vinatrip.domains.main.fragment.province.province_detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.province_image.ProvinceImageResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by duonghd on 11/3/2017.
 */

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ViewHolder> {
    private Context context;
    private List<ProvinceImageResponse> imageResponses;
    
    public RecyclerImageAdapter(Context context, List<ProvinceImageResponse> imageResponses) {
        this.context = context;
        this.imageResponses = imageResponses;
    }
    
    @Override
    public RecyclerImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_province_detail_it_photo, parent, false);
        return new RecyclerImageAdapter.ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(RecyclerImageAdapter.ViewHolder holder, int position) {
        ProvinceImageResponse image = imageResponses.get(position);
        Picasso.with(context).load(image.getUrl())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivImageAvatar);
    }
    
    @Override
    public int getItemCount() {
        return imageResponses.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImageAvatar;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ivImageAvatar = (ImageView) itemView.findViewById(R.id.item_province_detail_it_photo_iv_photos);
        }
    }
}
