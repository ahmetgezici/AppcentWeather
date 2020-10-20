package com.ahmetgezici.appcentweather.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationSearch {

    @SerializedName("distance")
    @Expose
    public Integer distance;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("location_type")
    @Expose
    public String locationType;
    @SerializedName("woeid")
    @Expose
    public String woeid;
    @SerializedName("latt_long")
    @Expose
    public String lattLong;

}