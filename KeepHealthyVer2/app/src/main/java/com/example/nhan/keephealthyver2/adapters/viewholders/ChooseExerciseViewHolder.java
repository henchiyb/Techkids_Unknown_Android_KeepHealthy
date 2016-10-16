package com.example.nhan.keephealthyver2.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhan.keephealthyver2.models.BreathRealmObject;
import com.example.nhan.keephealthyver2.R;
import com.example.nhan.keephealthyver2.models.ExercisesPhysicalRealmObject;
import com.example.nhan.keephealthyver2.models.OfficeRealmObject;
import com.example.nhan.keephealthyver2.models.PhysicalRealmObject;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nhan on 10/14/2016.
 */

public class ChooseExerciseViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image_exercise_breath)ImageView imageView;
    @BindView(R.id.exercise_name_breath)TextView tvName;
    public ChooseExerciseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setDataBreath(BreathRealmObject breath){
        Picasso.with(itemView.getContext())
                .load(breath.getImage())
                .fit()
                .centerCrop()
                .into(imageView);
        tvName.setText("Office Exercise: " + breath.getName());
        itemView.setTag(breath);
    }
    public void setDataPhysical(ExercisesPhysicalRealmObject physical){
        Picasso.with(itemView.getContext())
                .load(physical.getLinkImage())
                .fit()
                .centerCrop()
                .into(imageView);
        tvName.setText(physical.getName());
        itemView.setTag(physical);
    }
    public void setDataOffice(OfficeRealmObject office){
        Picasso.with(itemView.getContext())
                .load(office.getImage())
                .fit()
                .centerCrop()
                .into(imageView);
        tvName.setText(office.getName());
        itemView.setTag(office);
    }
}
