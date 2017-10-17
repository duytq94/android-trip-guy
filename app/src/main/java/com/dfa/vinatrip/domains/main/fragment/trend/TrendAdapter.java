package com.dfa.vinatrip.domains.main.fragment.trend;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.trend.detail_trend.DetailTrendActivity_;
import com.dfa.vinatrip.models.response.place.Trend;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duytq on 10/17/2017.
 */

public class TrendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Trend> trendList;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;
    private Context context;

    public TrendAdapter(Context context) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trend, parent, false);
        return new TrendHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TrendHolder trendHolder = (TrendHolder) holder;
        Trend trend = trendList.get(position);

        trendHolder.tvTitle.setText(trend.getTitle());
        trendHolder.tvIntro.setText(Html.fromHtml(trend.getIntro()));
        imageLoader.displayImage(trend.getBackground(), trendHolder.ivBackground, imageOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                trendHolder.rotateLoading.setVisibility(View.VISIBLE);
                trendHolder.rotateLoading.start();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                trendHolder.rotateLoading.setVisibility(View.GONE);
                trendHolder.rotateLoading.stop();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                trendHolder.rotateLoading.setVisibility(View.GONE);
                trendHolder.rotateLoading.stop();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                trendHolder.rotateLoading.setVisibility(View.GONE);
                trendHolder.rotateLoading.stop();
            }
        });
        trendHolder.llRoot.setOnClickListener(v -> {
            DetailTrendActivity_.intent(context).trend(trend).start();
        });
    }

    @Override
    public int getItemCount() {
        if (trendList != null) {
            return trendList.size();
        } else {
            return 0;
        }
    }

    public void setDealList(List<Trend> trendList) {
        this.trendList = new ArrayList<>();
        this.trendList.addAll(trendList);
    }

    public void appendList(List<Trend> trendList) {
        this.trendList.addAll(trendList);
    }

    public class TrendHolder extends RecyclerView.ViewHolder {

        ImageView ivBackground;
        TextView tvTitle, tvIntro;
        RotateLoading rotateLoading;
        LinearLayout llRoot;

        public TrendHolder(View itemView) {
            super(itemView);
            ivBackground = (ImageView) itemView.findViewById(R.id.item_trend_iv_background);
            tvTitle = (TextView) itemView.findViewById(R.id.item_trend_tv_title);
            tvIntro = (TextView) itemView.findViewById(R.id.item_trend_tv_intro);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_trend_rotate_loading);
            llRoot = (LinearLayout) itemView.findViewById(R.id.item_trend_ll_root);
        }
    }
}
