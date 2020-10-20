package com.ahmetgezici.appcentweather.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MinMax{

    @SerializedName("min_temp")
    @Expose
    public Double minTemp;
    @SerializedName("max_temp")
    @Expose
    public Double maxTemp;

}