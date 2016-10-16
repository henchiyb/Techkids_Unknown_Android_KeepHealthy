package com.example.nhan.keephealthyver2.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.nhan.keephealthyver2.R;
import com.example.nhan.keephealthyver2.adapters.ChooseBreathRecycleViewAdapter;
import com.example.nhan.keephealthyver2.constants.Constant;
import com.example.nhan.keephealthyver2.database.RealmHandler;
import com.example.nhan.keephealthyver2.events.EventDataReady;
import com.example.nhan.keephealthyver2.events.EventSendBreathObject;
import com.example.nhan.keephealthyver2.models.BreathRealmObject;
import com.example.nhan.keephealthyver2.models.StringRealmObject;
import com.example.nhan.keephealthyver2.networks.ApiUrl;
import com.example.nhan.keephealthyver2.networks.interfaces.GetBreathExerciseFromAPI;
import com.example.nhan.keephealthyver2.networks.ServiceFactory;
import com.example.nhan.keephealthyver2.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nhan on 10/14/2016.
 */

public class FragmentChooseBreathExercise extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private ChooseBreathRecycleViewAdapter adapter;
    private ServiceFactory serviceFactory;
    private BreathRealmObject breathObject;

    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_choose_exercise, container, false);

        EventBus.getDefault().register(this);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view_breath);
        layoutManager = new GridLayoutManager(view.getContext(),
                2,
                GridLayout.VERTICAL,
                false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        Log.d("Response", "response");
        loadDataByRetrofit();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void setAdapterView(EventDataReady event){
        adapter = new ChooseBreathRecycleViewAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void loadDataByRetrofit(){
        if (!Constant.isLoadedBreathExercise) {
            serviceFactory = new ServiceFactory(ApiUrl.BASE_URL);
            GetBreathExerciseFromAPI service = serviceFactory.createService(GetBreathExerciseFromAPI.class);
            Call<GetBreathExerciseFromAPI.Breath> call = service.callBreathExercise();
            call.enqueue(new Callback<GetBreathExerciseFromAPI.Breath>() {
                @Override
                public void onResponse(Call<GetBreathExerciseFromAPI.Breath> call, Response<GetBreathExerciseFromAPI.Breath> response) {
                    RealmHandler.getInstance().clearDataBreathInRealm();
                    List<GetBreathExerciseFromAPI.ExerciseBreath> exerciseBreathList = response.body().getExerciseBreathList();

                    for (int i = 0; i < exerciseBreathList.size(); i++){
                        BreathRealmObject breathRealmObject = new BreathRealmObject();
                        breathRealmObject.setName(exerciseBreathList.get(i).getName());
                        breathRealmObject.setId(exerciseBreathList.get(i).getId());
                        breathRealmObject.setColor(exerciseBreathList.get(i).getColor());
                        breathRealmObject.setImage(exerciseBreathList.get(i).getImage());
                        breathRealmObject.setTime(exerciseBreathList.get(i).getTime());
                        breathRealmObject.setInfo(exerciseBreathList.get(i).getInfo());
                        breathRealmObject.setLinkYoutube(exerciseBreathList.get(i).getLinkYoutube());
                        List<GetBreathExerciseFromAPI.Label> listGuideString = exerciseBreathList.get(i).getListGuide();
                        RealmList<StringRealmObject> list = new RealmList<>();
                        for (int j = 0; j < listGuideString.size(); j ++){
                            StringRealmObject stringRealmObject = new StringRealmObject(listGuideString.get(j).getLabel());
                            list.add(stringRealmObject);
                        }
                        breathRealmObject.setListStringGuide(list);
                        RealmHandler.getInstance().addBreathObjectToRealm(breathRealmObject);
                    }
                    EventBus.getDefault().post(new EventDataReady());
                    Utils.setLoadData(getActivity(), Constant.keyLoadedBreathExercise, true);
                }

                @Override
                public void onFailure(Call<GetBreathExerciseFromAPI.Breath> call, Throwable t) {
                    Log.d("Failure", t.toString());
                }
            });

        } else {
            EventBus.getDefault().post(new EventDataReady());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaPlayer = new MediaPlayer();
        Utils.setDataSourceForMediaPlayer(this.getContext(), mediaPlayer, Constant.MUSIC_BREATH);
        mediaPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        breathObject = (BreathRealmObject) v.getTag();
        EventBus.getDefault().postSticky( new EventSendBreathObject(breathObject));
        openFragment(new FragmentDoingBreathExcercise());
    }
    private void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

}
