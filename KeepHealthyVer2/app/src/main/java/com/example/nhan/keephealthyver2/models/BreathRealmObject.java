package com.example.nhan.keephealthyver2.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nhan on 10/14/2016.
 */

public class BreathRealmObject extends RealmObject {
    @PrimaryKey private String name;
    private String id;
    private String color;
    private String image;
    private String linkYoutube;
    private int time;
    private RealmList<StringRealmObject> listStringGuide;
    private Boolean favorite;
    private String info;

    public String getLinkYoutube() {
        return linkYoutube;
    }

    public void setLinkYoutube(String linkYoutube) {
        this.linkYoutube = linkYoutube;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
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

    public RealmList<StringRealmObject> getListStringGuide() {
        return listStringGuide;
    }

    public void setListStringGuide(RealmList<StringRealmObject> listStringGuide) {
        this.listStringGuide = listStringGuide;
    }
}
