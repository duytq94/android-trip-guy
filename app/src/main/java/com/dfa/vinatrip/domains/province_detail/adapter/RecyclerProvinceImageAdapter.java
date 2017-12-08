package com.dfa.vinatrip.domains.province_detail.adapter;

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

public class RecyclerProvinceImageAdapter extends RecyclerView.Adapter<RecyclerProvinceImageAdapter.ViewHolder> {
    private Context context;
    private List<ProvinceImageResponse> imageResponses;
    
    public RecyclerProvinceImageAdapter(Context context, List<ProvinceImageResponse> imageResponses) {
        this.context = context;
        this.imageResponses = imageResponses;
    }
    
    @Override
    public RecyclerProvinceImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_province_image, parent, false);
        return new RecyclerProvinceImageAdapter.ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(RecyclerProvinceImageAdapter.ViewHolder holder, int position) {
        ProvinceImageResponse image = imageResponses.get(position);
        Picasso.with(context).load(image.getUrl())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(holder.ivImageView);
    }
    
    @Override
    public int getItemCount() {
        return imageResponses.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImageView;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ivImageView = (ImageView) itemView.findViewById(R.id.item_recycler_province_image_iv_photos);
        }
    }
}
