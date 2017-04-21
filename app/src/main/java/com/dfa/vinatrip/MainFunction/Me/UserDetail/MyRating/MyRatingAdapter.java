package com.dfa.vinatrip.MainFunction.Me.UserDetail.MyRating;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.Rating.UserRating;
import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyRatingAdapter extends RecyclerView.Adapter<MyRatingAdapter.RatingViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<UserRating> myRatingList;

    public MyRatingAdapter(Context context, List<UserRating> myRatingList) {
        this.context = context;
        this.myRatingList = myRatingList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RatingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_my_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RatingViewHolder holder, int position) {
        UserRating myRating = myRatingList.get(position);

        holder.tvLocationName.setText(myRating.getLocationName());
        holder.tvNumStars.setText(myRating.getNumStars());
        holder.tvContent.setText(myRating.getContent());
        holder.tvDate.setText(myRating.getDate());

        // show symbol star
        String rate = null;
        switch (Integer.parseInt(myRating.getNumStars())) {
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

        Picasso.with(context).load(myRating.getLocationPhoto()).into(holder.civLocation);
    }

    @Override
    public int getItemCount() {
        return myRatingList.size();
    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocationName, tvNumStars, tvContent, tvDate;
        private CircleImageView civLocation;

        public RatingViewHolder(View itemView) {
            super(itemView);
            tvLocationName = (TextView) itemView.findViewById(R.id.item_my_rating_tv_location_name);
            tvNumStars = (TextView) itemView.findViewById(R.id.item_my_rating_tv_num_stars);
            tvContent = (TextView) itemView.findViewById(R.id.item_my_rating_tv_content);
            tvDate = (TextView) itemView.findViewById(R.id.item_my_rating_tv_date);
            civLocation = (CircleImageView) itemView.findViewById(R.id.item_my_rating_civ_location);
        }
    }
}
