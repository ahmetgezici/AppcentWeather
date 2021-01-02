package com.ahmetgezici.appcentweather.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsolidatedWeather {

    @SerializedName("id")
    @Expose
    @Nullable
    public String id;
    @SerializedName("weather_state_name")
    @Expose
    @Nullable
    public String weatherStateName;
    @SerializedName("weather_state_abbr")
    @Expose
    @Nullable
    public String weatherStateAbbr;
    @SerializedName("wind_direction_compass")
    @Expose
    @Nullable
    public String windDirectionCompass;
    @SerializedName("created")
    @Expose
    @Nullable
    public String created;
    @SerializedName("applicable_date")
    @Expose
    @Nullable
    public String applicableDate;
    @SerializedName("min_temp")
    @Expose
    @Nullable
    public Double minTemp;
    @SerializedName("max_temp")
    @Expose
    @Nullable
    public Double maxTemp;
    @SerializedName("the_temp")
    @Expose
    @Nullable
    public Double theTemp;
    @SerializedName("wind_speed")
    @Expose
    @Nullable
    public Double windSpeed;
    @SerializedName("wind_direction")
    @Expose
    @Nullable
    public Double windDirection;
    @SerializedName("air_pressure")
    @Expose
    @Nullable
    public Double airPressure;
    @SerializedName("humidity")
    @Expose
    @Nullable
    public Integer humidity;
    @SerializedName("visibility")
    @Expose
    @Nullable
    public Double visibility;
    @SerializedName("predictability")
    @Expose
    @Nullable
    public Integer predictability;
}