package uk.me.feixie.xfplayer.fragment;


import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.model.Music;
import uk.me.feixie.xfplayer.utils.TimeUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalFragmentMusicBySong extends Fragment {

    private List<Music> mMusicList;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private int mDelay = 1000;

    public LocalFragmentMusicBySong() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_fragment_music_by_song, container, false);
        initData();
        initViews(view);
        return view;
    }

    private void initData() {
        mMusicList = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.TRACK, MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ARTIST_ID};
        Cursor cursor = new CursorLoader(getContext(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null).loadInBackground();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String is_music = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if (TextUtils.equals(is_music, "1")) {
                    Music music = new Music();
                    music.setIs_music(is_music);
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    music.setId(id);
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    music.setAlbum(album);
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    music.setArtist(artist);
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    music.setData(data);
                    String display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    music.setDisplay_name(display_name);
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    music.setDuration(duration);
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    music.setTitle(title);
                    String track = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK));
                    music.setTrack(track);
                    String album_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    music.setAlbum_id(album_id);
                    String artist_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                    music.setArtist_id(artist_id);

                    Cursor cursorAlbum = new CursorLoader(getContext(), MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID + "=?",
                            new String[]{music.getAlbum_id()},
                            null).loadInBackground();

                    if (cursorAlbum != null) {
                        if (cursorAlbum.moveToFirst()) {
                            String path = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                            music.setAlbumPath(path);
                        }
                        cursorAlbum.close();
                    }

//                    System.out.println(music.toString());
                    mMusicList.add(music);
                }
            }
            cursor.close();
        }
    }

    private void initViews(View view) {
        RecyclerView rvLocalMusicBySong = (RecyclerView) view.findViewById(R.id.rvLocalMusicBySong);
        rvLocalMusicBySong.setHasFixedSize(true);
        rvLocalMusicBySong.setLayoutManager(new LinearLayoutManager(getContext()));
        SongAdapter adapter = new SongAdapter();
        rvLocalMusicBySong.setAdapter(adapter);
    }


    /*--------------------Recycler View for Music by Songs---------------------*/
    public class SongViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llBySongs;
        public ImageView ivMusicIcon;
        public TextView tvMusicTitle;
        public TextView tvMusicArtist;

        public SongViewHolder(final View itemView) {
            super(itemView);

            ivMusicIcon = (ImageView) itemView.findViewById(R.id.ivMusicIcon);
            tvMusicTitle = (TextView) itemView.findViewById(R.id.tvMusicTitle);
            tvMusicArtist = (TextView) itemView.findViewById(R.id.tvMusicArtist);

            llBySongs = (LinearLayout) itemView.findViewById(R.id.llBySongs);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 15, 15, 10);
            llBySongs.setLayoutParams(params);
            llBySongs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Music music = mMusicList.get(getAdapterPosition());

                    if (mMediaPlayer!=null) {
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                    mMediaPlayer = new MediaPlayer();

                    // Create the Snackbar
                    Snackbar snackBar = Snackbar.make(itemView, "Music", Snackbar.LENGTH_INDEFINITE);
                    // Get the Snackbar's layout view
                    Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackBar.getView();
                    // Hide the text
                    TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setVisibility(View.INVISIBLE);
                    // Inflate our custom view
                    View snackView = View.inflate(getContext(),R.layout.snackbar_player,null);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    snackView.setLayoutParams(layoutParams);
                    // Configure the view
                    //set seekBar
                    final SeekBar seekBar = (SeekBar) snackView.findViewById(R.id.sbPlayer);
                    seekBar.setMax(Integer.parseInt(music.getDuration()));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(mDelay);
                            mDelay=+1000;
                        }
                    },1000);

                    TextView tvTitlePlayer = (TextView) snackView.findViewById(R.id.tvTitlePlayer);
                    tvTitlePlayer.setText(music.getDisplay_name());
                    tvTitlePlayer.setTextColor(Color.WHITE);

                    TextView tvArtistPlayer = (TextView) snackView.findViewById(R.id.tvArtistPlayer);
                    tvArtistPlayer.setText(music.getArtist());
                    tvArtistPlayer.setTextColor(Color.WHITE);

                    TextView tvTimePlayer = (TextView) snackView.findViewById(R.id.tvTimePlayer);
                    String duration = TimeUtil.millToString(Long.parseLong(music.getDuration()));
                    tvTimePlayer.setText(duration);
                    tvTimePlayer.setTextColor(Color.WHITE);

                    final ImageView ivPlayPause = (ImageView) snackView.findViewById(R.id.ivPlayPause);

                    LinearLayout llPlayerControl = (LinearLayout) snackView.findViewById(R.id.llPlayControl);
                    llPlayerControl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mMediaPlayer.isPlaying()) {
                                mMediaPlayer.pause();
                                ivPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                            } else {
                                mMediaPlayer.start();
                                ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
                            }
                        }
                    });

                    // Add the view to the Snackbar's layout
                    layout.addView(snackView, 0);
                    // Show the Snackbar
                    snackBar.show();

                    try {
                        mMediaPlayer.setDataSource(music.getData());
                        mMediaPlayer.prepare();

                        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                System.out.println("onError");
                                return true;
                            }
                        });
                        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
                            }
                        });
                        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                            @Override
                            public void onSeekComplete(MediaPlayer mp) {
                                System.out.println("onSeekComplete");
                            }
                        });
                        mMediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public class SongAdapter extends RecyclerView.Adapter<SongViewHolder> {

        @Override
        public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_rv_local_music_song, null);
            return new SongViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SongViewHolder holder, int position) {

            String path = "";
            Music music = mMusicList.get(position);


            ImageOptions imageOptions = new ImageOptions.Builder()
//                        .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
//                        .setRadius(DensityUtil.dip2px(5))
                    // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.mipmap.ic_launcher)
                    .setFailureDrawableId(R.mipmap.ic_launcher)
                    .build();

            x.image().bind(holder.ivMusicIcon, path, imageOptions);
            holder.tvMusicTitle.setText(music.getDisplay_name());
            holder.tvMusicArtist.setText(music.getArtist());
        }

        @Override
        public int getItemCount() {
            if (mMusicList != null) {
                return mMusicList.size();
            }
            return 0;
        }
    }

}
