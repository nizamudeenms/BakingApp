package com.example.nizam.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsDetailFragment extends Fragment {
    private long currentPosition = 0;
    private Unbinder unbinder;

    @BindView(R.id.recipe_short_desc_text)
    TextView mShortdesc;

    @BindView(R.id.recipe_desc_text)
    TextView mDesc;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView simpleExoPlayerView;

    @BindView(R.id.exo_player_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.exo_player_thumbnail_image)
    ImageView mThumbnail;

    @BindView(R.id.step_details_frame_layout)
    FrameLayout mFramelayout;

    private SimpleExoPlayer player;

    private Timeline.Window window;

    private DataSource.Factory mediaDataSourceFactory;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;


    private ArrayList<BakingSteps> tempStepsArr = new ArrayList<BakingSteps>();
    String bakingId = "nil";
    String stepId = "nil";
    String videoUrl = "nil";
    String shortDesc = "nil";
    String desc = "nil";
    String thumbnail = "nil";

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBakingId() {
        return bakingId;
    }

    public void setBakingId(String bakingId) {
        this.bakingId = bakingId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public StepsDetailFragment() {
        // Required empty public constructor
    }

    public ArrayList<BakingSteps> getTempStepsArr() {
        return tempStepsArr;
    }

    public void setTempStepsArr(ArrayList<BakingSteps> tempStepsArr) {
        this.tempStepsArr = tempStepsArr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_steps_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (!thumbnail.equals("nil")) {
            Glide.with(getContext())
                    .load(getContext().getResources().getIdentifier(thumbnail, "mipmap", getContext().getPackageName()))
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .thumbnail(0.5f)
                    .into(mThumbnail);
        } else {
            mThumbnail.setVisibility(View.GONE);
        }

        mProgressBar.setVisibility(View.GONE);
        mShortdesc.setText(shortDesc);
        mDesc.setText(desc);

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

        if (!videoUrl.equals("nil")) {
            mShortdesc.setVisibility(View.VISIBLE);
            mDesc.setVisibility(View.VISIBLE);
            initializePlayer(videoUrl);
        } else {
            mFramelayout.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.GONE);
            if (mShortdesc.getText().equals("nil")) {
                mShortdesc.setVisibility(View.INVISIBLE);
                mDesc.setVisibility(View.INVISIBLE);
            } else {
                mShortdesc.setVisibility(View.VISIBLE);
                mDesc.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    private void initializePlayer(String videoUrl) {
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);


        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        simpleExoPlayerView.setPlayer(player);
        DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "Baking App"), bandwidthMeter1);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(videoUrl), dataSourceFactory, extractorsFactory, mainHandler, null);
        player.prepare(videoSource);

        if (currentPosition != 0) player.seekTo(currentPosition);
        player.setPlayWhenReady(true);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        if (player != null) {
            currentState.putLong("PLAYER_POSITION", player.getCurrentPosition());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (player != null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
