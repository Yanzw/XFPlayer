package uk.me.feixie.xfplayer.model;

/**
 * Created by Fei on 21/03/2016.
 */
public class WatchedItem {

    private long currentTime;
    private String title;
    private String watchedDate;
    private int type;
    private String path;


    public WatchedItem(long currentTime, String title, String watchedDate, int type, String path) {
        this.currentTime = currentTime;
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

    @Override
    public String toString() {
        return "WatchedItem{" +
                "currentTime=" + currentTime +
                ", title='" + title + '\'' +
                ", watchedDate='" + watchedDate + '\'' +
                ", type=" + type +
                ", path='" + path + '\'' +
                '}';
    }
}
