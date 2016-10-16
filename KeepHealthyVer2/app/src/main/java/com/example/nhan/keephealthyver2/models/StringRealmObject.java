package com.example.nhan.keephealthyver2.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nhan on 10/14/2016.
 */

public class StringRealmObject extends RealmObject{
    private String string;

    public StringRealmObject(String string) {
        this.string = string;
    }
    public StringRealmObject() {
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
