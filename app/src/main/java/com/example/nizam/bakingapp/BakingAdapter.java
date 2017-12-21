package com.example.nizam.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Nizam on 07-Nov-2017 007.
 */

public class BakingAdapter extends RecyclerView.Adapter<BakingAdapter.RecipeViewHolder> {
    private Context rContext;
    Cursor bakingCursor;

    public BakingAdapter(Context context, Cursor bakingCursor) {
        this.rContext = context;
        this.bakingCursor = bakingCursor;
    }

    @Override
    public BakingAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rContext = parent.getContext();
        View itemView = LayoutInflater.from(rContext).inflate(R.layout.acitvity_cardview, parent, false);
        return new RecipeViewHolder(itemView, bakingCursor, rContext);
    }

    @Override
    public void onBindViewHolder(BakingAdapter.RecipeViewHolder holder, int position) {
        bakingCursor.moveToPosition(position);
        System.out.println("position : "+ position);
        ImageView im = holder.bakingImage;
        String imageName = null;
//        Bitmap bmThumbnail = null;
        holder.bakingItemName.setText(bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_NAME)));
        holder.bakingServings.setText(bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_SERVINGS)));

//        new Thread(new Runnable() {
//            public void run() {
//        byte[] bm = bakingCursor.getBlob(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_IMAGE));
//        Bitmap bmThumbnail = null;
//        bmThumbnail = BitmapFactory.decodeByteArray(bm, 0, bm.length);
//        try {
//            bmThumbnail = retriveVideoFrameFromVideo(bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_IMAGE)));
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bmThumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);

        imageName = "id" + bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_ID));
        System.out.println("baking id : " + imageName);
//        Uri  img = Uri.parse("R.mipmap."+imageName);
//        System.out.println(img);
        System.out.println(rContext.getPackageName());

        Glide.with(rContext)
                .load(rContext.getResources().getIdentifier(imageName, "mipmap", rContext.getPackageName()))
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .thumbnail(0.5f)
                .into(im);
        //            }
//        }).start();


    }

//    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
//            throws Throwable {
//        Bitmap bitmap = null;
//        MediaMetadataRetriever mediaMetadataRetriever = null;
//        try {
//            System.out.println("inside retieve video method");
//            mediaMetadataRetriever = new MediaMetadataRetriever();
//            if (Build.VERSION.SDK_INT >= 14) {
//                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
//            } else {
//                mediaMetadataRetriever.setDataSource(videoPath);
//            }
//            bitmap = mediaMetadataRetriever.getFrameAtTime();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Throwable(
//                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
//                            + e.getMessage());
//
//        } finally {
//            if (mediaMetadataRetriever != null) {
//                mediaMetadataRetriever.release();
//            }
//        }
//        return bitmap;
//    }


    @Override
    public int getItemCount() {
        if (null == bakingCursor) return 0;
        return bakingCursor.getCount();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        TextView bakingItemName;
        TextView bakingServings;
        ImageView bakingImage;
        Cursor bakingCursor;
        Context context;


        public RecipeViewHolder(View itemView, Cursor bakingCursor, Context context) {
            super(itemView);
            this.bakingCursor = bakingCursor;
            this.context = context;
            bakingItemName = itemView.findViewById(R.id.info_text);
            bakingImage = itemView.findViewById(R.id.bakingImage);
            bakingServings = itemView.findViewById(R.id.servings);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            System.out.println("position; " + position);
            bakingCursor.moveToPosition(position);
            System.out.println("bakingId : " + bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_ID));
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("bakingId", bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_ID)));
            intent.putExtra("bakingName", bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_NAME)));
            context.startActivity(intent);
        }
    }
}
