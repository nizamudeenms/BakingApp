package com.example.nizam.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsDetailFragment extends Fragment implements ExoPlayer.EventListener {
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private long currentVideoPosition;


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

    @BindView(R.id.ll_container)
    LinearLayout llContainer;

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

    public ArrayList<BakingSteps> getTempStepsArr() {
        return tempStepsArr;
    }


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

    public void setTempStepsArr(ArrayList<BakingSteps> tempStepsArr) {
        this.tempStepsArr = tempStepsArr;
    }

    public StepsDetailFragment() {
        // Required empty public constructor
    }

    public static StepsDetailFragment newInstance(BakingSteps step, boolean isTablet) {
        StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        bundle.putBoolean("isTablet", isTablet);
        stepsDetailFragment.setArguments(bundle);

        return stepsDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_steps_detail, container, false);
        ButterKnife.bind(this, view);

        if (!thumbnail.equals("nil") && !thumbnail.isEmpty()) {
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

        if (!videoUrl.equals("nil")) {
            mShortdesc.setVisibility(View.VISIBLE);
            mDesc.setVisibility(View.VISIBLE);
            initializePlayer();
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


    private void initializePlayer() {
        initializeMediaSession();
        initializeVideoPlayer();
    }

    private void initializeMediaSession() {
        if (mMediaSession == null) {
            mMediaSession = new MediaSessionCompat(getActivity(), "Recipe");
            mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mMediaSession.setMediaButtonReceiver(null);

            mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                    PlaybackStateCompat.ACTION_PLAY |
                            PlaybackStateCompat.ACTION_PAUSE |
                            PlaybackStateCompat.ACTION_PLAY_PAUSE);

            mMediaSession.setPlaybackState(mStateBuilder.build());

            mMediaSession.setCallback(new MediaSessionCompat.Callback() {
                @Override
                public void onPlay() {
                    player.setPlayWhenReady(true);
                }

                @Override
                public void onPause() {
                    player.setPlayWhenReady(false);
                }

                @Override
                public void onSkipToPrevious() {
                    player.seekTo(0);
                }
            });

            mMediaSession.setActive(true);
        }
    }

    private void initializeVideoPlayer() {
        if (player == null && videoUrl != null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            player.seekTo(currentVideoPosition);
            simpleExoPlayerView.setPlayer(player);

            player.addListener(this);

            String userAgent = Util.getUserAgent(getActivity(), "Recipe");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl), new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(player != null) {
            currentVideoPosition = player.getCurrentPosition();
        }
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!videoUrl.equals("nil")) {
            initializePlayer();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

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
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    player.getCurrentPosition(), 1f);
            mProgressBar.setVisibility(View.GONE);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    player.getCurrentPosition(), 1f);
            mProgressBar.setVisibility(View.GONE);
        } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mStateBuilder != null) {
            mMediaSession.setPlaybackState(mStateBuilder.build());
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        showError("Cannot play! Check your internet connection");
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    public void showError(String message) {
        Snackbar snackbar = Snackbar
                .make(llContainer, message, Snackbar.LENGTH_LONG);

        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.red_700));

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red_100));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.red_700));
        snackbar.show();
    }
}
