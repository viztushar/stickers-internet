package com.viztushar.stickers.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viztushar.stickers.R;
import com.viztushar.stickers.model.StickerModel;

import java.util.ArrayList;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    Context context;
    ArrayList<StickerModel> stickerModels;

    public StickerAdapter(Context context, ArrayList<StickerModel> stickerModels) {
        this.context = context;
        this.stickerModels = stickerModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sticker, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        StickerModel model =stickerModels.get(i);
        viewHolder.name.setText(model.stickerName);
        String url = "https://googlechrome.github.io/samples/picture-element/images/";
        Glide.with(context)
                .load(url + model.image_one)
                .into(viewHolder.imone);

        Glide.with(context)
                .load(url + model.image_two)
                .into(viewHolder.imtwo);

        Glide.with(context)
                .load(url + model.image_three)
                .into(viewHolder.imthree);

        Glide.with(context)
                .load(url + model.image_four)
                .into(viewHolder.imfour);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return stickerModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView imone,imtwo,imthree,imfour,download;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.rv_sticker_name);
            imone = itemView.findViewById(R.id.sticker_one);
            imtwo = itemView.findViewById(R.id.sticker_two);
            imthree = itemView.findViewById(R.id.sticker_three);
            imfour = itemView.findViewById(R.id.sticker_four);
            download = itemView.findViewById(R.id.download);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
