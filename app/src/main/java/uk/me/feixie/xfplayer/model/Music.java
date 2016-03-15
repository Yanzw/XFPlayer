package uk.me.feixie.xfplayer.model;

import java.io.Serializable;

/**
 * Created by Fei on 12/03/2016.
 */
public class Music implements Serializable {

    private String id;
    private String title;
    private String duration;
    private String data;
    private String artist;
    private String album;
    private String display_name;
    private String is_music;
    private String track;
    private String album_id;
    private String artist_id;
    private String albumPath;
    private String artistPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getIs_music() {
        return is_music;
    }

    public void setIs_music(String is_music) {
        this.is_music = is_music;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public String getArtistPath() {
        return artistPath;
    }

    public void setArtistPath(String artistPath) {
        this.artistPath = artistPath;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", display_name='" + display_name + '\'' +
                ", is_music='" + is_music + '\'' +
                ", track='" + track + '\'' +
                ", album_id='" + album_id + '\'' +
                ", artist_id='" + artist_id + '\'' +
                ", albumPath='" + albumPath + '\'' +
                ", artistPath='" + artistPath + '\'' +
                '}';
    }
}
