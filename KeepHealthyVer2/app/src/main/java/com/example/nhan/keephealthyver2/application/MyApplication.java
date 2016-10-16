package com.example.nhan.keephealthyver2.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;

import com.example.nhan.keephealthyver2.constants.Constant;
import com.example.nhan.keephealthyver2.utils.Utils;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Nhan on 10/14/2016.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getApplicationContext())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Utils.loadDataSetting(getApplicationContext());
    }
}
