package com.example.nizam.bakingapp;


import android.database.sqlite.SQLiteDatabase;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsDetailFragment extends Fragment {
    private SQLiteDatabase mBakingDB;
    private ArrayList<String> ingredientsArray = new ArrayList<String>();
    TextView mShortdesc, mDesc;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    ProgressBar mProgressBar;
    RelativeLayout mRelativeLayout;
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
        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.exo_player_view);
        mProgressBar = view.findViewById(R.id.exo_player_progress_bar);
        mRelativeLayout = view.findViewById(R.id.exo_view_rel_layout);
        mRelativeLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);


        System.out.println("baking id from object : " + bakingId);
        System.out.println("step id from object : " + stepId);
        System.out.println("videoUrl from object : " + videoUrl);
        System.out.println("shortDesc from object : " + shortDesc);
        System.out.println("desc from object : " + desc);

//        System.out.println(" tempStepsArr video url " + tempStepsArr.get(0).getVideoUrl());

        mShortdesc = view.findViewById(R.id.recipe_short_desc_text);
        mShortdesc.setText(shortDesc);
        mDesc = view.findViewById(R.id.recipe_desc_text);
        mDesc.setText(desc);

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

//        videoUrl = tempStepsArr.get(0).getVideoUrl();

        if ( !videoUrl.equals("nil") ) {
            mShortdesc.setVisibility(View.VISIBLE);
            mDesc.setVisibility(View.VISIBLE);
            initializePlayer(videoUrl);
        }else {
            simpleExoPlayerView.setVisibility(View.GONE);
            if (!mShortdesc.getText().equals("nil")) {
                mShortdesc.setVisibility(View.VISIBLE);
                mDesc.setVisibility(View.VISIBLE);
            }else{
                mShortdesc.setVisibility(View.INVISIBLE);
                mDesc.setVisibility(View.INVISIBLE);
            }
        }
        return view;
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
                System.out.println(" inside onPlayerStateChanged");
                if (playbackState == Player.STATE_BUFFERING) {
                    System.out.println(" inside set prog visible");
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    System.out.println(" inside set prog invisible");
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
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);
    }


}
