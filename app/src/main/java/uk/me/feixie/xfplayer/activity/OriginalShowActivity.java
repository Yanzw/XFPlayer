package uk.me.feixie.xfplayer.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.MediaRouteButton;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.libraries.cast.companionlibrary.cast.BaseCastManager;
import com.google.android.libraries.cast.companionlibrary.cast.CastConfiguration;
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;
import com.google.android.libraries.cast.companionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions.CastException;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions.NoConnectionException;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions.TransientNetworkDisconnectionException;

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
    private MediaController mController;
    private VideoCastManager mVideoCastManager;
    private MediaRouteButton mMediaRouteButton;
    private VideoCastConsumerImpl mCastConsumer;
    private MediaInfo mSelectedMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseCastManager.checkGooglePlayServices(this);
        setContentView(R.layout.activity_original_show);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mVideo_path = getIntent().getStringExtra("video_path");
        mCurrent_time = getIntent().getLongExtra("current_time", 0);

        initCast();

        initViews();

        // listen phone state and deal with different situations
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoCastManager = VideoCastManager.getInstance();
        mVideoCastManager.incrementUiCounter();
        mVideoCastManager.addVideoCastConsumer(mCastConsumer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoCastManager.decrementUiCounter();
        mVideoCastManager.removeVideoCastConsumer(mCastConsumer);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        long currentPosition = mVvOriginalShow.getCurrentPosition();
        long duration = mVvOriginalShow.getDuration();
        String title = getVideoTitle(mVideo_path);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        mSharedPreferences.edit().putString(GloableConstants.VIDEO_TITLE, title).apply();
        mSharedPreferences.edit().putString(GloableConstants.VIDEO_WATCHED_DATE, currentTime).apply();
        mSharedPreferences.edit().putInt(GloableConstants.WATCHED_VIDEO, GloableConstants.TYPE_VIDEO).apply();
        mSharedPreferences.edit().putLong(GloableConstants.VIDEO_CURRENT_POSITION, currentPosition).apply();
        mSharedPreferences.edit().putLong(GloableConstants.VIDEO_DURATION, duration).apply();
        mSharedPreferences.edit().putString("video_file_path", mVideo_path).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoCastManager != null) {
            mVideoCastManager = null;
        }
    }

    private void initViews() {

        mVvOriginalShow = (VideoView) findViewById(R.id.vvOriginalShow);
//        ivLocalVideoCast = (ImageView) findViewById(R.id.ivLocalVideoCast);
        mMediaRouteButton = (MediaRouteButton) findViewById(R.id.local_media_router_button);
        mVideoCastManager = VideoCastManager.getInstance();
        mVideoCastManager.addMediaRouterButton(mMediaRouteButton);

//        System.out.println(mVideoCastManager.isAnyRouteAvailable());
//        mMediaRouteButton.setVisibility(mVideoCastManager.isAnyRouteAvailable() ? View.VISIBLE : View.INVISIBLE);

        mSelectedMedia = new MediaInfo.Builder(mVideo_path)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("video/mp4")
                .build();

        setupCastListener();

        if (mVvOriginalShow != null) {
            mController = new MediaController(this);
            mVvOriginalShow.setMediaController(mController);
            mVvOriginalShow.setOnPreparedListener(this);
            mVvOriginalShow.setOnErrorListener(this);
            mVvOriginalShow.setOnCompletionListener(this);
            if (!TextUtils.isEmpty(mVideo_path)) {
                mVvOriginalShow.setVideoPath(mVideo_path);
                if (mCurrent_time != 0) {
                    mVvOriginalShow.seekTo((int) mCurrent_time);
                }
                mVvOriginalShow.start();
            }
        }

    }

    private void initCast() {

        CastConfiguration options = new CastConfiguration.Builder(getString(R.string.app_id))
                .enableAutoReconnect()
                .enableCaptionManagement()
                .enableDebug()
                .enableLockScreen()
                .enableWifiReconnection()
//                .enableNotification()
//                .addNotificationAction(CastConfiguration.NOTIFICATION_ACTION_PLAY_PAUSE, true)
//                .addNotificationAction(CastConfiguration.NOTIFICATION_ACTION_DISCONNECT, true)
                .build();

        VideoCastManager.initialize(this, options);

    }

    private void setupCastListener() {

        mCastConsumer = new VideoCastConsumerImpl() {

            @Override
            public void onApplicationConnected(ApplicationMetadata appMetadata, String sessionId, boolean wasLaunched) {
                super.onApplicationConnected(appMetadata, sessionId, wasLaunched);

                System.out.println("onApplicationConnected");
//                mVideoCastManager.startVideoCastControllerActivity(OriginalShowActivity.this, mSelectedMedia,0,true);

                try {
                    System.out.println(mSelectedMedia.toString());
                    mVideoCastManager.loadMedia(mSelectedMedia,true,0);
                    mVideoCastManager.play();
//                    mVideoCastManager.startVideoCastControllerActivity(OriginalShowActivity.this, mSelectedMedia, 0, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApplicationDisconnected(int errorCode) {
                System.out.println("onApplicationDisconnected");
                super.onApplicationDisconnected(errorCode);
            }

            @Override
            public void onDisconnected() {
                System.out.println("onDisconnected");
                super.onDisconnected();
            }

            @Override
            public void onRemoteMediaPlayerMetadataUpdated() {
                super.onRemoteMediaPlayerMetadataUpdated();
            }

            @Override
            public void onFailed(int resourceId, int statusCode) {
                System.out.println("onFailed");
                super.onFailed(resourceId, statusCode);
            }

            @Override
            public void onConnectionSuspended(int cause) {
                System.out.println("onConnectionSuspended");
                super.onConnectionSuspended(cause);
            }

            @Override
            public void onConnectivityRecovered() {
                System.out.println("onConnectivityRecovered");
                super.onConnectivityRecovered();
            }

            @Override
            public void onCastAvailabilityChanged(boolean castPresent) {
                mMediaRouteButton.setVisibility(castPresent ? View.VISIBLE : View.INVISIBLE);
            }
        };
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
        int indexOf = video_path.lastIndexOf("/") + 1;
        return video_path.substring(indexOf, index);
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
