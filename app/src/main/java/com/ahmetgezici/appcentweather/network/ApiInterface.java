package com.ahmetgezici.appcentweather.network;

import com.ahmetgezici.appcentweather.model.City;
import com.ahmetgezici.appcentweather.model.ConsolidatedWeather;
import com.ahmetgezici.appcentweather.model.LocationSearch;
import com.ahmetgezici.appcentweather.model.MinMax;
import com.ahmetgezici.appcentweather.model.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api/location/{woeid}")
    Call<WeatherResponse> getData(@Path("woeid") String woeid);

    @GET("/api/location/search/?")
    Call<List<LocationSearch>> getLocation(@Query("lattlong") String lattlong);

    @GET("api/location/{woeid}/{date}")
    Call<List<ConsolidatedWeather>> getNextDate(@Path("woeid") String woeid, @Path("date") String date);

    @GET("api/location/{woeid}/{date}")
    Call<List<MinMax>> getMinMax(@Path("woeid") String woeid, @Path("date") String date);

    @GET("/api/location/search/?")
    Call<List<City>> getCity(@Query("query") String query);
}
