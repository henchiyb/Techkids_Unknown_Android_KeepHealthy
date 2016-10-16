package com.example.nhan.keephealthyver2.networks.interfaces;

import com.example.nhan.keephealthyver2.networks.ApiUrl;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Nhan on 10/14/2016.
 */

public interface GetOfficeExerciseFromAPI {
    @GET(ApiUrl.API_URL_OFFICE)
    Call<Office> callOfficeExercise();
    class Office {
        @SerializedName("office")
        private List<ExerciseOffice> exerciseOfficesList;

        public List<ExerciseOffice> getExerciseOfficesList() {
            return exerciseOfficesList;
        }
    }

    class ExerciseOffice{
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String image;
        @SerializedName("time")
        private int time;
        @SerializedName("image_gif")
        private String imageGif;
        @SerializedName("info")
        private String info;

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public int getTime() {
            return time;
        }

        public String getImageGif() {
            return imageGif;
        }

        public String getInfo() {
            return info;
        }
    }

}
