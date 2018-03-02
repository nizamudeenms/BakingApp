package com.example.nizam.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;


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
        ImageView im = holder.bakingImage;
        String imageName = null;
        imageName = "id" + bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_ID));

        holder.bakingItemName.setText(bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_NAME)));
        holder.bakingServings.setText(bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_SERVINGS)));

        if(!bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_IMAGE)).isEmpty()){
            Glide.with(rContext)
                    .load(bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_IMAGE)))
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .thumbnail(0.5f)
                    .into(im);
        }else{
            Glide.with(rContext)
                    .load(rContext.getResources().getIdentifier(imageName, "mipmap", rContext.getPackageName()))
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .thumbnail(0.5f)
                    .into(im);
        }
    }

    @Override
    public int getItemCount() {
        if (null == bakingCursor) return 0;
        return bakingCursor.getCount();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.info_text)
        TextView bakingItemName;

        @BindView(R.id.servings)
        TextView bakingServings;

        @BindView(R.id.bakingImage)
        ImageView bakingImage;

        Cursor bakingCursor;
        Context context;


        public RecipeViewHolder(View itemView, Cursor bakingCursor, Context context) {
            super(itemView);
            this.bakingCursor = bakingCursor;
            this.context = context;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            bakingCursor.moveToPosition(position);
            Intent intent = new Intent(context, RecipeStepsActivity.class);
            intent.putExtra("bakingId", bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_ID)));
            intent.putExtra("bakingName", bakingCursor.getString(bakingCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_NAME)));
            context.startActivity(intent);
        }
    }
}
