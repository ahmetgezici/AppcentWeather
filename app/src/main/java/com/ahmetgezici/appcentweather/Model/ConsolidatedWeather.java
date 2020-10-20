package com.ahmetgezici.appcentweather.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsolidatedWeather {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("weather_state_name")
    @Expose
    public String weatherStateName;
    @SerializedName("weather_state_abbr")
    @Expose
    public String weatherStateAbbr;
    @SerializedName("wind_direction_compass")
    @Expose
    public String windDirectionCompass;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("applicable_date")
    @Expose
    public String applicableDate;
    @SerializedName("min_temp")
    @Expose
    public Double minTemp;
    @SerializedName("max_temp")
    @Expose
    public Double maxTemp;
    @SerializedName("the_temp")
    @Expose
    public Double theTemp;
    @SerializedName("wind_speed")
    @Expose
    public Double windSpeed;
    @SerializedName("wind_direction")
    @Expose
    public Double windDirection;
    @SerializedName("air_pressure")
    @Expose
    public Double airPressure;
    @SerializedName("humidity")
    @Expose
    public Integer humidity;
    @SerializedName("visibility")
    @Expose
    public Double visibility;
    @SerializedName("predictability")
    @Expose
    public Integer predictability;
}