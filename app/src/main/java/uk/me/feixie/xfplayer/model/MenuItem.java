package uk.me.feixie.xfplayer.model;

import java.io.Serializable;

/**
 * Created by Fei on 12/03/2016.
 */
public class MenuItem implements Serializable {

    private int imageId;
    private String name;

    public MenuItem() {
    }

    public MenuItem(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "imageId=" + imageId +
                ", name='" + name + '\'' +
                '}';
    }
}
