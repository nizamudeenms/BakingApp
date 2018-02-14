package com.example.nizam.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsDetailFragment extends Fragment {

    @BindView(R.id.recipe_short_desc_text)
    TextView mShortdesc;

    @BindView(R.id.recipe_desc_text)
    TextView mDesc;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView simpleExoPlayerView;

    @BindView(R.id.exo_player_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.exo_view_rel_layout)
    RelativeLayout mRelativeLayout;

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
        ButterKnife.bind(this, view);
        mRelativeLayout.setVisibility(View.GONE);
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
            simpleExoPlayerView.setVisibility(View.GONE);
            if (!mShortdesc.getText().equals("nil")) {
                mShortdesc.setVisibility(View.VISIBLE);
                mDesc.setVisibility(View.VISIBLE);
            } else {
                mShortdesc.setVisibility(View.INVISIBLE);
                mDesc.setVisibility(View.INVISIBLE);
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

        simpleExoPlayerView.requestFocus();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        simpleExoPlayerView.setPlayer(player);
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
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mRelativeLayout.setVisibility(View.GONE);
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

        player.setPlayWhenReady(shouldAutoPlay);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (player != null) {
            player.stop();
            player.release();
        }
    }
}
