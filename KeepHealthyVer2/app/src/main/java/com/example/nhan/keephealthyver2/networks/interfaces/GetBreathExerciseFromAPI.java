package com.example.nhan.keephealthyver2.networks.interfaces;

import com.example.nhan.keephealthyver2.networks.ApiUrl;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Nhan on 10/14/2016.
 */

public interface GetBreathExerciseFromAPI {
    @GET(ApiUrl.API_URL_BREATH)
    Call<Breath> callBreathExercise();
    class Breath{
        @SerializedName("breath")
        private List<ExerciseBreath> exerciseBreathList;

        public List<ExerciseBreath> getExerciseBreathList() {
            return exerciseBreathList;
        }
    }

    class ExerciseBreath{
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private String id;
        @SerializedName("color")
        private String color;
        @SerializedName("image")
        private String image;
        @SerializedName("time")
        private int time;
        @SerializedName("guide")
        private List<Label> listGuide;
        @SerializedName("info")
        private String info;
        @SerializedName("youtube_video")
        private String linkYoutube;

        public String getLinkYoutube() {
            return linkYoutube;
        }

        public String getInfo() {
            return info;
        }

        public String getName() {
            return name;
        }

        public int getTime() {
            return time;
        }

        public List<Label> getListGuide() {
            return listGuide;
        }

        public String getId() {
            return id;
        }

        public String getColor() {
            return color;
        }

        public String getImage() {
            return image;
        }
    }

    class Label{
        @SerializedName("label")
        private String label;

        public String getLabel() {
            return label;
        }
    }
}
