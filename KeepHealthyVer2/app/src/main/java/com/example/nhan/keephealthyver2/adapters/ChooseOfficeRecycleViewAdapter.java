package com.example.nhan.keephealthyver2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nhan.keephealthyver2.R;
import com.example.nhan.keephealthyver2.adapters.viewholders.ChooseExerciseViewHolder;
import com.example.nhan.keephealthyver2.database.RealmHandler;
import com.example.nhan.keephealthyver2.models.BreathRealmObject;
import com.example.nhan.keephealthyver2.models.OfficeRealmObject;

import java.util.List;

/**
 * Created by Nhan on 10/15/2016.
 */

public class ChooseOfficeRecycleViewAdapter extends RecyclerView.Adapter<ChooseExerciseViewHolder> {
    List<OfficeRealmObject> objectList = RealmHandler.getInstance().getListOfficeObjectFromRealm();
    private View.OnClickListener onItemClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public ChooseExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_recyle_view_choose_exercise, parent, false);
        return new ChooseExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChooseExerciseViewHolder holder, int position) {
        holder.itemView.setOnClickListener(onItemClickListener);
        holder.setDataOffice(objectList.get(position));

    }
    @Override
    public int getItemCount() {
        return objectList.size();
    }
}
