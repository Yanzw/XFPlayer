package uk.me.feixie.xfplayer.activity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.MediaController;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.model.Video;
import uk.me.feixie.xfplayer.utils.GloableConstants;

public class OriginalShowActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private String mVideo_path;
    private VideoView mVvOriginalShow;
    private SharedPreferences mSharedPreferences;
    private long mCurrent_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_show);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mVideo_path = getIntent().getStringExtra("video_path");
        mCurrent_time = getIntent().getLongExtra("current_time", 0);

        initViews();
        // listen phone state and deal with different situations
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        long currentPosition = mVvOriginalShow.getCurrentPosition();
        String title = getVideoTitle(mVideo_path);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        mSharedPreferences.edit().putString(GloableConstants.VIDEO_TITLE,title).apply();
        mSharedPreferences.edit().putString(GloableConstants.VIDEO_WATCHED_DATE,currentTime).apply();
        mSharedPreferences.edit().putInt(GloableConstants.WATCHED_VIDEO,GloableConstants.TYPE_VIDEO).apply();
        mSharedPreferences.edit().putLong(GloableConstants.VIDEO_CURRENT_POSITION,currentPosition).apply();
        mSharedPreferences.edit().putString("video_file_path",mVideo_path).apply();
    }

    private void initViews() {
        mVvOriginalShow = (VideoView) findViewById(R.id.vvOriginalShow);
        if (mVvOriginalShow != null) {
            mVvOriginalShow.setMediaController(new MediaController(this));
            mVvOriginalShow.setOnPreparedListener(this);
            mVvOriginalShow.setOnErrorListener(this);
            mVvOriginalShow.setOnCompletionListener(this);
            if (!TextUtils.isEmpty(mVideo_path)) {
                mVvOriginalShow.setVideoPath(mVideo_path);
                if (mCurrent_time!=0) {
                    mVvOriginalShow.seekTo((int) mCurrent_time);
                }
                mVvOriginalShow.start();
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

    private String getVideoTitle(String video_path) {
        int index = video_path.lastIndexOf(".");
        int indexOf = video_path.lastIndexOf("/")+1;
        return video_path.substring(indexOf,index);
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
                    if (mVvOriginalShow.isPlaying()) {
                        mVvOriginalShow.pause();
                    }
                    break;
            }
        }
    };
}
