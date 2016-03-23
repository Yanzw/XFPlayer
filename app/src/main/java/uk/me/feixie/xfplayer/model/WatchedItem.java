package uk.me.feixie.xfplayer.model;

/**
 * Created by Fei on 21/03/2016.
 */
public class WatchedItem {

    private long currentTime;
    private long duration;
    private String title;
    private String watchedDate;
    private int type;
    private String path;


    public WatchedItem(long currentTime,long duration, String title, String watchedDate, int type, String path) {
        this.currentTime = currentTime;
        this.duration = duration;
        this.title = title;
        this.watchedDate = watchedDate;
        this.type = type;
        this.path = path;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWatchedDate() {
        return watchedDate;
    }

    public void setWatchedDate(String watchedDate) {
        this.watchedDate = watchedDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "WatchedItem{" +
                "currentTime=" + currentTime +
                ", duration=" + duration +
                ", title='" + title + '\'' +
                ", watchedDate='" + watchedDate + '\'' +
                ", type=" + type +
                ", path='" + path + '\'' +
                '}';
    }
}
