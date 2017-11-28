package com.example.nizam.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nizam on 07-Nov-2017 007.
 */

public class ReceipeAdapter extends RecyclerView.Adapter<ReceipeAdapter.ReceipeViewHolder> {
    private Context rContext;
    Cursor bakingCursor;

    public ReceipeAdapter(Context context, Cursor bakingCursor) {
        this.rContext = context;
        this.bakingCursor = bakingCursor;
    }

    @Override
    public ReceipeAdapter.ReceipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rContext = parent.getContext();
        View itemView = LayoutInflater.from(rContext).inflate(R.layout.acitvity_cardview, parent, false);
        return new ReceipeViewHolder(itemView, bakingCursor, rContext);
    }

    @Override
    public void onBindViewHolder(ReceipeAdapter.ReceipeViewHolder holder, int position) {
        bakingCursor.moveToPosition(position);
        ImageView im = holder.bakingImage;
        holder.bakingItemName.setText(bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_NAME)));
        System.out.println(" Image Url is : " + bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_IMAGE)));

//        Uri uri = Uri.fromFile(bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_IMAGE)));
//        Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        Bitmap bmThumbnail = null;
        try {
            bmThumbnail = retriveVideoFrameFromVideo(bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_IMAGE)));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmThumbnail.compress(Bitmap.CompressFormat.PNG,100,stream);
        Glide.with(rContext)
                .load(stream.toByteArray())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.bakingImage);


    }

    private byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            System.out.println("inside retieve video method");
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14) {
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            }
            else {
                mediaMetadataRetriever.setDataSource(videoPath);
            }
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }



    @Override
    public int getItemCount() {
        if (null == bakingCursor) return 0;
        return bakingCursor.getCount();
    }

    public class ReceipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        TextView bakingItemName;
        ImageView bakingImage;
        Cursor bakingCursor;
        Context context;


        public ReceipeViewHolder(View itemView, Cursor bakingCursor, Context context) {
            super(itemView);
            this.bakingCursor = bakingCursor;
            this.context = context;
            bakingItemName = (TextView) itemView.findViewById(R.id.info_text);
            bakingImage = (ImageView) itemView.findViewById(R.id.bakingImage);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
