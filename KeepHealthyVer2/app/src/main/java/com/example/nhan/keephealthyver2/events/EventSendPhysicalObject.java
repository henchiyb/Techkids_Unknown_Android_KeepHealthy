package com.example.nhan.keephealthyver2.events;

import com.example.nhan.keephealthyver2.models.BreathRealmObject;
import com.example.nhan.keephealthyver2.models.ExercisesPhysicalRealmObject;
import com.example.nhan.keephealthyver2.models.PhysicalRealmObject;

/**
 * Created by Nhan on 10/14/2016.
 */

public class EventSendPhysicalObject {
    private ExercisesPhysicalRealmObject exercisesPhysicalRealmObject;

    public EventSendPhysicalObject(ExercisesPhysicalRealmObject exercisesPhysicalRealmObject) {
        this.exercisesPhysicalRealmObject = exercisesPhysicalRealmObject;
    }

    public ExercisesPhysicalRealmObject getPhysicalRealmObject() {
        return exercisesPhysicalRealmObject;
    }
}
