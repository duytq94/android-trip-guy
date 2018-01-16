package com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by duonghd on 12/7/2017.
 * duonghd1307@gmail.com
 */

public class RecyclerHotelFeedbackAdapter extends RecyclerView.Adapter<RecyclerHotelFeedbackAdapter.ViewHolder> {
    private Context context;
    private List<FeedbackResponse> feedbackResponses;

    public RecyclerHotelFeedbackAdapter(Context context, List<FeedbackResponse> feedbackResponses) {
        this.context = context;
        this.feedbackResponses = feedbackResponses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_feedback_hotel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FeedbackResponse feedbackResponse = feedbackResponses.get(position);
        if (feedbackResponse.getProfile() != null) {
            if (feedbackResponse.getProfile().getAvatar() != null) {
                Picasso.with(context).load(feedbackResponse.getProfile().getAvatar())
                        .error(R.drawable.photo_not_available)
                        .into(holder.civAvatar);
            } else {
                holder.civAvatar.setImageResource(R.drawable.ic_avatar);
            }
            holder.tvUsername.setText(feedbackResponse.getProfile().getUsername());
        }
        holder.srbRate.setRating(feedbackResponse.getRate());
        holder.tvContent.setText(feedbackResponse.getContent());
        try {
            String time = feedbackResponse.getUpdatedAt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'h:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            Date parseInput = simpleDateFormat.parse(time.split(Pattern.quote("."))[0]);
            holder.tvTime.setText(simpleDateFormat2.format(parseInput));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return feedbackResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civAvatar;
        private TextView tvUsername;
        private SimpleRatingBar srbRate;
        private TextView tvContent;
        private TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            civAvatar = (CircleImageView) itemView.findViewById(R.id.item_recycler_feedback_hotel_civ_avatar);
            tvUsername = (TextView) itemView.findViewById(R.id.item_recycler_feedback_hotel_tv_user_name);
            srbRate = (SimpleRatingBar) itemView.findViewById(R.id.item_recycler_feedback_hotel_srb_rate);
            tvContent = (TextView) itemView.findViewById(R.id.item_recycler_feedback_hotel_tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.item_recycler_feedback_hotel_tv_time);
        }
    }
}
