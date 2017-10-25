package com.dfa.vinatrip.domains.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;

import java.util.List;

/**
 * Created by duytq on 09/28/2017.
 */

public class StickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private StickerListener stickerListener;
    private List<Integer> listSticker;

    public StickerAdapter(List<Integer> listSticker, Context context) {
        this.stickerListener = (StickerListener) context;
        this.listSticker = listSticker;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new StickerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StickerHolder stickerHolder = (StickerHolder) holder;
        stickerHolder.ivSticker.setImageResource(listSticker.get(position));
        stickerHolder.ivSticker.setOnClickListener(v -> {
            stickerListener.onStickerClick(String.format("sticker%s", position + 1));
        });
    }

    @Override
    public int getItemCount() {
        if (listSticker != null) {
            return listSticker.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class StickerHolder extends RecyclerView.ViewHolder {
        private ImageView ivSticker;

        public StickerHolder(View itemView) {
            super(itemView);
            ivSticker = (ImageView) itemView.findViewById(R.id.item_sticker_iv_sticker);
        }
    }

    public interface StickerListener {
        void onStickerClick(String position);
    }

}