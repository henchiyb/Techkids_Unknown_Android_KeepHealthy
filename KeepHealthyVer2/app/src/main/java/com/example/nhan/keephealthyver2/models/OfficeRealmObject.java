package com.example.nhan.keephealthyver2.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nhan on 10/15/2016.
 */

public class OfficeRealmObject extends RealmObject {
    @PrimaryKey private String name;
    private String image;
    private int time;
    private String info;
    private String imageGif;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImageGif() {
        return imageGif;
    }

    public void setImageGif(String imageGif) {
        this.imageGif = imageGif;
    }
}
