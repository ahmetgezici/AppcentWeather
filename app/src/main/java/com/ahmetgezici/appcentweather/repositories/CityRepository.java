package com.ahmetgezici.appcentweather.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ahmetgezici.appcentweather.model.LocationSearch;
import com.ahmetgezici.appcentweather.network.ApiClient;
import com.ahmetgezici.appcentweather.network.ApiInterface;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityRepository {

    String TAG = "aaa";


    public MutableLiveData<List<LocationSearch>> requestLocationSearch(double latitude, double longitude) {

        MutableLiveData<List<LocationSearch>> mutableLiveData = new MutableLiveData<>();

        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);


        Log.e(TAG, decimalFormat.format(latitude) + "," + decimalFormat.format(longitude));

        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

        Call<List<LocationSearch>> locationSearchCall = apiInterface.getLocation(decimalFormat.format(latitude) + "," + decimalFormat.format(longitude));

        locationSearchCall.enqueue(new Callback<List<LocationSearch>>() {
            @Override
            public void onResponse(@NotNull Call<List<LocationSearch>> call, @NotNull Response<List<LocationSearch>> response) {

                if (response.code() == 200) {
                    List<LocationSearch> locationSearch = response.body();
                    mutableLiveData.postValue(locationSearch);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<LocationSearch>> call, @NotNull Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });

        return mutableLiveData;

    }

}
