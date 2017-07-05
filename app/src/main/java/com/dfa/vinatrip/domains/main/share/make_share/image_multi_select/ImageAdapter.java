package com.dfa.vinatrip.domains.main.share.make_share.image_multi_select;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

/**
 * Created by duytq on 7/4/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private String[] arrPath;

    private long[] arrayId;

    private int size = 0;
    private boolean[] imageSlect;
    private callbackMainActivity callback;
    private int count = 0;
    private static int MAX_SELECT = 4;

    public ImageAdapter(Context context, String[] arrPath, long[] arrayBitmap, int size) {
        this.context = context;
        this.arrPath = arrPath;
        this.arrayId = arrayBitmap;
        this.size = size;

        this.imageSlect = new boolean[arrPath.length];
        for (int i = 0; i < imageSlect.length; i++) {
            imageSlect[i] = false;
        }

        this.callback = (callbackMainActivity) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.height = this.size;

        if (imageSlect[position]) {
            holder.viewOverlay.setVisibility(View.VISIBLE);
        } else {
            holder.viewOverlay.setVisibility(View.GONE);
        }

        Picasso.with(context).load("file://" + arrPath[position])
               .resize(96, 96)
               .centerCrop()
               .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrPath.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private View viewOverlay;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_image_view);
            viewOverlay = (View) itemView.findViewById(R.id.item_image_view_overlay);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (imageSlect[getAdapterPosition()]) {
                count = count - 1;
                imageSlect[getAdapterPosition()] = false;
                viewOverlay.setVisibility(View.GONE);
                callback.selectClick(arrPath[getAdapterPosition()]);
            } else {
                if (count < MAX_SELECT) {
                    count = count + 1;
                    imageSlect[getAdapterPosition()] = true;
                    viewOverlay.setVisibility(View.VISIBLE);
                    callback.selectClick(arrPath[getAdapterPosition()]);
                } else {
                    callback.selectMax();
                }
            }
        }
    }

    public interface callbackMainActivity {
        void selectClick(String imagePath);

        void selectMax();
    }
}