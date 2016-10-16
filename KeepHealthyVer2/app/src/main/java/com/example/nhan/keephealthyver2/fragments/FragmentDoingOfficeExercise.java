package com.example.nhan.keephealthyver2.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nhan.keephealthyver2.R;
import com.example.nhan.keephealthyver2.constants.Constant;
import com.example.nhan.keephealthyver2.events.EventSendBreathObject;
import com.example.nhan.keephealthyver2.events.EventSendOfficeObject;
import com.example.nhan.keephealthyver2.models.OfficeRealmObject;
import com.example.nhan.keephealthyver2.utils.Utils;
import com.triggertrap.seekarc.SeekArc;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Nhan on 10/15/2016.
 */

public class FragmentDoingOfficeExercise extends Fragment {
    @BindView(R.id.progress_office)SeekArc officeProgress;
    @BindView(R.id.btn_play_progress_office)ImageButton btnPlayOffice;
    @BindView(R.id.tv_name_session_office)TextView tvNameSession;
    @BindView(R.id.tv_nums_round_office)TextView tvNumsRound;
    @BindView(R.id.tv_information_office)TextView tvInformation;
    @BindView(R.id.tv_time_count_office)TextView tvCountTime;
    @BindView(R.id.office_image_view_do_exercise)GifImageView imageViewOffice;
    @BindView(R.id.bt_youtube_office)ImageButton imageButton;

    private Boolean isPlay = false;
    private CountDownTimer timer;
    private int time;
    private TextToSpeech textToSpeech;
    private OfficeRealmObject officeObject;

    private MediaPlayer mediaPlayer;

    @OnClick(R.id.btn_play_progress_office)
    public void onClick(){
        if(isPlay){
            isPlay = false;
            btnPlayOffice.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            EventBus.getDefault().post("CANCEL");
        } else {
            isPlay = true;
            btnPlayOffice.setImageResource(R.drawable.ic_loop_black_24dp);
            EventBus.getDefault().post("PLAY");
        }
    }

    @Subscribe(sticky = true)
    public void receiveOfficeExercise(EventSendOfficeObject event){
        this.officeObject = event.getOfficeRealmObject();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_do_office_exercise, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        tvNameSession.setText(officeObject.getName());
        imageButton.setVisibility(View.GONE);
        imageViewOffice.setImageResource(getActivity().getResources().getIdentifier(officeObject.getImageGif(), "drawable",
                getContext().getPackageName()));
        imageViewOffice.setVisibility(View.INVISIBLE);
        officeProgress.setProgressWidth(20);

        tvNumsRound.setVisibility(View.VISIBLE);

        time = officeObject.getTime();
        textToSpeech = Utils.textToSpeech(getActivity().getApplicationContext());

        if(!Constant.voiceInstructor){
            textToSpeech.shutdown();
        }

        createDialog();
        return view;
    }

    private void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Guide");
        builder.setMessage(officeObject.getInfo());
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaPlayer = new MediaPlayer();
        Utils.setDataSourceForMediaPlayer(this.getContext(), mediaPlayer, Constant.MUSIC_OFICE);
        mediaPlayer.start();
        timer = new CountDownTimer(time * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                int timeCount = (int) (100 - millisUntilFinished / (time * 10));
                officeProgress.setProgress(timeCount);
                tvCountTime.setText(String.valueOf(time - millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                timer.cancel();
                tvInformation.setText(R.string.finished_exercise);
                textToSpeech.speak(tvInformation.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null)
            timer.cancel();
        textToSpeech.stop();
        textToSpeech.shutdown();
    }


    @Subscribe
    public void createTimerEnd(String event) {
        if (event.equals("PLAY")){
            Log.d("test", "PLAY");
            imageViewOffice.setVisibility(View.VISIBLE);
            textToSpeech.speak(tvNameSession.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            timer.start();
        } else if ( event.equals("CANCEL")){
            Log.d("test", "CANCEL");
            imageViewOffice.setVisibility(View.INVISIBLE);
            timer.cancel();
            textToSpeech.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
}
