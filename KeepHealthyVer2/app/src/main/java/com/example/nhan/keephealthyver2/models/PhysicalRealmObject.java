package com.example.nhan.keephealthyver2.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nhan on 10/15/2016.
 */

public class PhysicalRealmObject extends RealmObject {
    @PrimaryKey private String name;
    private String id;
    private String color;
    private String imageGif;
    private String linkYoutube;

    public String getImageGif() {
        return imageGif;
    }

    public void setImageGif(String imageGif) {
        this.imageGif = imageGif;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLinkYoutube() {
        return linkYoutube;
    }

    public void setLinkYoutube(String linkYoutube) {
        this.linkYoutube = linkYoutube;
    }
}
