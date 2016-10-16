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
import com.example.nhan.keephealthyver2.adapters.ChooseOfficeRecycleViewAdapter;
import com.example.nhan.keephealthyver2.constants.Constant;
import com.example.nhan.keephealthyver2.database.RealmHandler;
import com.example.nhan.keephealthyver2.events.EventDataReady;
import com.example.nhan.keephealthyver2.events.EventSendOfficeObject;
import com.example.nhan.keephealthyver2.models.OfficeRealmObject;
import com.example.nhan.keephealthyver2.networks.ApiUrl;
import com.example.nhan.keephealthyver2.networks.ServiceFactory;
import com.example.nhan.keephealthyver2.networks.interfaces.GetOfficeExerciseFromAPI;
import com.example.nhan.keephealthyver2.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nhan on 10/15/2016.
 */

public class FragmentChooseOfficeExercise extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private ChooseOfficeRecycleViewAdapter adapter;
    private ServiceFactory serviceFactory;
    private OfficeRealmObject officeRealmObject;

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
        adapter = new ChooseOfficeRecycleViewAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void loadDataByRetrofit(){
        if (!Constant.isLoadedBreathExercise) {
            serviceFactory = new ServiceFactory(ApiUrl.BASE_URL);
            GetOfficeExerciseFromAPI service = serviceFactory.createService(GetOfficeExerciseFromAPI.class);
            Call<GetOfficeExerciseFromAPI.Office> call = service.callOfficeExercise();
            call.enqueue(new Callback<GetOfficeExerciseFromAPI.Office>() {
                @Override
                public void onResponse(Call<GetOfficeExerciseFromAPI.Office> call, Response<GetOfficeExerciseFromAPI.Office> response) {
                    RealmHandler.getInstance().clearDataOfficeInRealm();
                    List<GetOfficeExerciseFromAPI.ExerciseOffice> exerciseOfficeList = response.body().getExerciseOfficesList();
//
                    for (int i = 0; i < exerciseOfficeList.size(); i++) {
                        OfficeRealmObject officeRealmObject = new OfficeRealmObject();
                        officeRealmObject.setName(exerciseOfficeList.get(i).getName());
                        officeRealmObject.setImage(exerciseOfficeList.get(i).getImage());
                        officeRealmObject.setTime(exerciseOfficeList.get(i).getTime());
                        officeRealmObject.setInfo(exerciseOfficeList.get(i).getInfo());
                        officeRealmObject.setImageGif(exerciseOfficeList.get(i).getImageGif());
                        RealmHandler.getInstance().addOfficeObjectToRealm(officeRealmObject);
                    }
                    EventBus.getDefault().post(new EventDataReady());
                    Utils.setLoadData(getActivity(), Constant.keyLoadedOfficeExercise, true);

                }

                @Override
                public void onFailure(Call<GetOfficeExerciseFromAPI.Office> call, Throwable t) {

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
        Utils.setDataSourceForMediaPlayer(this.getContext(), mediaPlayer, Constant.MUSIC_OFICE);
        mediaPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        officeRealmObject = (OfficeRealmObject) v.getTag();
        EventBus.getDefault().postSticky( new EventSendOfficeObject(officeRealmObject));
        openFragment(new FragmentDoingOfficeExercise());
    }
    private void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

}
