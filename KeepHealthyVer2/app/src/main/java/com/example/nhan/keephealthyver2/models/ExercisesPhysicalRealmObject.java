package com.example.nhan.keephealthyver2.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nhan on 10/15/2016.
 */

public class ExercisesPhysicalRealmObject extends RealmObject {
    @PrimaryKey private String name;
    private String linkImage;
    private RealmList<PhysicalRealmObject> listPhysicalObject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public RealmList<PhysicalRealmObject> getListPhysicalObject() {
        return listPhysicalObject;
    }

    public void setListPhysicalObject(RealmList<PhysicalRealmObject> listPhysicalObject) {
        this.listPhysicalObject = listPhysicalObject;
    }
}
