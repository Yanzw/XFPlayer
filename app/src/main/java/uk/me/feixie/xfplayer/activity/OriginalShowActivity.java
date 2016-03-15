package uk.me.feixie.xfplayer.activity;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.MediaController;
import android.widget.VideoView;

import uk.me.feixie.xfplayer.R;

public class OriginalShowActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private String mVideo_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_show);
        mVideo_path = getIntent().getStringExtra("video_path");
        initViews();
    }

    private void initViews() {
        VideoView vvOriginalShow = (VideoView) findViewById(R.id.vvOriginalShow);
        if (vvOriginalShow != null) {
            vvOriginalShow.setMediaController(new MediaController(this));
            vvOriginalShow.setOnPreparedListener(this);
            vvOriginalShow.setOnErrorListener(this);
            vvOriginalShow.setOnCompletionListener(this);
            if (!TextUtils.isEmpty(mVideo_path)) {
                vvOriginalShow.setVideoPath(mVideo_path);
                vvOriginalShow.start();
            }
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
