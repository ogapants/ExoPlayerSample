package com.github.ogapants.exoplayersample;

import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;


public class MainActivity extends AppCompatActivity {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);

        final String url = "http://html5demos.com/assets/dizzy.mp4";
        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        DataSource dataSource = new DefaultUriDataSource(this, "userAgent");
        SampleSource sampleSource = new ExtractorSampleSource(
                Uri.parse(url), dataSource, allocator, BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);
        TrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(
                this, sampleSource, MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        TrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(
                sampleSource, MediaCodecSelector.DEFAULT);
        TrackRenderer[] rendererArray = {videoRenderer, audioRenderer};

        ExoPlayer exoPlayer = ExoPlayer.Factory.newInstance(rendererArray.length);
        exoPlayer.prepare(rendererArray);
        exoPlayer.sendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surfaceView.getHolder().getSurface());
        exoPlayer.setPlayWhenReady(true);

        //Incidentally...
//        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(url), surfaceHolder);
//                mediaPlayer.start();
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//            }
//        });
    }
}
