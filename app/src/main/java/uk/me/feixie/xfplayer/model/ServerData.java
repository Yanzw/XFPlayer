package uk.me.feixie.xfplayer.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fei on 16/03/2016.
 */
public class ServerData implements Serializable {

    public ArrayList<ServerMovie> movies;

    @Override
    public String toString() {
        return "ServerData{" +
                "movies=" + movies +
                '}';
    }

    public class ServerMovie implements Serializable {
        public String id;
        public String title;
        public String imagePath;
        public String videoPath;

        @Override
        public String toString() {
            return "ServerMovie{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", imagePath='" + imagePath + '\'' +
                    ", videoPath='" + videoPath + '\'' +
                    '}';
        }
    }

}
