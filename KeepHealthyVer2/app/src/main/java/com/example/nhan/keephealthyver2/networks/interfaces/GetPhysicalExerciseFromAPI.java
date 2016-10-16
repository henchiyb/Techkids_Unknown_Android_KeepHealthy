package com.example.nhan.keephealthyver2.networks.interfaces;

import com.example.nhan.keephealthyver2.networks.ApiUrl;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Nhan on 10/14/2016.
 */

public interface GetPhysicalExerciseFromAPI {
    @GET(ApiUrl.API_URL_PHYSICAL)
    Call<Physical> callPhysicalExercise();
    class Physical{
        @SerializedName("physical")
        private List<Exercises> exercisesList;

        public List<Exercises> getExercisesList() {
            return exercisesList;
        }
    }

    class Exercises{
        @SerializedName("exercise")
        private List<ExercisePhysical> exercisePhysicalList;
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String linkImage;

        public String getName() {
            return name;
        }

        public String getLinkImage() {
            return linkImage;
        }

        public List<ExercisePhysical> getExercisePhysicalList() {
            return exercisePhysicalList;
        }
    }

    class ExercisePhysical {
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private String id;
        @SerializedName("color")
        private String color;
        @SerializedName("image_gif")
        private String image;
        @SerializedName("youtube_video")
        private String linkYoutube;

        public String getLinkYoutube() {
            return linkYoutube;
        }

        public String getName() {
            return name;
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
}
