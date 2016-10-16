package com.example.nhan.keephealthyver2.constants;

import android.content.SharedPreferences;

import com.example.nhan.keephealthyver2.application.MyApplication;

/**
 * Created by Nhan on 10/15/2016.
 */

public class Constant {
    public static boolean isLoadedBreathExercise;
    public static boolean isLoadedPhysicalExercise;
    public static boolean isLoadedOfficeExercise;
    public static boolean voiceInstructor;
    public static boolean isOnTimer;

    public static final String keyLoadedBreathExercise = "keyLoadedBreathExercise";
    public static final String keyLoadedPhysicalExercise = "keyLoadedPhysicalExercise";
    public static final String keyLoadedOfficeExercise = "keyLoadedOfficeExercise";
    public static final String keytimeRound = "keytimeRound";
    public static final String keytimeReminder = "keytimeReminder";
    public static final String keyVoice = "keyVoice";
    public static final String keyOnTimer = "keyOnTimer";
    public static String timeReminder;

    public static int timeRound;

    public static final String MUSIC_NAME_PREF = "musicNamePref";
    public static final String MUSIC_PHYSICAL = "8_bit_mayhem.mp3";
    public static final String MUSIC_BREATH = "nhacthiennhien.mp3";
    public static final String MUSIC_HOME = "RightHereWaitingForYou.mp3";
    public static final String MUSIC_OFICE = "Payphone.mp3";
}
