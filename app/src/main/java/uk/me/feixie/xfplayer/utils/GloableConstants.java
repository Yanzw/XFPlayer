package uk.me.feixie.xfplayer.utils;

/**
 * Created by Fei on 14/03/2016.
 */
public class GloableConstants {

    public static final String FRAGMENT_LOCAL_VIDEO = "localFragmentVideo";
    public static final String FRAGMENT_LOCAL_AUDIO = "localFragmentAudio";
    public static final String FRAGMENT_LOCAL_DIRECTORIES = "localFragmentDirectories";

    public static final String SERVER_PATH = "http://www.feixie.me.uk/xfplayer/";
    public static final String SERVER_JSON_PATH = "http://www.feixie.me.uk/xfplayer/xfplayer_server.json";
    public static final String LIVE_TV_JSON_PATH = "http://www.feixie.me.uk/xfplayer/xfplayer_live_tv.json";
    public static final String LIVE_RADIO_JSON_PATH = "http://www.feixie.me.uk/xfplayer/xfplayer_live_radio.json";

    public static final String[] AUTO_COMPLETE_SERVER = {"http://", "https://", "mms://", "rtsp://",
            "http://www.", "https://www."};

    public static final int RADIO_READY = 1000;
    public static final int RADIO_ERROR = 1001;

    public static final String MOVIE_CURRENT_POSITION = "movie_current_position";
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_WATCHED_DATE = "movie_watched_date";

    public static final String VIDEO_CURRENT_POSITION = "video_current_position";
    public static final String VIDEO_TITLE = "video_title";
    public static final String VIDEO_WATCHED_DATE = "video_watched_date";

    public static final String LIVE_TITLE = "live_title";
    public static final String LIVE_WATCHED_DATE = "live_watched_date";

    public static final String WATCHED_MOVIE = "watched_movie";
    public static final String WATCHED_VIDEO = "watched_video";
    public static final String WATCHED_LIVE = "watched_live";
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_MOVIE = 2;
    public static final int TYPE_AUDIO = 3;
    public static final int TYPE_RADIO = 4;
    public static final int TYPE_LIVE = 5;
}
