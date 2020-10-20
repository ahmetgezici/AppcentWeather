package com.ahmetgezici.appcentweather.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {

    @SerializedName("consolidated_weather")
    @Expose
    public ArrayList<ConsolidatedWeather> consolidatedWeather = new ArrayList<>();
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("sun_rise")
    @Expose
    public String sunRise;
    @SerializedName("sun_set")
    @Expose
    public String sunSet;
    @SerializedName("timezone_name")
    @Expose
    public String timezoneName;
    @SerializedName("parent")
    @Expose
    public Parent parent;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("location_type")
    @Expose
    public String locationType;
    @SerializedName("woeid")
    @Expose
    public Integer woeid;
    @SerializedName("latt_long")
    @Expose
    public String lattLong;
    @SerializedName("timezone")
    @Expose
    public String timezone;

}