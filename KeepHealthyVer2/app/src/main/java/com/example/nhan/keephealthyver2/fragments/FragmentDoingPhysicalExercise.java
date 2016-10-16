package com.example.nhan.keephealthyver2.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nhan.keephealthyver2.R;
import com.example.nhan.keephealthyver2.constants.Constant;
import com.example.nhan.keephealthyver2.events.EventSendPhysicalObject;
import com.example.nhan.keephealthyver2.models.ExercisesPhysicalRealmObject;
import com.example.nhan.keephealthyver2.models.PhysicalRealmObject;
import com.example.nhan.keephealthyver2.utils.Utils;
import com.triggertrap.seekarc.SeekArc;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Qk Lahpita on 10/15/2016.
 */
public class FragmentDoingPhysicalExercise extends Fragment implements View.OnClickListener {

    @BindView(R.id.progress_physical)
    SeekArc physicalProgress;
    @BindView(R.id.btn_play_progress_physical)
    ImageButton btnPlayPhysical;
    @BindView(R.id.tv_name_session)
    TextView tvNameSession;
    @BindView(R.id.tv_nums_round)
    TextView tvNumRound;
    @BindView(R.id.tv_name_detail)
    TextView tvDetails;
    @BindView(R.id.tv_time_count_physical)
    TextView tvCountTime;
    @BindView(R.id.iv_gif)
    GifImageView imageViewPhysic;
    @BindView(R.id.bt_youtube_physical)ImageButton imageButton;

    @OnClick(R.id.bt_youtube_physical)
    public void onClickPhysical(){
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
        youtubeIntent.setData(Uri.parse(exercisesPhysicalRealmObject
                .getListPhysicalObject()
                .get(currentIndexPhysicalRealmObject)
                .getLinkYoutube()));
        startActivity(youtubeIntent);
    }

    private Boolean isPlay = false;
    private ExercisesPhysicalRealmObject exercisesPhysicalRealmObject;
    private List<PhysicalRealmObject> physicalRealmObjectList;
    private PhysicalRealmObject physicalRealmObject;

    private CountDownTimer cdtDoExercise;
    private CountDownTimer cdtRest;
    private int timeDoExercise = 10;
    private int timeDoRest = 5;
    private boolean doneEx = false;

    private int currentCircuit;
    private int totalCircuit = Constant.timeRound;
    private boolean doneCircuit;

    private int currentIndexPhysicalRealmObject = 0;
    private TextToSpeech textToSpeech;

    private MediaPlayer mediaPlayer;

    @Subscribe(sticky = true)
    public void receiveBreathExercise(EventSendPhysicalObject event) {
        this.exercisesPhysicalRealmObject = event.getPhysicalRealmObject();
        physicalRealmObjectList = exercisesPhysicalRealmObject.getListPhysicalObject();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_do_physical_exercise, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        textToSpeech = Utils.textToSpeech(getActivity().getApplicationContext());

        tvNameSession.setText(exercisesPhysicalRealmObject.getName());
        if(!Constant.voiceInstructor){
            textToSpeech.shutdown();
        }

        btnPlayPhysical.setOnClickListener(this);
        imageViewPhysic.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaPlayer = new MediaPlayer();
        Utils.setDataSourceForMediaPlayer(this.getContext(), mediaPlayer, Constant.MUSIC_PHYSICAL);
        mediaPlayer.start();
        currentIndexPhysicalRealmObject = 0;
        currentCircuit = 1;
        tvNumRound.setText("Round: " + currentCircuit + "/" + totalCircuit);

        cdtDoExercise = new CountDownTimer(timeDoExercise * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                int timeCount = (int) (100 - millisUntilFinished / (timeDoExercise * 10));
                physicalProgress.setProgress(timeCount);
                tvCountTime.setText(String.valueOf(timeDoExercise - millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                cdtDoExercise.cancel();
                currentIndexPhysicalRealmObject++;
                if (currentIndexPhysicalRealmObject <= (physicalRealmObjectList.size()-1)) {
                    getClassicExerciseData();
                }
                if (doneCircuit) {
                    tvDetails.setText(R.string.finished_exercise);
                    Log.d("abcd", "currentCircuit" + currentCircuit);
                    Log.d("abcd", "currentEx" + currentIndexPhysicalRealmObject);
                    textToSpeech.speak(tvDetails.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    cdtRest.cancel();
                    cdtDoExercise.cancel();
                } else if (doneEx && !doneCircuit) {
                    currentCircuit++;
                    tvNumRound.setText("Round: " + currentCircuit + "/" + totalCircuit);
                    currentIndexPhysicalRealmObject = 0;
                    tvDetails.setText("Round: " + currentCircuit + "/" + totalCircuit);
                    doneEx = false;
                    getClassicExerciseData();
                    cdtRest.start();
                } else {
                    cdtRest.start();
                }
            }
        };

        cdtRest = new CountDownTimer(timeDoRest * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                int timeCount = (int) (100 - millisUntilFinished / (timeDoRest * 10));
                physicalProgress.setProgress(timeCount);
                tvCountTime.setText(String.valueOf(timeDoRest - millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                cdtRest.cancel();

                if (currentIndexPhysicalRealmObject == physicalRealmObjectList.size()-1) {
                    doneEx = true;
                }
                //done het toan bo cac vong
                if ((currentCircuit == totalCircuit) && (currentIndexPhysicalRealmObject == physicalRealmObjectList.size()-1)) {
                    doneCircuit = true;
                }
                cdtDoExercise.start();
            }
        };

        getClassicExerciseData();
    }

    public void getClassicExerciseData() {
        physicalRealmObject = physicalRealmObjectList.get(currentIndexPhysicalRealmObject);
        tvDetails.setText(physicalRealmObject.getName());
        textToSpeech.speak(tvDetails.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        imageViewPhysic.setImageResource(getActivity().getResources().getIdentifier(physicalRealmObject.getImageGif(), "drawable",
                        getContext().getPackageName()));
        Log.d("abcd", "id: " + getActivity().getResources().getIdentifier(physicalRealmObject.getImageGif(), "drawable",
                getContext().getPackageName()));
    }

    @Subscribe
    public void createTimerEnd(String event) {
        if (event.equals("PLAY")){
            Log.d("test", "PLAY");
            doneCircuit = false;
            doneEx = false;
            currentCircuit = 1;
            currentIndexPhysicalRealmObject = 0;
            getClassicExerciseData();
            imageViewPhysic.setVisibility(View.VISIBLE);
            tvNumRound.setText("Round " + currentCircuit + "/" + totalCircuit);
            tvDetails.setText(physicalRealmObject.getName());
            textToSpeech.speak(tvDetails.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            cdtRest.start();
        } else if ( event.equals("CANCEL")){
            Log.d("test", "CANCEL");
            imageViewPhysic.setVisibility(View.INVISIBLE);
            textToSpeech.stop();
            if (cdtRest != null)
                cdtRest.cancel();
            if (cdtDoExercise != null)
                cdtDoExercise.cancel();

        }
    }

    @Override
    public void onClick(View view) {
        if(isPlay){
            isPlay = false;
            btnPlayPhysical.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            EventBus.getDefault().post("CANCEL");
        } else {
            isPlay = true;
            btnPlayPhysical.setImageResource(R.drawable.ic_loop_black_24dp);
            EventBus.getDefault().post("PLAY");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (cdtRest != null)
            cdtRest.cancel();
        if (cdtDoExercise != null)
            cdtDoExercise.cancel();
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
}
