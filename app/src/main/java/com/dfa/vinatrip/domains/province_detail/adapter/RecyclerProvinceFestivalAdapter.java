package com.dfa.vinatrip.domains.province_detail.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.province_detail.view_all.festival.FestivalSearchActivity_;
import com.dfa.vinatrip.domains.province_detail.view_all.festival.festival_detail.FestivalDetailActivity_;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.festival.FestivalResponse;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by duonghd on 12/8/2017.
 * duonghd1307@gmail.com
 */

public class RecyclerProvinceFestivalAdapter extends RecyclerView.Adapter<RecyclerProvinceFestivalAdapter.ViewHolder> {
    private Context context;
    private Province province;
    private List<FestivalResponse> festivalResponses;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public RecyclerProvinceFestivalAdapter(Context context, Province province, List<FestivalResponse> festivalResponses) {
        this.context = context;
        this.province = province;
        this.festivalResponses = festivalResponses;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_province_festival, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FestivalResponse festival = festivalResponses.get(position);
        if (position != festivalResponses.size() - 1) {
            holder.cvViewMain.setVisibility(View.VISIBLE);
            holder.cvViewAll.setVisibility(View.GONE);
            holder.tvEventTime.setText(festival.getTime());
            holder.tvEventName.setText(festival.getName());

            imageLoader.displayImage(festival.getAvatar(), holder.ivEventAvatar, imageOptions, new ImageLoadingListener() {
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
        } else {
            holder.cvViewMain.setVisibility(View.GONE);
            holder.cvViewAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return festivalResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cvViewMain;
        private CardView cvViewAll;
        private ImageView ivEventAvatar;
        private TextView tvEventName;
        private TextView tvEventTime;
        private RotateLoading rotateLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            cvViewMain = (CardView) itemView.findViewById(R.id.item_recycler_province_festival_cv_view_main);
            cvViewAll = (CardView) itemView.findViewById(R.id.item_recycler_province_festival_cv_view_all);
            ivEventAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_province_festival_iv_image);
            tvEventName = (TextView) itemView.findViewById(R.id.item_recycler_province_festival_tv_name);
            tvEventTime = (TextView) itemView.findViewById(R.id.item_recycler_province_festival_tv_time);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_recycler_province_festival_rotate_loading);

            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() != festivalResponses.size() - 1) {
                    FestivalDetailActivity_.intent(context).festivalResponse(festivalResponses.get(getAdapterPosition())).start();
                } else {
                    FestivalSearchActivity_.intent(context).province(province).start();
                }
            });
        }
    }
}
