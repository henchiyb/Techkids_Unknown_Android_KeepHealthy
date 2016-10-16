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
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nhan.keephealthyver2.R;
import com.example.nhan.keephealthyver2.constants.Constant;
import com.example.nhan.keephealthyver2.events.EventSendBreathObject;
import com.example.nhan.keephealthyver2.models.BreathRealmObject;
import com.example.nhan.keephealthyver2.utils.Utils;
import com.squareup.picasso.Picasso;
import com.triggertrap.seekarc.SeekArc;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nhan on 10/14/2016.
 */

public class FragmentDoingBreathExcercise extends Fragment {

    private int round = Constant.timeRound;
    @BindView(R.id.progress_breath)SeekArc breathProgress;
    @BindView(R.id.btn_play_progress_breath)ImageButton btnPlayBreath;
    @BindView(R.id.tv_name_session)TextView tvNameSession;
    @BindView(R.id.tv_nums_round)TextView tvNumsRound;
    @BindView(R.id.tv_information)TextView tvInformation;
    @BindView(R.id.tv_time_count_breath)TextView tvCountTime;
    @BindView(R.id.breath_image_view_do_exercise)CircleImageView imageViewBreath;
    @BindView(R.id.bt_youtube_breath)ImageButton imageButton;

    @OnClick(R.id.bt_youtube_breath)
    public void onClickPhysical(){
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
        youtubeIntent.setData(Uri.parse(breathObject.getLinkYoutube()));
        startActivity(youtubeIntent);
    }
    private Boolean isPlay = false;
    private CountDownTimer timer;
    private int time;
    private int countRound;
    private int countSpeak;
    private TextToSpeech textToSpeech;
    private BreathRealmObject breathObject;

    private ScaleAnimation ZoomOut;
    private ScaleAnimation zoomIn;

    private MediaPlayer mediaPlayer;

    @OnClick(R.id.btn_play_progress_breath)
    public void onClick(){
        if(isPlay){
            isPlay = false;
            btnPlayBreath.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            EventBus.getDefault().post("CANCEL");
        } else {
            isPlay = true;
            btnPlayBreath.setImageResource(R.drawable.ic_loop_black_24dp);
            EventBus.getDefault().post("PLAY");
        }
    }

    @Subscribe(sticky = true)
    public void receiveBreathExercise(EventSendBreathObject event){
        this.breathObject = event.getBreathRealmObject();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_do_breath_exercise, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        tvNameSession.setText(breathObject.getName());

        imageViewBreath.setImageResource(R.drawable.progress_breath);
        breathProgress.setProgressWidth(20);
        countRound = 1;
        tvNumsRound.setText("Round " + countRound + "/" + round);

        time = breathObject.getTime();
        ZoomOut =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ZoomOut.setDuration(time * 1000);
        ZoomOut.setFillAfter(true);

        zoomIn =  new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        zoomIn.setDuration(time * 1000);
        zoomIn.setFillAfter(true);
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
        builder.setMessage(breathObject.getInfo());
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaPlayer = new MediaPlayer();
        Utils.setDataSourceForMediaPlayer(this.getContext(), mediaPlayer, Constant.MUSIC_BREATH);
        mediaPlayer.start();
        timer = new CountDownTimer(time * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                int timeCount = (int) (100 - millisUntilFinished / (time * 10));
                breathProgress.setProgress(timeCount);
                tvCountTime.setText(String.valueOf(time - millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                timer.cancel();
                if (countSpeak < breathObject.getListStringGuide().size() - 1) {
                    countSpeak++;
                    if(countSpeak == (breathObject.getListStringGuide().size() - 1)){
                        imageViewBreath.startAnimation(zoomIn);
                    }
                    tvInformation.setText(breathObject.getListStringGuide().get(countSpeak).getString());
                    textToSpeech.speak(tvInformation.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    countSpeak = 0;
                    countRound++;
                    if (countRound <= round) {
                        tvNumsRound.setText("Round " + countRound + "/" + round);
                        imageViewBreath.startAnimation(ZoomOut);
                        tvInformation.setText(breathObject.getListStringGuide().get(countSpeak).getString());
                        textToSpeech.speak(tvInformation.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        tvInformation.setText(R.string.finished_exercise);
                        textToSpeech.speak(tvInformation.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                if (countRound <= round)
                    timer.start();
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
            countRound = 1;
            countSpeak = 0;
            imageViewBreath.startAnimation(ZoomOut);
            tvNumsRound.setText("Round " + countRound + "/" + round);
            tvInformation.setText(breathObject.getListStringGuide().get(countSpeak).getString());
            textToSpeech.speak(tvInformation.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            timer.start();
        } else if ( event.equals("CANCEL")){
            Log.d("test", "CANCEL");
            imageViewBreath.clearAnimation();
            countRound = 0;
            timer.cancel();
            textToSpeech.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.release();
        if (timer != null)
            timer.cancel();
        textToSpeech.stop();
    }
}
