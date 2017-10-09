package com.dfa.vinatrip.domains.main.fragment.province.each_item_detail_province.rating;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.me.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<UserRating> listUserRatings;
    private UserProfile currentUser;

    public RatingAdapter(Context context, List<UserRating> listUserRatings, UserProfile currentUser) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listUserRatings = listUserRatings;
        this.currentUser = currentUser;
    }

    @Override
    public RatingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RatingViewHolder holder, int position) {
        UserRating userRating = listUserRatings.get(position);

        if (currentUser != null && currentUser.getUid().equals(userRating.getUid())) {
            holder.tvNickname.setText("Tôi");
        } else {
            holder.tvNickname.setText(userRating.getNickname());
        }

        holder.tvEmail.setText(userRating.getEmail());
        holder.tvContent.setText(userRating.getContent());
        holder.tvDate.setText(userRating.getDate());

        // show symbol star
        String rate = null;
        switch (Integer.parseInt(userRating.getNumStars())) {
            case 1:
                rate = "&#9733;";
                break;
            case 2:
                rate = "&#9733; &#9733;";
                break;
            case 3:
                rate = "&#9733; &#9733; &#9733;";
                break;
            case 4:
                rate = "&#9733; &#9733; &#9733; &#9733;";
                break;
            case 5:
                rate = "&#9733; &#9733; &#9733; &#9733; &#9733;";
                break;
        }
        holder.tvNumStars.setText(Html.fromHtml(rate));

        Picasso.with(context).load(listUserRatings.get(position).getAvatar())
               .placeholder(R.drawable.ic_loading)
               .error(R.drawable.photo_not_available)
               .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return listUserRatings.size();
    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAvatar;
        private TextView tvNickname, tvEmail, tvContent, tvNumStars, tvDate;

        public RatingViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_rating_iv_avatar);
            tvNickname = (TextView) itemView.findViewById(R.id.item_rating_tv_nickname);
            tvEmail = (TextView) itemView.findViewById(R.id.item_rating_tv_email);
            tvContent = (TextView) itemView.findViewById(R.id.item_rating_tv_content);
            tvNumStars = (TextView) itemView.findViewById(R.id.item_rating_tv_num_stars);
            tvDate = (TextView) itemView.findViewById(R.id.item_rating_tv_date);
        }
    }
}