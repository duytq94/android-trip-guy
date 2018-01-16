package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_rating.adapter;

import android.content.Context;
import android.graphics.Color;
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
 * Created by duonghd on 1/6/2018.
 * duonghd1307@gmail.com
 */

public class RecyclerMyFeedbackAdapter extends RecyclerView.Adapter<RecyclerMyFeedbackAdapter.ViewHolder> {
    private Context context;
    private List<FeedbackResponse> feedbackResponses;

    public RecyclerMyFeedbackAdapter(Context context, List<FeedbackResponse> feedbackResponses) {
        this.context = context;
        this.feedbackResponses = feedbackResponses;
    }

    @Override
    public RecyclerMyFeedbackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_feedback_me, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FeedbackResponse feedbackResponse = feedbackResponses.get(position);
        switch (feedbackResponse.getType()) {
            case "feedback_event":
                if (feedbackResponse.getEvent().getAvatar() != null) {
                    Picasso.with(context).load(feedbackResponse.getEvent().getAvatar())
                            .error(R.drawable.photo_not_available)
                            .into(holder.civAvatar);
                } else {
                    holder.civAvatar.setImageResource(R.drawable.ic_avatar);
                }
                holder.tvUsername.setText(feedbackResponse.getEvent().getName());
                holder.srbRate.setFillColor(Color.parseColor("#c31cff"));
                holder.srbRate.setBorderColor(Color.parseColor("#c31cff"));
                break;
            case "feedback_hotel":
                if (feedbackResponse.getHotel().getAvatar() != null) {
                    Picasso.with(context).load(feedbackResponse.getHotel().getAvatar())
                            .error(R.drawable.photo_not_available)
                            .into(holder.civAvatar);
                } else {
                    holder.civAvatar.setImageResource(R.drawable.ic_avatar);
                }
                holder.tvUsername.setText(feedbackResponse.getHotel().getName());
                holder.srbRate.setFillColor(Color.parseColor("#ffdd00"));
                holder.srbRate.setBorderColor(Color.parseColor("#ffdd00"));
                break;
            case "feedback_food":
                if (feedbackResponse.getFood().getAvatar() != null) {
                    Picasso.with(context).load(feedbackResponse.getFood().getAvatar())
                            .error(R.drawable.photo_not_available)
                            .into(holder.civAvatar);
                } else {
                    holder.civAvatar.setImageResource(R.drawable.ic_avatar);
                }
                holder.tvUsername.setText(feedbackResponse.getFood().getName());
                holder.srbRate.setFillColor(Color.parseColor("#ff0090"));
                holder.srbRate.setBorderColor(Color.parseColor("#ff0090"));
                break;
            case "feedback_place":
                if (feedbackResponse.getPlace().getAvatar() != null) {
                    Picasso.with(context).load(feedbackResponse.getPlace().getAvatar())
                            .error(R.drawable.photo_not_available)
                            .into(holder.civAvatar);
                } else {
                    holder.civAvatar.setImageResource(R.drawable.ic_avatar);
                }
                holder.tvUsername.setText(feedbackResponse.getPlace().getName());
                holder.srbRate.setFillColor(Color.parseColor("#088b0c"));
                holder.srbRate.setBorderColor(Color.parseColor("#088b0c"));
                break;
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
            civAvatar = (CircleImageView) itemView.findViewById(R.id.item_recycler_feedback_me_civ_avatar);
            tvUsername = (TextView) itemView.findViewById(R.id.item_recycler_feedback_me_tv_user_name);
            srbRate = (SimpleRatingBar) itemView.findViewById(R.id.item_recycler_feedback_me_srb_rate);
            tvContent = (TextView) itemView.findViewById(R.id.item_recycler_feedback_me_tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.item_recycler_feedback_me_tv_time);
        }
    }
}
