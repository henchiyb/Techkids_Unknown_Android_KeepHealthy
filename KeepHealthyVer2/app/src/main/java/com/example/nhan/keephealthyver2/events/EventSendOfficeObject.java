package com.example.nhan.keephealthyver2.events;

import com.example.nhan.keephealthyver2.models.OfficeRealmObject;

/**
 * Created by Nhan on 10/15/2016.
 */

public class EventSendOfficeObject {
    private OfficeRealmObject officeRealmObject;

    public EventSendOfficeObject(OfficeRealmObject officeRealmObject) {
        this.officeRealmObject = officeRealmObject;
    }

    public OfficeRealmObject getOfficeRealmObject() {
        return officeRealmObject;
    }
}
