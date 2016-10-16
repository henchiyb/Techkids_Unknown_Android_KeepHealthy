package com.example.nhan.keephealthyver2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;

import com.example.nhan.keephealthyver2.constants.Constant;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nhan on 10/12/2016.
 */

public class Utils {

    public static String TIME_FORMAT = "HH:mm";
    public static String TIME_FORMAT_STRING = "%02d:%02d";

    private static TextToSpeech t1;

    public static TextToSpeech textToSpeech(Context context) {
        t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    t1.setSpeechRate(1.2f);
                }
            }
        });
        return t1;
    }

    public static void setLoadData(Context context, String key, boolean isLoaded) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SP", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, isLoaded);
        editor.commit();
    }

    public static void setDataSourceForMediaPlayer(Context context, MediaPlayer mediaPlayer, String fileName) {
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd("sounds/" + fileName);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Utils.getIntFromPreference(context, Constant.MUSIC_NAME_PREF) == 0) {
            mediaPlayer.setVolume(1.0f, 1.0f);
        } else {
            mediaPlayer.setVolume(0, 0);
        }
    }

    public static void saveIntToPreference(Context context, String namePref, int value) {
        SharedPreferences preferences = context.getSharedPreferences(namePref, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(namePref, value);
        editor.apply();
    }

    public static void saveBooleanToPreference(Context context, String namePref, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(namePref, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(namePref, value);
        editor.apply();
    }

    public static int getIntFromPreference(Context context, String namePref) {
        SharedPreferences preferences = context.getSharedPreferences(namePref, MODE_PRIVATE);
        return preferences.getInt(namePref, 0);
    }

    public static String getTimeFromSystem() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        return String.format(TIME_FORMAT_STRING, hour, minute);
    }

    public static String getTimeString(int hour, int minute) {
        return String.format(TIME_FORMAT_STRING, hour, minute);
    }

    public static void loadDataSetting(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SP", MODE_PRIVATE);
        Constant.isLoadedBreathExercise = sharedPreferences.getBoolean(Constant.keyLoadedBreathExercise, false);
        Constant.isLoadedPhysicalExercise = sharedPreferences.getBoolean(Constant.keyLoadedPhysicalExercise, false);
        Constant.isLoadedOfficeExercise = sharedPreferences.getBoolean(Constant.keyLoadedOfficeExercise, false);
        Constant.timeRound = sharedPreferences.getInt(Constant.keytimeRound, 1);
        Constant.timeReminder = sharedPreferences.getString(Constant.keytimeReminder, Utils.getTimeFromSystem());
        Constant.voiceInstructor = sharedPreferences.getBoolean(Constant.keyVoice, true);
        Constant.isOnTimer = sharedPreferences.getBoolean(Constant.keyOnTimer, false);
    }

    public static int getHourFromTimeReminder(String string) {
        String strArrtmp[] = string.split(":");
        return Integer.parseInt(strArrtmp[0]);
    }

    public static int getMinuteFromTimeReminder(String string) {
        String strArrtmp[] = string.split(":");
        return Integer.parseInt(strArrtmp[1]);
    }
}
