package uk.me.feixie.xfplayer.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.utils.GloableConstants;

public class ShowActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {

    private VideoView vvShow;
    private ProgressBar pbMovie;
    private RelativeLayout rlShow;
    private static final double SCALE_RATIO = 2.5;
    private String mVideo_path;
    private SharedPreferences mSharedPreferences;
    private long mCurrent_time;
    //    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //check vitamio ready
//        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
//            return;
        Vitamio.isInitialized(this);

        mVideo_path = getIntent().getStringExtra("video_path");
        mCurrent_time = getIntent().getLongExtra("current_time", 0);

        initViews();

        // listen phone state and deal with different situations
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onBackPressed() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        if (mVideo_path.contains(".m3u8")) {
//            System.out.println(mVideo_path);
            String title = getVideoTitle(mVideo_path);


            mSharedPreferences.edit().putString(GloableConstants.LIVE_TITLE, title).apply();
            mSharedPreferences.edit().putString(GloableConstants.LIVE_WATCHED_DATE, currentTime).apply();
            mSharedPreferences.edit().putInt(GloableConstants.WATCHED_LIVE, GloableConstants.TYPE_LIVE).apply();
            mSharedPreferences.edit().putString("live_file_path", mVideo_path).apply();

        } else {
            long currentPosition = vvShow.getCurrentPosition();
            String title = getVideoTitle(mVideo_path);
            long duration = vvShow.getDuration();

            mSharedPreferences.edit().putString(GloableConstants.MOVIE_TITLE, title).apply();
            mSharedPreferences.edit().putString(GloableConstants.MOVIE_WATCHED_DATE, currentTime).apply();
            mSharedPreferences.edit().putInt(GloableConstants.WATCHED_MOVIE, GloableConstants.TYPE_MOVIE).apply();
            mSharedPreferences.edit().putLong(GloableConstants.MOVIE_CURRENT_POSITION, currentPosition).apply();
            mSharedPreferences.edit().putLong(GloableConstants.MOVIE_DURATION, duration).apply();
            mSharedPreferences.edit().putString("movie_file_path", mVideo_path).apply();
        }

        super.onBackPressed();
    }

    private String getVideoTitle(String video_path) {
        int index = video_path.lastIndexOf(".");
        int indexOf = video_path.lastIndexOf("/") + 1;
        return video_path.substring(indexOf, index);
    }

    private void initViews() {
        rlShow = (RelativeLayout) findViewById(R.id.rlShow);
        vvShow = (VideoView) findViewById(R.id.vvShow);
        pbMovie = (ProgressBar) findViewById(R.id.pbMovie);

//        System.out.println(getResources().getConfiguration().orientation+"///"+ Configuration.ORIENTATION_LANDSCAPE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rlShow.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            rlShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
        } else {
            int height = getWindowManager().getDefaultDisplay().getHeight();
            rlShow.getLayoutParams().height = (int) (height / SCALE_RATIO);
            rlShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().height = (int) (height / SCALE_RATIO);
            vvShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
        }

//        vvShow.setVideoPath("http://192.168.0.2/tycq.mkv");
//        vvShow.setVideoPath("http://192.168.0.2/tjxs.rmvb");
//        vvShow.setVideoPath("http://zv.3gv.ifeng.com/live/zixun800k.m3u8");
//        MediaController controller = new MediaController(this);
        if (!TextUtils.isEmpty(mVideo_path)) {
            vvShow.setVideoPath(mVideo_path);
            vvShow.setMediaController(new MediaController(this));
            vvShow.setOnCompletionListener(this);
            vvShow.setOnPreparedListener(this);
            vvShow.setOnErrorListener(this);
            vvShow.setOnBufferingUpdateListener(this);
            if (mCurrent_time != 0) {
                vvShow.seekTo(mCurrent_time);
            }
            vvShow.start();

        } else {
            Toast.makeText(this, "No video found!", Toast.LENGTH_SHORT).show();
        }

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
            int height = getWindowManager().getDefaultDisplay().getHeight();
            rlShow.getLayoutParams().height = (int) (height / SCALE_RATIO);
            rlShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.getLayoutParams().height = (int) (height / SCALE_RATIO);
            vvShow.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            vvShow.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        Toast.makeText(this, "onCompletion", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
//        Toast.makeText(this, "onError", Toast.LENGTH_SHORT).show();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        },2000);
        return false;
    }


    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    if (vvShow.isPlaying()) {
                        vvShow.pause();
                    }
                    break;
            }
        }
    };

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        pbMovie.setVisibility(View.GONE);
    }
}
