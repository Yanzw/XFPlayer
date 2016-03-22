package uk.me.feixie.xfplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.io.IOException;

import uk.me.feixie.xfplayer.utils.GloableConstants;

public class MusicService extends Service implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private Messenger mMessenger;
    private int mCurrentPosition;

    public MusicService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String music_path = intent.getStringExtra("music_path");
//        System.out.println(mRadio_path);
        String command = intent.getStringExtra("command");
        if (mMessenger == null) {
            mMessenger = intent.getParcelableExtra("messenger");
        }

        if (command.equalsIgnoreCase("play")) {
            play(music_path);
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

    private void play(String music_path) {

        if (mMediaPlayer != null) {
            if (mCurrentPosition > 0) {
                System.out.println(mCurrentPosition);
                mMediaPlayer.seekTo(mCurrentPosition);
                mMediaPlayer.start();
            } else {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnErrorListener(this);
                mMediaPlayer.setOnCompletionListener(this);
                //play music
                try {
                    mMediaPlayer.setDataSource(music_path);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            //play music
            try {
                mMediaPlayer.setDataSource(music_path);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        System.out.println("onError");
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        System.out.println("onCompletion");
        Message msg = Message.obtain();
        msg.what = GloableConstants.MUSIC_COMPLETE;
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mCurrentPosition = 0;
    }
}
