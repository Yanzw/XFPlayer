package uk.me.feixie.xfplayer.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import uk.me.feixie.xfplayer.R;

public class ShowActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private VideoView vvShow;
    private RelativeLayout rlShow;
    private static final double SCALE_RATIO = 2.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        //check vitamio ready
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;

        initViews();
    }

    private void initViews() {
        rlShow = (RelativeLayout) findViewById(R.id.rlShow);
        vvShow = (VideoView) findViewById(R.id.vvShow);

//        System.out.println(getResources().getConfiguration().orientation+"///"+ Configuration.ORIENTATION_LANDSCAPE);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rlShow.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            rlShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH,0);
        } else {
            int height =  getWindowManager().getDefaultDisplay().getHeight();
            rlShow.getLayoutParams().height = (int) (height/SCALE_RATIO);
            rlShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().height = (int) (height/SCALE_RATIO);
            vvShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH,0);
        }

//        vvShow.setVideoPath("http://192.168.0.2/tycq.mkv");
        vvShow.setVideoPath("http://192.168.0.2/tjxs.rmvb");
//        vvShow.setVideoPath("http://zv.3gv.ifeng.com/live/zixun800k.m3u8");
//        MediaController controller = new MediaController(this);
        vvShow.setMediaController(new MediaController(this));
        vvShow.setOnCompletionListener(this);
        vvShow.setOnErrorListener(this);
        vvShow.setOnPreparedListener(this);
        vvShow.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

//        int height = getWindowManager().getDefaultDisplay().getHeight();
//        System.out.println(height);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            vvShow.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH,0);
            rlShow.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            rlShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
        } else {
//            vvShow.setVideoLayout(VideoView.VIDEO_LAYOUT_ORIGIN,0);
//            rlShow.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//            rlShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
//            mVideoView.setVideoScale(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(TvPlayActivity.this, 230));
//            vvShow.getHolder().setFixedSize(vvShow.getLayoutParams().width, vvShow.getLayoutParams().height);
            int height =  getWindowManager().getDefaultDisplay().getHeight();
            rlShow.getLayoutParams().height = (int) (height/SCALE_RATIO);
            rlShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().height = (int) (height/SCALE_RATIO);
            vvShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH,0);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(this, "onCompletion", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "onError", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Toast.makeText(this, "onPrepared", Toast.LENGTH_SHORT).show();
    }
}
