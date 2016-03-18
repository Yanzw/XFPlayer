package uk.me.feixie.xfplayer.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fei on 18/03/2016.
 */
public class LiveRadioData implements Serializable{

    public ArrayList<LiveRadio> data;

    @Override
    public String toString() {
        return "LiveRadioData{" +
                "data=" + data +
                '}';
    }

    public class LiveRadio implements Serializable{
        public String id;
        public String title;
        public String radioPath;

        @Override
        public String toString() {
            return "LiveRadio{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", radioPath='" + radioPath + '\'' +
                    '}';
        }
    }

}
