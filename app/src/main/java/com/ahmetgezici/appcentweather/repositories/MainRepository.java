package com.ahmetgezici.appcentweather.repositories;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ahmetgezici.appcentweather.model.MinMax;
import com.ahmetgezici.appcentweather.model.WeatherResponse;
import com.ahmetgezici.appcentweather.network.ApiClient;
import com.ahmetgezici.appcentweather.network.ApiInterface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    String TAG = "aaa";

    ApiInterface apiInterface;

    public MainRepository() {
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    }

    public MutableLiveData<WeatherResponse> requestWeatherResponse(String woeid) {

        MutableLiveData<WeatherResponse> mutableLiveData = new MutableLiveData<>();

        Call<WeatherResponse> weatherCall = apiInterface.getData(woeid);
        weatherCall.enqueue(new Callback<WeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<WeatherResponse> call, @NotNull Response<WeatherResponse> response) {
                if (response.code() == 200) {

                    Log.e(TAG, "onResponse: " + response.body());
                    WeatherResponse weatherResponse = response.body();
                    mutableLiveData.setValue(weatherResponse);

                }
            }

            @Override
            public void onFailure(@NotNull Call<WeatherResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return mutableLiveData;
    }


    public MutableLiveData<List<MinMax>> requestMinMax(String woeid, String date) {

        MutableLiveData<List<MinMax>> mutableLiveData = new MutableLiveData<>();

        Call<List<MinMax>> minMaxCall = apiInterface.getMinMax(woeid, date);
        minMaxCall.enqueue(new Callback<List<MinMax>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<List<MinMax>> call, @NotNull Response<List<MinMax>> response) {
                if (response.code() == 200) {
                    List<MinMax> minMax = response.body();
                    mutableLiveData.setValue(minMax);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<MinMax>> call, @NotNull Throwable t) {
                Log.e("aaa", "onFailure: " + t.getMessage());
            }
        });

        return mutableLiveData;
    }

}
