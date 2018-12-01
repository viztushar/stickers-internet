package com.viztushar.stickers.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.viztushar.stickers.MainActivity;
import com.viztushar.stickers.R;
import com.viztushar.stickers.Sticker;
import com.viztushar.stickers.StickerPack;
import com.viztushar.stickers.activity.StickerDetailsActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.viztushar.stickers.MainActivity.EXTRA_STICKERPACK;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    Context context;
    ArrayList<StickerPack> StickerPack;

    public StickerAdapter(Context context, ArrayList<StickerPack> StickerPack) {
        this.context = context;
        this.StickerPack = StickerPack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sticker, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final List<Sticker> models = StickerPack.get(i).getStickers();
        viewHolder.name.setText(StickerPack.get(i).name);
        final String url = "https://raw.githubusercontent.com/viztushar/Urban/master/thub5/";
        Glide.with(context)
                .load(url + models.get(0).imageFileName.replace(".webp",".png"))
                .into(viewHolder.imone);

        Glide.with(context)
                .load(url + models.get(1).imageFileName.replace(".webp",".png"))
                .into(viewHolder.imtwo);

        Glide.with(context)
                .load(url + models.get(2).imageFileName.replace(".webp",".png"))
                .into(viewHolder.imthree);

        if (models.size() > 3) {
            Glide.with(context)
                    .load(url + models.get(3).imageFileName.replace(".webp",".png"))
                    .into(viewHolder.imfour);
        } else {
            viewHolder.imfour.setVisibility(View.INVISIBLE);
        }

        Glide.with(context)
                .asBitmap()
                .load("https://googlechrome.github.io/samples/picture-element/images/" + StickerPack.get(i).trayImageFile.replace("_"," "))
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Bitmap bitmap1 = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);
                        Matrix matrix = new Matrix();
                        Canvas canvas = new Canvas(bitmap1);
                        canvas.drawColor(Color.TRANSPARENT);
                        matrix.postTranslate(
                                canvas.getWidth() / 2 - resource.getWidth() / 2,
                                canvas.getHeight() / 2 - resource.getHeight() / 2
                        );
                        canvas.drawBitmap(resource, matrix, null);
                        MainActivity.SaveTryImage(bitmap1,StickerPack.get(i).trayImageFile,StickerPack.get(i).identifier);
                        return false;
                    }
                })
                .submit();
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, StickerDetailsActivity.class)
                                .putExtra(EXTRA_STICKERPACK, StickerPack.get(viewHolder.getAdapterPosition())),
                        ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(),
                                v.getHeight()).toBundle());
            }
        });

        File file = new File(MainActivity.path + "/" + StickerPack.get(i).identifier + "/" + models.get(0).imageFileName);
        if (!file.exists()) {
            viewHolder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("adapter", "onClick: " + StickerPack.get(viewHolder.getAdapterPosition()).getStickers().size());
                    ((Activity) context).runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.download.setVisibility(View.INVISIBLE);
                                    viewHolder.bar.setVisibility(View.VISIBLE);
                                    for (final Sticker s : StickerPack.get(viewHolder.getAdapterPosition()).getStickers()) {
                                        Log.d("adapter", "onClick: " + s.imageFileName);
                                        Glide.with(context)
                                                .asBitmap()
                                                .apply(new RequestOptions().override(512, 512))
                                                .load(url + s.imageFileName.replace(".webp",".png"))
                                                .addListener(new RequestListener<Bitmap>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                        Bitmap bitmap1 = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
                                                        Matrix matrix = new Matrix();
                                                        Canvas canvas = new Canvas(bitmap1);
                                                        canvas.drawColor(Color.TRANSPARENT);
                                                        matrix.postTranslate(
                                                                canvas.getWidth() / 2 - resource.getWidth() / 2,
                                                                canvas.getHeight() / 2 - resource.getHeight() / 2
                                                        );
                                                        canvas.drawBitmap(resource, matrix, null);
                                                        MainActivity.SaveImage(bitmap1, s.imageFileName, StickerPack.get(viewHolder.getAdapterPosition()).identifier);
                                                        return true;
                                                    }
                                                }).submit();
                                    }
                                    viewHolder.download.setVisibility(View.INVISIBLE);
                                    viewHolder.bar.setVisibility(View.INVISIBLE);
                                }
                            }
                    );

                }
            });
        } else {
            viewHolder.rl.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return StickerPack.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView imone, imtwo, imthree, imfour, download;
        CardView cardView;
        ProgressBar bar;
        RelativeLayout rl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.rv_sticker_name);
            imone = itemView.findViewById(R.id.sticker_one);
            imtwo = itemView.findViewById(R.id.sticker_two);
            imthree = itemView.findViewById(R.id.sticker_three);
            imfour = itemView.findViewById(R.id.sticker_four);
            download = itemView.findViewById(R.id.download);
            cardView = itemView.findViewById(R.id.card_view);
            bar = itemView.findViewById(R.id.progressBar);
            rl = itemView.findViewById(R.id.download_layout);
        }
    }
}
