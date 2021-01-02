package com.ahmetgezici.appcentweather.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {

    @SerializedName("consolidated_weather")
    @Expose
    @Nullable
    public ArrayList<ConsolidatedWeather> consolidatedWeather = new ArrayList<>();
    @SerializedName("time")
    @Expose
    @Nullable
    public String time;
    @SerializedName("sun_rise")
    @Expose
    @Nullable
    public String sunRise;
    @SerializedName("sun_set")
    @Expose
    @Nullable
    public String sunSet;
    @SerializedName("timezone_name")
    @Expose
    @Nullable
    public String timezoneName;
    @SerializedName("parent")
    @Expose
    @Nullable
    public Parent parent;
    @SerializedName("title")
    @Expose
    @Nullable
    public String title;
    @SerializedName("location_type")
    @Expose
    @Nullable
    public String locationType;
    @SerializedName("woeid")
    @Expose
    @Nullable
    public Integer woeid;
    @SerializedName("latt_long")
    @Expose
    @Nullable
    public String lattLong;
    @SerializedName("timezone")
    @Expose
    @Nullable
    public String timezone;

}