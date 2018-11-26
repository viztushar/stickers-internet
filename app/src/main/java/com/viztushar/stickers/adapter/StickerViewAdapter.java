package com.viztushar.stickers.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.viztushar.stickers.MainActivity;
import com.viztushar.stickers.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StickerViewAdapter extends RecyclerView.Adapter<StickerViewAdapter.ViewHolder> {

    ArrayList<String> strings;
    Context context;
    String id;

    public StickerViewAdapter(ArrayList<String> strings, String id, Context context) {
        this.strings = strings;
        this.context = context;
        this.id = id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_image, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        InputStream iStream = null;
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.sticker_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();
        Log.d("adapter 2", "onBindViewHolder: " + strings.get(i));
        Glide.with(context)
                .asBitmap()
                .load(strings.get(i))
                .apply(options)
                .apply(new RequestOptions().override(512, 512))
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (i == 0) {
                            Bitmap bitmap1 = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);
                            Matrix matrix = new Matrix();
                            Canvas canvas = new Canvas(bitmap1);
                            canvas.drawColor(Color.TRANSPARENT);
                            matrix.postTranslate(
                                    canvas.getWidth() / 2 - resource.getWidth() / 2,
                                    canvas.getHeight() / 2 - resource.getHeight() / 2
                            );
                            canvas.drawBitmap(resource, matrix, null);
                            MainActivity.SaveTryImage(bitmap1, String.valueOf(i));
                        }
                        Bitmap bitmap1 = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
                        Matrix matrix = new Matrix();
                        Canvas canvas = new Canvas(bitmap1);
                        canvas.drawColor(Color.TRANSPARENT);
                        matrix.postTranslate(
                                canvas.getWidth() / 2 - resource.getWidth() / 2,
                                canvas.getHeight() / 2 - resource.getHeight() / 2
                        );
                        canvas.drawBitmap(resource, matrix, null);
                        MainActivity.SaveImage(bitmap1, String.valueOf(i));
                        return false;
                    }
                })
                .into(viewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sticker_image);
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
