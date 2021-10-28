package com.theway4wardacademy.report.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.theway4wardacademy.report.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class FullVideo extends AppCompatActivity implements CacheListener {
    SimpleExoPlayer exoPlayer;
    PlayerView playerView;
    ProgressBar progressBar;
    Handler handler;
    String url;
    TextView again;
    Runnable runnable;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video);

        playerView = findViewById(R.id.ep_video_view);

        progressBar = findViewById(R.id.progressBar);
        again = (TextView)findViewById(R.id.again);

        url = getIntent().getStringExtra("url").toString();
        checkCachedState();
        exoPlayer = setupPlayer();
        exoPlayer.setPlayWhenReady(true);




        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState){
                    case SimpleExoPlayer.STATE_READY:

                        break;
                    case SimpleExoPlayer.STATE_ENDED:
                        again.setVisibility(View.VISIBLE);
                        break;

                }
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress((int) ((exoPlayer.getCurrentPosition()*100)/exoPlayer.getDuration()));
                        handler.postDelayed(runnable, 1000);
                    }
                };
                handler.postDelayed(runnable, 0);


            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {


            }

        });


        progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekto();
                return false;
            }
        });

    }






    @Override
    protected void onStart() {

        super.onStart();
    }

    private void seekto(){
        long videoPosition = exoPlayer.getDuration() * progressBar.getProgress() / 100;
        exoPlayer.seekTo(videoPosition);
        exoPlayer.setPlayWhenReady(true);
    }
    private void checkCachedState() {
        HttpProxyCacheServer proxy = AAUA_Updates.getProxy(FullVideo.this);
        boolean fullyCached = proxy.isCached(url);
        setCachedState(fullyCached);
        if (fullyCached) {
            progressBar.setSecondaryProgress(100);
       }
    }


    public void setDetailsVideo(String name, String Videourl){


        try {
            HttpProxyCacheServer proxy = AAUA_Updates.getProxy(this);
            proxy.registerCacheListener(this, Videourl);

            String proxyUrl = proxy.getProxyUrl(Videourl);



            DefaultTrackSelector.ParametersBuilder trackSelector1 = new DefaultTrackSelector()
                    .buildUponParameters()
                    .setForceLowestBitrate(true)
                    //.setMaxVideoSizeSd()
                    .setMaxVideoFrameRate(24);
                  //  .setMaxVideoBitrate(10000);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(FullVideo.this).build();
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            trackSelector.setParameters(trackSelector1);

            DefaultLoadControl.Builder defaultLoadControlBuilder = new DefaultLoadControl.Builder();
            defaultLoadControlBuilder.setAllocator(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE));
            defaultLoadControlBuilder.setBufferDurationsMs(3000, 3000, 1500, 3000);
            defaultLoadControlBuilder.setTargetBufferBytes(-1);
            defaultLoadControlBuilder.setPrioritizeTimeOverSizeThresholds(true);
            DefaultLoadControl defaultLoadControl = defaultLoadControlBuilder.createDefaultLoadControl();

            exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(FullVideo.this,trackSelector,defaultLoadControl);

            Uri video = Uri.parse(proxyUrl);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(video,dataSourceFactory,extractorsFactory,null,null);
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);


        }catch (Exception e){
            Log.e("ViewHolder","exoplayer error"+e.toString());
        }


    }

    private void pausePlayer(SimpleExoPlayer player) {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }
    private void playPlayer(SimpleExoPlayer player) {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    private void seekTo(SimpleExoPlayer player, long positionInMS) {
        if (player != null) {
            player.seekTo(positionInMS);
        }
    }
    private void stopPlayer(){
        playerView.setPlayer(null);
        exoPlayer.release();
        exoPlayer = null;
    }









    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.release();
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        progressBar.setSecondaryProgress(percentsAvailable);
        setCachedState(percentsAvailable == 100);
    }

    private void setCachedState(boolean cached) {
//        int statusIconId = cached ? R.drawable.ic_cloud_done : R.drawable.ic_cloud_download;
//        cacheStatusImageView.setImageResource(statusIconId);
    }

    private void updateVideoProgress() {
        long videoProgress = exoPlayer.getCurrentPosition() * 100 / exoPlayer.getDuration();
        progressBar.setProgress((int) videoProgress);
    }





    private SimpleExoPlayer setupPlayer() {
        playerView.setUseController(false);
        HttpProxyCacheServer proxy = AAUA_Updates.getProxy(FullVideo.this);
        proxy.registerCacheListener(this, url);
        String proxyUrl = proxy.getProxyUrl(url);

        SimpleExoPlayer exoPlayer = newSimpleExoPlayer();
        playerView.setPlayer(exoPlayer);

        MediaSource videoSource = newVideoSource(proxyUrl);
        exoPlayer.prepare(videoSource);

        return exoPlayer;
    }

    private SimpleExoPlayer newSimpleExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        return ExoPlayerFactory.newSimpleInstance(FullVideo.this, trackSelector, loadControl);

    }


    private MediaSource newVideoSource(String url) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        String userAgent = Util.getUserAgent(FullVideo.this, "Lic");
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(FullVideo.this, userAgent, bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        return new ExtractorMediaSource(Uri.parse(url), dataSourceFactory, extractorsFactory, null, null);
    }





    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




    @Override
    protected void onStop() {
        AAUA_Updates.getProxy(FullVideo.this).unregisterCacheListener(this);
        super.onStop();
    }





    public void againWatch(View view){
        again.setVisibility(View.GONE);
        exoPlayer.seekTo(0);
        exoPlayer.setPlayWhenReady(true);
    }

}
