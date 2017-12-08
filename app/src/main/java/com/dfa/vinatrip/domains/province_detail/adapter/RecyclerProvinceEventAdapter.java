package com.dfa.vinatrip.domains.province_detail.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.event.EventResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by duonghd on 12/8/2017.
 */

public class RecyclerProvinceEventAdapter extends RecyclerView.Adapter<RecyclerProvinceEventAdapter.ViewHolder> {
    private Context context;
    private Province province;
    private List<EventResponse> eventResponses;
    
    public RecyclerProvinceEventAdapter(Context context, Province province, List<EventResponse> eventResponses) {
        this.context = context;
        this.province = province;
        this.eventResponses = eventResponses;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_province_event, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventResponse event = eventResponses.get(position);
        if (position != eventResponses.size() - 1) {
            holder.cvViewMain.setVisibility(View.VISIBLE);
            holder.cvViewAll.setVisibility(View.GONE);
            holder.tvEventTime.setText(event.getTime());
            holder.tvEventName.setText(event.getName());
            Picasso.with(context).load(event.getAvatar())
                    .error(R.drawable.photo_not_available)
                    .into(holder.ivEventAvatar);
        } else {
            holder.cvViewMain.setVisibility(View.GONE);
            holder.cvViewAll.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public int getItemCount() {
        return eventResponses.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cvViewMain;
        private CardView cvViewAll;
        private ImageView ivEventAvatar;
        private TextView tvEventName;
        private TextView tvEventTime;
        
        public ViewHolder(View itemView) {
            super(itemView);
            cvViewMain = (CardView) itemView.findViewById(R.id.item_recycler_province_event_cv_view_main);
            cvViewAll = (CardView) itemView.findViewById(R.id.item_recycler_province_event_cv_view_all);
            ivEventAvatar = (ImageView) itemView.findViewById(R.id.item_recycler_province_event_iv_image);
            tvEventName = (TextView) itemView.findViewById(R.id.item_recycler_province_event_tv_name);
            tvEventTime = (TextView) itemView.findViewById(R.id.item_recycler_province_event_tv_time);
        }
    }
}
