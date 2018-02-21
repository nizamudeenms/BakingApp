package com.example.nizam.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nizamudeenms on 17/02/18.
 */

public class BakingStepsAdapter extends RecyclerView.Adapter<BakingStepsAdapter.BakingStepsHolder> {

    private Context rContext;
    private ArrayList<String> stepsDescArr ;
    private ArrayList<BakingSteps> tempStepsArr ;

    private StepSelectedListener stepSelectedListener;

    public interface StepSelectedListener {

        void onStepSelected(int position);

    }

    public BakingStepsAdapter(Context rContext, ArrayList<String> stepsDescArr, ArrayList<BakingSteps> tempStepsArr, StepSelectedListener stepSelectedListener) {
        this.rContext = rContext;
        this.stepsDescArr = stepsDescArr;
        this.tempStepsArr = tempStepsArr;
        this.stepSelectedListener = stepSelectedListener;
    }

    @Override
    public BakingStepsAdapter.BakingStepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rContext = parent.getContext();
        View itemView = LayoutInflater.from(rContext).inflate(R.layout.content_steps,parent,false);
        return new BakingStepsHolder(itemView, stepsDescArr);
    }

    @Override
    public void onBindViewHolder(BakingStepsAdapter.BakingStepsHolder holder, int position) {
        String bakingSteps = stepsDescArr.get(position);
        holder.bakingSteps.setText(bakingSteps);

        if(tempStepsArr.get(position).getVideoUrl().isEmpty()){
            Glide.with(rContext)
                    .load(R.mipmap.youtube_disabled)
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .thumbnail(0.5f)
                    .into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return stepsDescArr.size();
    }

    public class BakingStepsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.steps_text_view)
        TextView bakingSteps;

        @BindView(R.id.steps_thumbnailUrl_image)
        ImageView thumbnail;

        private ArrayList<String> stepsDescArr ;


        public BakingStepsHolder(View itemView, ArrayList<String> stepsDescArr) {
            super(itemView);
            this.stepsDescArr = stepsDescArr;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Bundle b = new Bundle();
            int position = getAdapterPosition();
            b.putString("bakingVideo", tempStepsArr.get(position).getVideoUrl().isEmpty() ? "nil" : tempStepsArr.get(position).getVideoUrl());
            b.putString("stepId", tempStepsArr.get(position).getStepId());
            b.putString("bakingId", tempStepsArr.get(position).getBakingId());
            b.putString("shortDesc", tempStepsArr.get(position).getShortDesc());
            b.putString("desc", tempStepsArr.get(position).getDesc());
            stepSelectedListener.onStepSelected(position);
        }
    }
}