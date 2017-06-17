package com.dfa.vinatrip.domains.main.share;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by duytq on 6/17/2017.
 */

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {
    private List<Share> shareList;
    private LayoutInflater layoutInflater;
    private Context context;

    public ShareAdapter(Context context, List<Share> shareList) {
        this.shareList = shareList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_share, parent, false);
        return new ShareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShareAdapter.ShareViewHolder holder, int position) {
        Share share = shareList.get(position);

        holder.tvDestination.setText(share.getDestination());
        holder.tvAddress.setText(share.getAddress());
        holder.tvName.setText(share.getNickname());

        Picasso.with(context).load(share.getPhoto1())
               .placeholder(R.drawable.bg_test1)
               .error(R.drawable.photo_not_available)
               .into(holder.ivBackground);

        Picasso.with(context).load(share.getAvatar())
               .placeholder(R.drawable.ic_loading)
               .error(R.drawable.photo_not_available)
               .into(holder.civAvatar);
    }

    @Override
    public int getItemCount() {
        return shareList.size();
    }

    public static class ShareViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDestination, tvAddress, tvName;
        private ImageView ivBackground;
        private CircleImageView civAvatar;


        public ShareViewHolder(View itemView) {
            super(itemView);
            tvDestination = (TextView) itemView.findViewById(R.id.item_share_tv_destination);
            tvAddress = (TextView) itemView.findViewById(R.id.item_share_tv_address);
            tvName = (TextView) itemView.findViewById(R.id.item_share_tv_name);
            ivBackground = (ImageView) itemView.findViewById(R.id.item_share_iv_background);
            civAvatar = (CircleImageView) itemView.findViewById(R.id.item_share_civ_avatar);
        }
    }
}