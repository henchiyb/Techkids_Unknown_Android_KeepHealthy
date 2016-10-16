package com.example.nhan.keephealthyver2.events;

import com.example.nhan.keephealthyver2.models.BreathRealmObject;

/**
 * Created by Nhan on 10/14/2016.
 */

public class EventSendBreathObject {
    private BreathRealmObject breathRealmObject;

    public EventSendBreathObject(BreathRealmObject breathRealmObject) {
        this.breathRealmObject = breathRealmObject;
    }

    public BreathRealmObject getBreathRealmObject() {
        return breathRealmObject;
    }
}
