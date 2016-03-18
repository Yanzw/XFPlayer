package uk.me.feixie.xfplayer.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fei on 18/03/2016.
 */
public class LiveTVData implements Serializable{

    public ArrayList<LiveTV> data;

    @Override
    public String toString() {
        return "LiveTVData{" +
                "data=" + data +
                '}';
    }

    public class LiveTV implements Serializable{
        public String id;
        public String title;
        public String tvPath;

        @Override
        public String toString() {
            return "LiveTV{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", tvPath='" + tvPath + '\'' +
                    '}';
        }
    }

}
