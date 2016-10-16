package com.example.nhan.keephealthyver2.networks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nhan on 10/14/2016.
 */

public class ServiceFactory {
    private Retrofit retrofit;

    public ServiceFactory(String baseUrl){
        retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public <ServiceClass>ServiceClass createService(Class<ServiceClass> serviceClass){
        return retrofit.create(serviceClass);
    }
}
