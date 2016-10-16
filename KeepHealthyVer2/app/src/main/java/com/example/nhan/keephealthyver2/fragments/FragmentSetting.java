package com.example.nhan.keephealthyver2.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.nhan.keephealthyver2.R;
import com.example.nhan.keephealthyver2.constants.Constant;
import com.example.nhan.keephealthyver2.utils.NewAlarmReceiver;
import com.example.nhan.keephealthyver2.utils.Utils;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nhan on 10/14/2016.
 */

public class FragmentSetting extends Fragment implements View.OnClickListener {
    private ToggleButton tgVoice, tgMusic, tgTimer;
    private TextView tvRound, tvTimeReminder, tvRepeat;
    private ImageView ivMinusRound, ivPlusRound;
    private RelativeLayout viewTimeReminder, viewRepeat;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private MediaPlayer mediaPlayer;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initLayout(view);
        initData();
        addListeners();
        return view;
    }

    private void initLayout(View view) {
        tgVoice = (ToggleButton) view.findViewById(R.id.toggle_voice);
        tgMusic = (ToggleButton) view.findViewById(R.id.toggle_music);
        tvRound = (TextView) view.findViewById(R.id.tv_time_round);
        tvTimeReminder = (TextView) view.findViewById(R.id.tv_reminder);
        tvRepeat = (TextView) view.findViewById(R.id.tv_repeat);
        ivMinusRound = (ImageView) view.findViewById(R.id.iv_minus_round);
        ivPlusRound = (ImageView) view.findViewById(R.id.iv_plus_round);
        tgTimer = (ToggleButton) view.findViewById(R.id.toggle_timer);
        viewTimeReminder = (RelativeLayout) view.findViewById(R.id.view_time_reminder);
        viewRepeat = (RelativeLayout) view.findViewById(R.id.view_repeat);
    }

    private void initData() {
        sharedPreferences = getActivity().getSharedPreferences("SP", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Utils.loadDataSetting(getContext());

        tgVoice.setChecked(Constant.voiceInstructor);
        tvRound.setText(Constant.timeRound + "");
        tvTimeReminder.setText(Constant.timeReminder);
        tgTimer.setChecked(Constant.isOnTimer);
        if (Constant.isOnTimer) {
            viewTimeReminder.setVisibility(View.VISIBLE);
            viewRepeat.setVisibility(View.VISIBLE);
        } else {
            viewTimeReminder.setVisibility(View.INVISIBLE);
            viewRepeat.setVisibility(View.INVISIBLE);
        }
    }

    private void addListeners() {
        tgVoice.setOnClickListener(this);
        tgMusic.setOnClickListener(this);
        ivPlusRound.setOnClickListener(this);
        ivMinusRound.setOnClickListener(this);
        tvTimeReminder.setOnClickListener(this);
        tgTimer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle_voice: {
                editor.putBoolean(Constant.keyVoice, tgVoice.isChecked());
                Log.d("abcd", "tgvoice" + tgVoice.isChecked());
                editor.commit();
                break;
            }
            case R.id.toggle_music: {
                if(tgMusic.isChecked()){
                    Utils.saveIntToPreference(getContext(), Constant.MUSIC_NAME_PREF, 0);
                    mediaPlayer.setVolume(1.0f, 1.0f);
                } else {
                    Utils.saveIntToPreference(getContext(), Constant.MUSIC_NAME_PREF, 1);
                    mediaPlayer.setVolume(0, 0);
                }
                break;
            }
            case R.id.iv_minus_round: {
                if (Constant.timeRound > 1) {
                    Constant.timeRound--;
                }
                editor.putInt(Constant.keytimeRound, Constant.timeRound);
                editor.commit();
                tvRound.setText(Constant.timeRound + "");
                break;
            }
            case R.id.iv_plus_round: {
                if (Constant.timeRound < 10) {
                    Constant.timeRound++;
                }
                editor.putInt(Constant.keytimeRound, Constant.timeRound);
                editor.commit();
                tvRound.setText(Constant.timeRound + "");
                break;
            }
            case R.id.toggle_timer: {
                if (tgTimer.isChecked()) {
                    Constant.isOnTimer = true;
                    viewTimeReminder.setVisibility(View.VISIBLE);
                    viewRepeat.setVisibility(View.VISIBLE);
                    editor.putBoolean(Constant.keyOnTimer, Constant.isOnTimer);
                    editor.commit();
                } else {
                    Constant.isOnTimer = false;
                    viewTimeReminder.setVisibility(View.INVISIBLE);
                    viewRepeat.setVisibility(View.INVISIBLE);
                    editor.putBoolean(Constant.keyOnTimer, Constant.isOnTimer);
                    editor.commit();
                }
                break;
            }
            case R.id.tv_reminder: {
                int hour = 10;
                int minute = 10;
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tvTimeReminder.setText(Utils.getTimeString(i, i1));
                        editor.putString(Constant.keytimeReminder, tvTimeReminder.getText().toString());
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Set Time Reminder");
                timePickerDialog.show();
                break;
            }
            case R.id.tv_repeat: {

                break;
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mediaPlayer = new MediaPlayer();
        Utils.setDataSourceForMediaPlayer(this.getContext(), mediaPlayer, Constant.MUSIC_HOME);
        if (Utils.getIntFromPreference(getContext(), Constant.MUSIC_NAME_PREF) == 0){
            tgMusic.setChecked(true);
            mediaPlayer.setVolume(1.0f, 1.0f);
        } else {
            tgMusic.setChecked(false);
            mediaPlayer.setVolume(0, 0);
        }
        mediaPlayer.start();
        context = getActivity();
}

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.release();

        if (Constant.isOnTimer) {
            Calendar currentCalendar = new GregorianCalendar();
            currentCalendar.setTimeInMillis(System.currentTimeMillis());

            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.HOUR_OF_DAY, Utils.getHourFromTimeReminder(tvTimeReminder.getText().toString()));
            calendar.set(Calendar.MINUTE, Utils.getMinuteFromTimeReminder(tvTimeReminder.getText().toString()));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Intent myIntent = new Intent(context, NewAlarmReceiver.class);
            myIntent.putExtra("title", "Keep Heathy");
            myIntent.putExtra("detail", "Let's do exercise");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent);
            Toast.makeText(getActivity(), "Alarm has been set at " + tvTimeReminder.getText(), Toast.LENGTH_LONG).show();
        }
    }
}
