package com.example.nizam.bakingapp;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class RecipeStepsActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        String bakingName = getIntent().getStringExtra("bakingName");
        String bakingId = getIntent().getStringExtra("bakingId");
        String stepId = getIntent().getStringExtra("stepId");
        String videoUrl = getIntent().getStringExtra("bakingVideo");
        String shortDesc = getIntent().getStringExtra("bakingShortDesc");
        String desc = getIntent().getStringExtra("bakingDesc");

        setTitle(bakingName);

        System.out.println("baking id from object : " + bakingId);
        System.out.println("step id from object : " + stepId);
        System.out.println("videoUrl from object : " + videoUrl);
        System.out.println("shortDesc from object : " + shortDesc);
        System.out.println("desc from object : " + desc);

        mShortdesc = findViewById(R.id.recipe_short_desc_text);
        mShortdesc.setText(shortDesc);
        mDesc = findViewById(R.id.recipe_desc_text);
        mDesc.setText(desc);
//        getVideoDetails(bakingId);


        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);

        if(!videoUrl.equals("nil")) {
            initializePlayer(videoUrl);
        }else{
            simpleExoPlayerView.setVisibility(View.GONE);
        }
    }

    private void initializePlayer(String videoUrl) {

        simpleExoPlayerView.requestFocus();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);
    }



}
