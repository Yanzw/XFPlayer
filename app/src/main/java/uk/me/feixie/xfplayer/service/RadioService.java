package uk.me.feixie.xfplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;
import uk.me.feixie.xfplayer.utils.GloableConstants;

public class RadioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    private MediaPlayer mMediaPlayer;
    private String mRadio_path;
    private Messenger mMessenger;

    public RadioService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mRadio_path = intent.getStringExtra("radio_path");
        String command = intent.getStringExtra("command");
        if (mMessenger==null) {
            mMessenger = intent.getParcelableExtra("messenger");
        }

        if (command.equalsIgnoreCase("play") && !TextUtils.isEmpty(mRadio_path)) {
            play(mRadio_path);
        }
        if (command.equalsIgnoreCase("pause")) {
            pause();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void play(String path) {

        if (mMediaPlayer!=null) {
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
        mMediaPlayer = new MediaPlayer(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.releaseDisplay();
        //mms can only be played in this direction
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepareAsync();
//            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        System.out.println("onPrepared");
        Message msg = Message.obtain();
        msg.what = GloableConstants.RADIO_READY;
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        System.out.println("onError");
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        System.out.println("onCompletion");

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        System.out.println(percent);
    }
}
