package com.dfa.vinatrip.domains.main.fragment.deal;

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
import com.dfa.vinatrip.domains.web.WebActivity_;
import com.dfa.vinatrip.models.response.Deal;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duytq on 10/16/2017.
 */

public class DealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Deal> dealList;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;
    private Context context;

    public DealAdapter(Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal, parent, false);
        return new DealHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DealHolder dealHolder = (DealHolder) holder;
        Deal deal = dealList.get(position);

        dealHolder.tvTitle.setText(deal.getTitle());
        dealHolder.tvTitle.setSelected(true);
        dealHolder.tvRoute.setText(Html.fromHtml(deal.getRoute()));
        dealHolder.tvRoute.setSelected(true);
        dealHolder.tvContent.setText(Html.fromHtml(deal.getContent()));
        dealHolder.tvPrice.setText(String.format("%sđ", AppUtil.convertPrice(deal.getPrice())));
        dealHolder.tvDayStart.setText(deal.getDayStart());
        dealHolder.llGoDetail.setOnClickListener(v -> {
            WebActivity_.intent(context).url(deal.getLinkDetail()).start();
        });
        imageLoader.displayImage(deal.getImg(), dealHolder.ivPhoto, imageOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                dealHolder.rotateLoading.setVisibility(View.VISIBLE);
                dealHolder.rotateLoading.start();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                dealHolder.rotateLoading.setVisibility(View.GONE);
                dealHolder.rotateLoading.stop();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                dealHolder.rotateLoading.setVisibility(View.GONE);
                dealHolder.rotateLoading.stop();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                dealHolder.rotateLoading.setVisibility(View.GONE);
                dealHolder.rotateLoading.stop();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dealList != null) {
            return dealList.size();
        } else {
            return 0;
        }
    }

    public void setDealList(List<Deal> dealList) {
        this.dealList = new ArrayList<>();
        this.dealList.addAll(dealList);
    }

    public void appendList(List<Deal> dealList) {
        this.dealList.addAll(dealList);
    }

    public class DealHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;
        private TextView tvTitle, tvRoute, tvContent, tvPrice, tvDayStart;
        private LinearLayout llGoDetail;
        private RotateLoading rotateLoading;

        public DealHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.item_deal_iv_photo);
            tvTitle = (TextView) itemView.findViewById(R.id.item_deal_tv_title);
            tvRoute = (TextView) itemView.findViewById(R.id.item_deal_tv_route);
            tvContent = (TextView) itemView.findViewById(R.id.item_deal_tv_content);
            tvPrice = (TextView) itemView.findViewById(R.id.item_deal_tv_price);
            tvDayStart = (TextView) itemView.findViewById(R.id.item_deal_tv_day_start);
            llGoDetail = (LinearLayout) itemView.findViewById(R.id.item_deal_ll_go_detail);
            rotateLoading = (RotateLoading) itemView.findViewById(R.id.item_deal_rotate_loading);
        }
    }
}
